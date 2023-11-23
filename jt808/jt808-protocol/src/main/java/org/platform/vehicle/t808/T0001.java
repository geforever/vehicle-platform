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
@Message({JT808.平台通用应答, JT808.终端通用应答})
public class T0001 extends JTMessage implements Response {

    public static final int Success = 0; //成功、确认
    public static final int Failure = 1;//失败
    public static final int MessageError = 2;//消息有误
    public static final int NotSupport = 3;//不支持
    public static final int AlarmAck = 4;//报警处理确认

    @Field(length = 2, desc = "应答流水号")
    private int responseSerialNo;
    @Field(length = 2, desc = "应答ID")
    private int responseMessageId;
    @Field(length = 1, desc = "结果：0.成功 1.失败 2.消息有误 3.不支持 4.报警处理确认")
    private int resultCode;

    public int getResponseSerialNo() {
        return responseSerialNo;
    }

    public void setResponseSerialNo(int responseSerialNo) {
        this.responseSerialNo = responseSerialNo;
    }

    public int getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(int responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isSuccess() {
        return this.resultCode == Success;
    }
}
