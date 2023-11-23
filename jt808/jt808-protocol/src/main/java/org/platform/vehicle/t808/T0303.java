package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 * 该消息2019版本已删除
 */
@Message(JT808.信息点播_取消)
public class T0303 extends JTMessage {

    @Field(length = 1, desc = "消息类型")
    private int type;
    @Field(length = 1, desc = "点播/取消标志：0.取消 1.点播")
    private int action;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
