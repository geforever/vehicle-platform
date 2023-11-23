package org.platform.vehicle.t808;

import io.github.yezhihao.netmc.core.model.Response;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message({JT808.服务器补传分包请求, JT808.终端补传分包请求})
public class T8003 extends JTMessage implements Response {

    @Field(length = 2, desc = "原始消息流水号")
    private int responseSerialNo;
    @Field(totalUnit = 1, desc = "重传包ID列表", version = {-1, 0})
    @Field(totalUnit = 2, desc = "重传包ID列表", version = 1)
    private short[] id;

    public int getResponseSerialNo() {
        return responseSerialNo;
    }

    public void setResponseSerialNo(int responseSerialNo) {
        this.responseSerialNo = responseSerialNo;
    }

    public short[] getId() {
        return id;
    }

    public void setId(short[] id) {
        this.id = id;
    }
}
