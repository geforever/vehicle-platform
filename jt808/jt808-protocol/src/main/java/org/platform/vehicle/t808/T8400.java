package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.电话回拨)
public class T8400 extends JTMessage {

    /** 通话 */
    public static final int Normal = 0;
    /** 监听 */
    public static final int Listen = 1;

    @Field(length = 1, desc = "类型：0.通话 1.监听")
    private int type;
    @Field(length = 20, desc = "电话号码")
    private String phoneNumber;

    public T8400() {
    }

    public T8400(int type, String phoneNumber) {
        this.type = type;
        this.phoneNumber = phoneNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
