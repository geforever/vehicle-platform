package org.platform.vehicle.t808;

import io.github.yezhihao.netmc.core.model.Response;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import java.util.Map;
import java.util.TreeMap;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;
import org.platform.vehicle.commons.transform.ParameterConverter;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.查询终端参数应答)
public class T0104 extends JTMessage implements Response {

    @Field(length = 2, desc = "应答流水号")
    private int responseSerialNo;
    @Field(length = 1, desc = "应答参数个数")
    private int total;
    @Field(desc = "参数项列表", converter = ParameterConverter.class)
    private Map<Integer, Object> parameters;

    public int getResponseSerialNo() {
        return responseSerialNo;
    }

    public void setResponseSerialNo(int responseSerialNo) {
        this.responseSerialNo = responseSerialNo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<Integer, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, Object> parameters) {
        this.parameters = parameters;
        this.total = parameters.size();
    }

    public void addParameter(Integer key, Object value) {
        if (parameters == null)
            parameters = new TreeMap();
        parameters.put(key, value);
        total = parameters.size();
    }
}
