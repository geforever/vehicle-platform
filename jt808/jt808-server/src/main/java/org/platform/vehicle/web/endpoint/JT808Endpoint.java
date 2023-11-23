package org.platform.vehicle.web.endpoint;

import static org.platform.vehicle.commons.JT808.CAN总线数据上传;
import static org.platform.vehicle.commons.JT808.事件报告;
import static org.platform.vehicle.commons.JT808.位置信息查询应答;
import static org.platform.vehicle.commons.JT808.位置信息汇报;
import static org.platform.vehicle.commons.JT808.信息点播_取消;
import static org.platform.vehicle.commons.JT808.多媒体事件信息上传;
import static org.platform.vehicle.commons.JT808.多媒体数据上传;
import static org.platform.vehicle.commons.JT808.存储多媒体数据检索应答;
import static org.platform.vehicle.commons.JT808.定位数据批量上传;
import static org.platform.vehicle.commons.JT808.提问应答;
import static org.platform.vehicle.commons.JT808.摄像头立即拍摄命令应答;
import static org.platform.vehicle.commons.JT808.数据上行透传;
import static org.platform.vehicle.commons.JT808.数据压缩上报;
import static org.platform.vehicle.commons.JT808.查询区域或线路数据应答;
import static org.platform.vehicle.commons.JT808.查询服务器时间;
import static org.platform.vehicle.commons.JT808.查询终端参数应答;
import static org.platform.vehicle.commons.JT808.查询终端属性应答;
import static org.platform.vehicle.commons.JT808.电子运单上报;
import static org.platform.vehicle.commons.JT808.终端RSA公钥;
import static org.platform.vehicle.commons.JT808.终端升级结果通知;
import static org.platform.vehicle.commons.JT808.终端心跳;
import static org.platform.vehicle.commons.JT808.终端注册;
import static org.platform.vehicle.commons.JT808.终端注销;
import static org.platform.vehicle.commons.JT808.终端补传分包请求;
import static org.platform.vehicle.commons.JT808.终端通用应答;
import static org.platform.vehicle.commons.JT808.终端鉴权;
import static org.platform.vehicle.commons.JT808.行驶记录数据上传;
import static org.platform.vehicle.commons.JT808.车辆控制应答;
import static org.platform.vehicle.commons.JT808.驾驶员身份信息采集上报;

import com.alibaba.fastjson.JSONObject;
import io.github.yezhihao.netmc.core.annotation.Async;
import io.github.yezhihao.netmc.core.annotation.AsyncBatch;
import io.github.yezhihao.netmc.core.annotation.Endpoint;
import io.github.yezhihao.netmc.core.annotation.Mapping;
import io.github.yezhihao.netmc.session.Session;
import io.github.yezhihao.protostar.util.KeyValuePair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;
import org.platform.vehicle.t808.T0001;
import org.platform.vehicle.t808.T0100;
import org.platform.vehicle.t808.T0102;
import org.platform.vehicle.t808.T0104;
import org.platform.vehicle.t808.T0107;
import org.platform.vehicle.t808.T0108;
import org.platform.vehicle.t808.T0200;
import org.platform.vehicle.t808.T0201_0500;
import org.platform.vehicle.t808.T0301;
import org.platform.vehicle.t808.T0302;
import org.platform.vehicle.t808.T0303;
import org.platform.vehicle.t808.T0608;
import org.platform.vehicle.t808.T0700;
import org.platform.vehicle.t808.T0702;
import org.platform.vehicle.t808.T0704;
import org.platform.vehicle.t808.T0705;
import org.platform.vehicle.t808.T0800;
import org.platform.vehicle.t808.T0801;
import org.platform.vehicle.t808.T0802;
import org.platform.vehicle.t808.T0805;
import org.platform.vehicle.t808.T0900;
import org.platform.vehicle.t808.T0901;
import org.platform.vehicle.t808.T0A00_8A00;
import org.platform.vehicle.t808.T8003;
import org.platform.vehicle.t808.T8004;
import org.platform.vehicle.t808.T8100;
import org.platform.vehicle.t808.T8800;
import org.platform.vehicle.web.config.RabbitMqConfig;
import org.platform.vehicle.web.domain.GeoLocation;
import org.platform.vehicle.web.domain.TirePressure;
import org.platform.vehicle.web.model.entity.DeviceDO;
import org.platform.vehicle.web.model.enums.SessionKey;
import org.platform.vehicle.web.service.FileService;
import org.platform.vehicle.web.util.TirePressureConvert;

@Endpoint
@Component
public class JT808Endpoint {

	private static final Logger log = LoggerFactory.getLogger(JT808Endpoint.class);

	@Autowired
	private FileService fileService;

	@Autowired
	private RabbitTemplate rabbitTemplate;


	@Mapping(types = 终端通用应答, desc = "终端通用应答")
	public Object T0001(T0001 message, Session session) {

		System.out.println("111111111111111111");
		System.out.println("收到了一个应答");
		System.out.println(message.toString());

		log.error("111111111111111111");
		log.error("收到了一个终端应答");
		log.error(message.toString());

		session.response(message);
		return null;
	}

	@Mapping(types = 终端心跳, desc = "终端心跳")
	public void T0002(JTMessage message, Session session) {
		System.out.println("222222222222222222");
	}

	@Mapping(types = 终端注销, desc = "终端注销")
	public void T0003(JTMessage message, Session session) {
		session.invalidate();
	}

	@Mapping(types = 查询服务器时间, desc = "查询服务器时间")
	public T8004 T0004(JTMessage message, Session session) {
		System.out.println("333333333333333333");
		T8004 result = new T8004(LocalDateTime.now(ZoneOffset.UTC));
		return result;
	}

	@Mapping(types = 终端补传分包请求, desc = "终端补传分包请求")
	public void T8003(T8003 message, Session session) {
		System.out.println("4444444444444444");
	}

	@Mapping(types = 终端注册, desc = "终端注册")
	public T8100 T0100(T0100 message, Session session) {
		System.out.println("5555555555555555");
		session.register(message);
		DeviceDO device = new DeviceDO();
		device.setProtocolVersion(message.getProtocolVersion());
		device.setMobileNo(message.getClientId());
		device.setDeviceId(message.getDeviceId());
		device.setPlateNo(message.getPlateNo());
		session.setAttribute(SessionKey.Device, device);

		T8100 result = new T8100();
		result.setResponseSerialNo(message.getSerialNo());
		result.setToken(message.getDeviceId() + "," + message.getPlateNo());
		result.setResultCode(T8100.Success);
		return result;
	}

	@Mapping(types = 终端鉴权, desc = "终端鉴权")
	public T0001 T0102(T0102 message, Session session) {
		System.out.println("666666666666666666");
		session.register(message);
		DeviceDO device = new DeviceDO();
		String[] token = message.getToken().split(",");
		device.setProtocolVersion(message.getProtocolVersion());
		device.setMobileNo(message.getClientId());
		device.setDeviceId(token[0]);
		if(token.length > 1) {
            device.setPlateNo(token[1]);
        }
		session.setAttribute(SessionKey.Device, device);

		T0001 result = new T0001();
		result.setResponseSerialNo(message.getSerialNo());
		result.setResponseMessageId(message.getMessageId());
		result.setResultCode(T0001.Success);
		return result;
	}

	@Mapping(types = 查询终端参数应答, desc = "查询终端参数应答")
	public void T0104(T0104 message, Session session) {
		System.out.println("7777777777777");
		session.response(message);
	}

	@Mapping(types = 查询终端属性应答, desc = "查询终端属性应答")
	public void T0107(T0107 message, Session session) {
		System.out.println("8888888888888888");
		session.response(message);
	}

	@Mapping(types = 终端升级结果通知, desc = "终端升级结果通知")
	public void T0108(T0108 message, Session session) {
		System.out.println("99999999999999999");
	}

	/**
	 * 异步批量处理
	 * poolSize：参考数据库CPU核心数量
	 * maxElements：最大累积4000条记录处理一次
	 * maxWait：最大等待时间1秒
	 */
	@AsyncBatch(poolSize = 2, maxElements = 4000, maxWait = 1000)
	@Mapping(types = 位置信息汇报, desc = "位置信息汇报")
	public void T0200(List<T0200> list) {
		log.info("位置信息汇报,开始入库,数量:{}", list.size());
		// 推送到MQ
		this.saveLocationInfo(list);
	}

	private void saveLocationInfo(List<T0200> list) {
		List<GeoLocation> geoLocationSendList = new ArrayList<>();
		for(T0200 t200 : list) {
			GeoLocation gl = new GeoLocation();
			Date deviceTime = Date.from(t200.getDeviceTime().atZone(ZoneId.systemDefault()).toInstant());
			gl.setCreateTime(new Date());
			gl.setReceiverId(t200.getClientId());
			gl.setDeviceTime(deviceTime);
			gl.setLat(String.valueOf(t200.getLat()));
			gl.setLng(String.valueOf(t200.getLng()));
			gl.setAltitude(String.valueOf(t200.getAltitude()));
			gl.setMsgId(String.valueOf(t200.getMessageId()));
			gl.setSpeed(String.valueOf(t200.getSpeedKph()));
			gl.setWarnBit(t200.getWarnBit());
			gl.setStatusBit(t200.getStatusBit());
			geoLocationSendList.add(gl);
		}
		rabbitTemplate.convertAndSend(RabbitMqConfig.VEHICLE_EXCHANGE,
				RabbitMqConfig.VEHICLE_LOCATION_QUEUE_ROUTING_KEY,
				JSONObject.toJSON(geoLocationSendList));
	}

	@Mapping(types = 定位数据批量上传, desc = "定位数据批量上传")
	public void T0704(T0704 message) {
		System.out.println("BBBBBBBBBBBBBBBBBB");
	}

	@Mapping(types = {位置信息查询应答, 车辆控制应答}, desc = "位置信息查询应答/车辆控制应答")
	public void T0201_0500(T0201_0500 message, Session session) {
		System.out.println("CCCCCCCCCCCCCCCCCCCC");
		session.response(message);
	}

	@Mapping(types = 事件报告, desc = "事件报告")
	public void T0301(T0301 message, Session session) {
		System.out.println("DDDDDDDDDDDDDDDDDD");
	}

	@Mapping(types = 提问应答, desc = "提问应答")
	public void T0302(T0302 message, Session session) {
		System.out.println("EEEEEEEEEEEEEE");
	}

	@Mapping(types = 信息点播_取消, desc = "信息点播/取消")
	public void T0303(T0303 message, Session session) {
		System.out.println("FFFFFFFFFFFFFFFFFFFFF");
	}

	@Mapping(types = 查询区域或线路数据应答, desc = "查询区域或线路数据应答")
	public void T0608(T0608 message, Session session) {
		System.out.println("GGGGGGGGGGGGGGGGGG");
		session.response(message);
	}

	@Mapping(types = 行驶记录数据上传, desc = "行驶记录仪数据上传")
	public void T0700(T0700 message, Session session) {
		System.out.println("HHHHHHHHHHHHHHHHHHHHHHH");
		session.response(message);
	}

	@Mapping(types = 电子运单上报, desc = "电子运单上报")
	public void T0701(JTMessage message, Session session) {
		System.out.println("IIIIIIIIIIIIIIIIIIII");
	}

	@Mapping(types = 驾驶员身份信息采集上报, desc = "驾驶员身份信息采集上报")
	public void T0702(T0702 message, Session session) {
		System.out.println("JJJJJJJJJJJJJJJJJJJJJ");
		session.response(message);
	}

	@Mapping(types = CAN总线数据上传, desc = "CAN总线数据上传")
	public void T0705(T0705 message, Session session) {
		System.out.println("KKKKKKKKKKKKKKKKKK");
	}

	@Mapping(types = 多媒体事件信息上传, desc = "多媒体事件信息上传")
	public void T0800(T0800 message, Session session) {
		System.out.println("LLLLLLLLLLLLLLLLL");
	}

	@Async
	@Mapping(types = 多媒体数据上传, desc = "多媒体数据上传")
	public JTMessage T0801(T0801 message, Session session) {
		System.out.println("MMMMMMMMMMMMMMMMMM");
		if(message.getPacket() == null) {
			T0001 result = new T0001();
			result.copyBy(message);
			result.setMessageId(JT808.平台通用应答);
			result.setSerialNo(session.nextSerialNo());

			result.setResponseSerialNo(message.getSerialNo());
			result.setResponseMessageId(message.getMessageId());
			result.setResultCode(T0001.Success);
			return result;
		}
		fileService.saveMediaFile(message);
		T8800 result = new T8800();
		result.setMediaId(message.getId());
		return result;
	}

	@Mapping(types = 存储多媒体数据检索应答, desc = "存储多媒体数据检索应答")
	public void T0802(T0802 message, Session session) {
		System.out.println("NNNNNNNNNNNNNNNNNNNNNNN");
		session.response(message);
	}

	@Mapping(types = 摄像头立即拍摄命令应答, desc = "摄像头立即拍摄命令应答")
	public void T0805(T0805 message, Session session) {
		System.out.println("OOOOOOOOOOOOOOOOO");
		session.response(message);
	}

	@AsyncBatch(poolSize = 2, maxElements = 4000, maxWait = 1000)
	@Mapping(types = 数据上行透传, desc = "数据上行透传")
	public void T0900(List<T0900> messageList) {
		List<T0900> tirePressureMessageList = new ArrayList<>();
		for (T0900 message : messageList) {
			KeyValuePair<Integer, Object> keyValuePair = message.getMessage();
			if(keyValuePair.getKey() == 242) {
				tirePressureMessageList.add(message);
			}
		}
		// F2(242) 为胎压数据透传 ID
		if(!tirePressureMessageList.isEmpty()) {
			List<TirePressure> tirePressureList = new ArrayList<>();
			for (T0900 message : tirePressureMessageList) {
				TirePressure tirePressure = TirePressureConvert.convert(message);
				tirePressureList.add(tirePressure);
			}
			rabbitTemplate.convertAndSend(RabbitMqConfig.VEHICLE_EXCHANGE,
					RabbitMqConfig.VEHICLE_TIRE_DATA_QUEUE_ROUTING_KEY,
					JSONObject.toJSON(tirePressureList));
		}
	}



	@Mapping(types = 数据压缩上报, desc = "数据压缩上报")
	public void T0901(T0901 message, Session session) {
		System.out.println("QQQQQQQQQQQQQQQQQQQQQQ");
	}

	@Mapping(types = 终端RSA公钥, desc = "终端RSA公钥")
	public void T0A00(T0A00_8A00 message, Session session) {
		System.out.println("RRRRRRRRRRRRRRRRRRR");
		session.response(message);
	}
}
