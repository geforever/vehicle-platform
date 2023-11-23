package org.platform.vehicle.instruction;

import io.github.yezhihao.protostar.util.KeyValuePair;
import lombok.Data;
import org.platform.vehicle.t808.T8900;

@Data
public class YuzhiInst {

	public String clientId;

	private String yuzhi;

	private String tireSiteId;

	@SuppressWarnings("boxing")
	public T8900 toT8900() {
		T8900 t8900 = new T8900();
		t8900.setClientId(this.clientId);
		KeyValuePair<Integer, Object> kvp = new KeyValuePair<Integer, Object>(0xBB);
		
		
//7E
//8900
//000E
//18 30 43 20 00 43
//0001
//F2
//01
//05
//74
//45
//74
//45
//74
//45
//74
//45
//74
//45
//8C
//C5
//7E
		// 7E8900000E1830432000430001F20105744574457445744574458CC57E
		
		String code = "7E8900000E1821032000010001F20105744574457445744574458C137E";
		//             7E89000000182103200001006EFC7E
		
		
		StringBuffer valBuffer = new StringBuffer();
		valBuffer.append("7e");//开始 7E
		valBuffer.append("8900");//消息ID 89 00
		valBuffer.append("0009");//消息体长度
		valBuffer.append("183043200043");//GPS ID
		valBuffer.append("12");//此条消息的流水号 
		valBuffer.append("f2");//胎压数据透传 ID
		valBuffer.append("01");//操作模式
		valBuffer.append("05");//轴总数
		valBuffer.append("20");//第1轴高压
		valBuffer.append("11");//第1轴低压
		valBuffer.append("20");//第2轴高压
		valBuffer.append("11");//第2轴低压
		valBuffer.append("20");//第3轴高压
		valBuffer.append("11");//第3轴低压
		valBuffer.append("20");//第4轴高压
		valBuffer.append("11");//第5轴低压
		valBuffer.append("46");//高温
		valBuffer.append("0");//校验码
		valBuffer.append("7e");//结束
		
		//kvp.setValue(Integer.toHexString(Integer.valueOf(this.yuzhi)));
		
		kvp.setValue(code);
		
		t8900.setMessage(kvp);
		return t8900;
	}

	public YuzhiInst(String clientId, String yuzhi, String tireSiteId) {
		super();
		this.clientId = clientId;
		this.yuzhi = yuzhi;
		this.tireSiteId = tireSiteId;
	}
	
	

}
