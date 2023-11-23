package org.platform.vehicle.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.yezhihao.netmc.session.Session;
import io.github.yezhihao.netmc.session.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.swagger.v3.oas.annotations.Operation;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("mx")
public class MxController {

	@Autowired
	private SessionManager sessionManager;

	/**
	 * 下挂指令
	 *
	 * @param clientId GPS ID（12位的数字或字母）
	 **/
	@Operation(summary = "下挂")
	@PostMapping("xiagua")
	public Mono<String> xiagua(@RequestParam String clientId) {
		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append("8900");// 数据上行透传消息ID
		messageBuffer.append("0009");// 消息体数据长度
		messageBuffer.append(clientId);// GPS ID
		messageBuffer.append(calcSerialNum(4));// 此条消息的流水号
		messageBuffer.append("F3");// F3 为设置胎压传感器 ID 和轮位绑定
		messageBuffer.append("01");// 操作模式：1 表示设置
		messageBuffer.append("01");// 配置轮胎个数 1～22 有效，胎压传感器接收机最多配 22 个轮胎(这边的实际应用场景是只绑定一个轮胎，所以值固定为1)

		messageBuffer.append("00");// 传感器ID 固定值00
		messageBuffer.append("11");// 传感器ID 固定值11
		messageBuffer.append("11");// 传感器ID 固定值11
		messageBuffer.append("11");// 传感器ID 固定值11
		messageBuffer.append("03");// 拖卡号 固定值03
		messageBuffer.append("10");// 轮胎序号 固定值10

		messageBuffer.append(calcValidateNum(messageBuffer));// 校验码
		messageBuffer.append("7E");
		messageBuffer.insert(0, "7E");
		Session session = this.sessionManager.get(clientId);
		if(session != null) {
			ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(messageBuffer.toString()));
			return session.notify(byteBuf).map(unused -> "success")
					.timeout(Duration.ofSeconds(10), Mono.just("timeout"))
					.onErrorResume(throwable -> Mono.just("fail"));
		}
		return Mono.just("offline");
	}

	/**
	 * 上挂指令
	 *
	 * @param clientId GPS ID（12位的数字或字母）
	 * @param chuanganqiId 传感器ID（挂车的中继器ID）（6位的数字或字母）
	 **/
	@Operation(summary = "上挂")
	@PostMapping("shanggua")
	public Mono<String> shanggua(@RequestParam String clientId, @RequestParam String chuanganqiId) {
		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append("8900");// 数据上行透传消息ID
		messageBuffer.append("0009");// 消息体数据长度
		messageBuffer.append(clientId);// GPS ID
		messageBuffer.append(calcSerialNum(4));// 此条消息的流水号
		messageBuffer.append("F3");// F3 为设置胎压传感器 ID 和轮位绑定
		messageBuffer.append("01");// 操作模式：1 表示设置
		messageBuffer.append("01");// 配置轮胎个数 1～22 有效，胎压传感器接收机最多配 22 个轮胎(这边的实际应用场景是只绑定一个轮胎，所以值固定为1)

		messageBuffer.append("00");// 传感器ID 固定值00
		messageBuffer.append(chuanganqiId);// 传感器ID
		messageBuffer.append("03");// 拖卡号 固定值03
		messageBuffer.append("08");// 轮胎序号 固定值08

		messageBuffer.append(calcValidateNum(messageBuffer));// 校验码
		messageBuffer.append("7E");
		messageBuffer.insert(0, "7E");
		Session session = this.sessionManager.get(clientId);
		if(session != null) {
			ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(messageBuffer.toString()));
			return session.notify(byteBuf).map(unused -> "success")
					.timeout(Duration.ofSeconds(10), Mono.just("timeout"))
					.onErrorResume(throwable -> Mono.just("fail"));
		}
		return Mono.just("offline");
	}

	/**
	 * 下发车轴阈值
	 *
	 * @param clientId GPS ID（12位的数字或字母）
	 * @param thresholdJson 车轴阈值表达式
	 * 		此处参数数字全部统一采用10进制数字
	 * 		格式实例 :
	 * 		{
	 * 			"zhoushu" : 5 //当前设置车辆轴总数
	 * 			"taiya" : [
	 * 				{"idx": 1, "gaoya" : 4.1, "diya" : 2.2},//一轴高压低压
	 * 				{"idx": 2, "gaoya" : 4.1, "diya" : 2.2},//二轴高压低压
	 * 				{"idx": 3, "gaoya" : 4.1, "diya" : 2.2} //三轴高压低压
	 * 			],
	 * 			"gaowen" : 70  //高温阈值
	 * 		}
	 * @return
	 **/
	// 8900 000E 183043200043 0001 F2 01 05 14 0A 14 0A 14 0A 14 0A 14 0A 64
	@Operation(summary = "阈值")
	@PostMapping("yuzhi")
	public Mono<String> yuzhi(@RequestParam String clientId, @RequestParam String thresholdJson) {
		JSONObject jsonObj = JSON.parseObject(thresholdJson);
		int zhoushu = jsonObj.getIntValue("zhoushu");
		Integer gaowen = jsonObj.getInteger("gaowen");
		JSONArray taiyaArr = jsonObj.getJSONArray("taiya");

		List<TaiyaInfo> taiyaInfoList = new ArrayList<TaiyaInfo>(zhoushu);
		for(int i = 0; i < zhoushu; i++) {
			TaiyaInfo taiyaInfo = null;
			for(int j = 0; j < taiyaArr.size(); j++) {
				JSONObject taiya = taiyaArr.getJSONObject(j);
				if(taiya.getInteger("idx").intValue() == i + 1) {
					taiyaInfo = new TaiyaInfo(taiya.getInteger("idx").intValue(), taiya.getDoubleValue("gaoya"),
							taiya.getDoubleValue("diya"));
				}
			}
			taiyaInfoList.add(taiyaInfo);
		}

		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append("8900");// 数据上行透传消息ID
		messageBuffer.append(calcMessLen(zhoushu));// 消息体数据长度
		messageBuffer.append(clientId);// GPS ID
		messageBuffer.append(calcSerialNum(4));// 此条消息的流水号
		messageBuffer.append("F2");// F2 为设置胎压(高压/低压/高温/低电压)门限值透传ID
		messageBuffer.append("01");// 操作模式：1 表示设置
		messageBuffer.append(calcHexStr(zhoushu, 2));// 当前设置车辆轴总数
		for(TaiyaInfo ti : taiyaInfoList) {
			if(ti == null) {
				messageBuffer.append("0000");// 如果没有该轴的下发值，则高压低压都用00填充
			} else {
				messageBuffer.append(calcTaiya(ti.getGaoya()));// 高压
				messageBuffer.append(calcTaiya(ti.getDiya()));// 低压
			}
		}
		messageBuffer.append(calcGaowen(gaowen));// 高温
		messageBuffer.append(calcValidateNum(messageBuffer));// 校验码
		messageBuffer.append("7E");
		messageBuffer.insert(0, "7E");

		Session session = this.sessionManager.get(clientId);
		if(session != null) {
			ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(messageBuffer.toString()));
			return session.notify(byteBuf).map(unused -> "success")
					.timeout(Duration.ofSeconds(10), Mono.just("timeout"))
					.onErrorResume(throwable -> Mono.just("fail"));
		}
		return Mono.just("offline");
	}

	/**
	 * 下发中继器绑定指令
	 *
	 *
	 * @param cheType 车辆类型（1表示主车；2表示挂车）
	 * @param clientId GPS ID（12位的数字或字母）
	 * @param zhongjiqiId 中继器ID（6位的数字或字母）
	 **/
	@Operation(summary = "绑定中继器")
	@PostMapping("zhongjiqi")
	public Mono<String> zhongjiqi(@RequestParam int cheType, @RequestParam String clientId,
			@RequestParam String zhongjiqiId) {
		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append("8900");// 数据上行透传消息ID
		messageBuffer.append("0009");// 消息体数据长度
		messageBuffer.append(clientId);// GPS ID
		messageBuffer.append(calcSerialNum(4));// 此条消息的流水号
		messageBuffer.append("F3");// F3 为设置胎压传感器 ID 和轮位绑定
		messageBuffer.append("01");// 操作模式：1 表示设置
		messageBuffer.append("01");// 配置轮胎个数 1～22 有效，胎压传感器接收机最多配 22 个轮胎(这边的实际应用场景是只绑定一个轮胎，所以值固定为1)

		messageBuffer.append(zhongjiqiId);// 智能中继器ID或普通中继器ID（智能中继器就是挂车中继器，普通中继器就是车头中继器）
		messageBuffer.append("03");// 托卡号，设置中继器时使用固定值3
		messageBuffer.append(cheType == 1 ? "07" : "05");// 轮胎序号，设置中继器时使用固定值（主车是04，挂车是05）

		messageBuffer.append(calcValidateNum(messageBuffer));// 校验码
		messageBuffer.append("7E");
		messageBuffer.insert(0, "7E");
		Session session = this.sessionManager.get(clientId);
		if(session != null) {
			ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(messageBuffer.toString()));
			return session.notify(byteBuf).map(unused -> "success")
					.timeout(Duration.ofSeconds(10), Mono.just("timeout"))
					.onErrorResume(throwable -> Mono.just("fail"));
		}
		return Mono.just("offline");
	}

	/**
	 * 下发中继器绑定指令
	 *
	 *
	 * @param cheType 车辆类型（1表示主车；2表示挂车）
	 * @param luntaiNum 轮胎序号（ 01～20 有效，1-32）
	 * @param clientId GPS ID（12位的数字或字母）
	 * @param chuanganqiId 传感器ID（6位的数字或字母）
	 **/
	@Operation(summary = "轮位同步")
	@PostMapping("lunwei")
	public Mono<String> lunwei(@RequestParam int cheType, @RequestParam int luntaiNum, @RequestParam String clientId,
			@RequestParam String chuanganqiId) {
		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append("8900");// 数据上行透传消息ID
		messageBuffer.append("0009");// 消息体数据长度
		messageBuffer.append(clientId);// GPS ID
		messageBuffer.append(calcSerialNum(4));// 此条消息的流水号
		messageBuffer.append("F3");// F3 为设置胎压传感器 ID 和轮位绑定
		messageBuffer.append("01");// 操作模式：1 表示设置
		messageBuffer.append("01");// 配置轮胎个数 1～22 有效，胎压传感器接收机最多配 22 个轮胎(这边的实际应用场景是只绑定一个轮胎，所以值固定为1)
		messageBuffer.append(chuanganqiId);// 胎压传感器 ID
		messageBuffer.append(calcHexStr(cheType - 1, 2));// 托卡号： 00表示主车，01表示挂车
		messageBuffer.append(calcHexStr(luntaiNum, 2));// 轮胎序号 02 01～20 有效，1-32
		messageBuffer.append(calcValidateNum(messageBuffer));// 校验码
		messageBuffer.append("7E");
		messageBuffer.insert(0, "7E");
		Session session = this.sessionManager.get(clientId);
		if(session != null) {
			ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(messageBuffer.toString()));
			return session.notify(byteBuf).map(unused -> "success")
					.timeout(Duration.ofSeconds(10), Mono.just("timeout"))
					.onErrorResume(throwable -> Mono.just("fail"));
		}
		return Mono.just("offline");
	}

	/**
	 * 下发阈值时，根据消息片段（消息头+消息体）计算校验码
	 *
	 * @param messFrag 消息片段（消息头+消息体）
	 * @return String 返回校验码（16进制，2位）
	 */
	private static String calcValidateNum(StringBuffer messFrag) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < messFrag.length(); i++) {
			if(i > 0 && i % 2 == 0) {
				sb.append(",");
			}
			sb.append(messFrag.charAt(i));
		}
		String[] arr = sb.toString().split(",");
		int x = -1;
		for(String s : arr) {
			int v = Integer.parseInt(s, 16);
			if(x == -1) {
				x = v;
				continue;
			}
			x ^= v;
		}
		return calcHexStr(x, 2);
	}

	/**
	 * 下发阈值时，计算消息体长度
	 *
	 * @param zhoushu 轴数
	 * @return String 返回消息体长度（16进制，4位）
	 */
	private static String calcMessLen(int zhoushu) {
		return calcHexStr(zhoushu * 2 + 4, 4);
	}

	/**
	 * 下发阈值时，生成随机流水号
	 *
	 * @param len 流水号长度
	 * @return String 返回随机流水号（16进制）
	 */
	private static String calcSerialNum(int len) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < len; i++) {
			SecureRandom random = new SecureRandom();
			int x = random.nextInt(10);
			// 生成随机数
			sb.append(Integer.toString(x, 16));
		}
		return sb.toString();
	}

	/**
	 * 下发阈值时，计算16进制胎压消息数据
	 *
	 * @param taiya 十进制胎压
	 * @return String 返回16进制胎压消息数据
	 */
	private static String calcTaiya(double taiya) {
		return calcHexStr((int)(taiya * 10), 2);
	}

	/**
	 * 下发阈值时，计算16进制高温消息数据
	 *
	 * @param gaowen 十进制高温
	 * @return String 返回16进制高温消息数据
	 */
	private static String calcGaowen(Integer gaowen) {
		return (gaowen == null) ? "00" : calcHexStr(gaowen.intValue() + 50, 2);
	}

	/**
	 * 将十进制数字转化为指定长度的十六进制数字字符串（位数不足前面补0）
	 *
	 * @param val int 十进制数字
	 * @param hexStrLen int 十六进制数字字符串的长度
	 * @return String 返回十六进制数字字符串
	 */
	private static String calcHexStr(int val, int hexStrLen) {
		String hexStr = Integer.toString(val, 16);
		for(int i = 0, x = hexStrLen - hexStr.length(); i < x; i++) {
			hexStr = "0" + hexStr;
		}
		return hexStr;
	}
}

@Data
class TaiyaInfo {
	int idx;
	double gaoya;
	double diya;

	TaiyaInfo(int idx, double gaoya, double diya) {
		this.idx = idx;
		this.gaoya = gaoya;
		this.diya = diya;
	}

	@Override
	public String toString() {
		return "TaiyaInfo [idx=" + this.idx + ", gaoya=" + this.gaoya + ", diya=" + this.diya + "]";
	}

}
