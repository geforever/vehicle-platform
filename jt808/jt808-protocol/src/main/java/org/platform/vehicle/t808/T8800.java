package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.多媒体数据上传应答)
public class T8800 extends JTMessage {

    @Field(length = 4, desc = "多媒体ID(大于0) 如收到全部数据包则没有后续字段")
    private int mediaId;
    @Field(totalUnit = 1, desc = "重传包ID列表")
    private short[] id;

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public short[] getId() {
        return id;
    }

    public void setId(short[] id) {
        this.id = id;
    }
}
