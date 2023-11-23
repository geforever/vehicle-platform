package org.platform.vehicle.jsatl12;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import io.netty.buffer.ByteBuf;
import org.platform.vehicle.basics.JTMessage;

/**
 * 文件数据上传
 * 帧头标识 0x30 0x31 0x63 0x64
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message
public class DataPacket extends JTMessage {

    @Field(length = 4, desc = "帧头标识")
    private int flag;
    @Field(length = 50, desc = "文件名称(文件类型_通道号_报警类型_序号_报警编号.后缀名)")
    private String name;
    @Field(length = 4, desc = "数据偏移量")
    private int offset;
    @Field(length = 4, desc = "数据长度")
    private int length;
    @Field(desc = "数据体")
    private ByteBuf data;

    @Override
    public String getClientId() {
        if (session != null)
            return session.getClientId();
        return null;
    }

    @Override
    public int getMessageId() {
        return flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ByteBuf getData() {
        return data;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(120);
        sb.append("DataPacket{name=").append(name);
        sb.append(",offset=").append(offset);
        sb.append(",length=").append(length);
        sb.append(",data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
