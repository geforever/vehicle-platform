package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.多媒体事件信息上传)
public class T0800 extends JTMessage {

    @Field(length = 4, desc = "多媒体数据ID")
    private int id;
    @Field(length = 1, desc = "多媒体类型：0.图像 1.音频 2.视频 ")
    private int type;
    @Field(length = 1, desc = "多媒体格式编码：0.JPEG 1.TIF 2.MP3 3.WAV 4.WMV ")
    private int format;
    @Field(length = 1, desc = "事件项编码")
    private int event;
    @Field(length = 1, desc = "通道ID")
    private int channelId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
}
