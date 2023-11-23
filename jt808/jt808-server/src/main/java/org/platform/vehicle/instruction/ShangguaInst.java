package org.platform.vehicle.instruction;

import io.github.yezhihao.protostar.util.KeyValuePair;
import lombok.Data;
import org.platform.vehicle.t808.T8900;


@Data
public class ShangguaInst {

	public String clientId;

	private String zhongjiqiId;

	public T8900 toT8900() {
		T8900 t8900 = new T8900();
		t8900.setClientId(this.clientId);
		KeyValuePair<Integer, Object> kvp = new KeyValuePair<Integer, Object>(0xe0);

		StringBuffer sb = new StringBuffer();

		//TODO: 需要中继器ID
		sb.append(this.zhongjiqiId);


		kvp.setValue(sb.toString());
		t8900.setMessage(kvp);
		return t8900;
	}

	public ShangguaInst(String clientId, String zhongjiqiId) {
		super();
		this.clientId = clientId;
		this.zhongjiqiId = zhongjiqiId;
	}

}
