package org.platform.vehicle.jsatl12;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import java.time.LocalDateTime;
import java.util.List;
import org.platform.vehicle.util.StrUtils;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JSATL12;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JSATL12.报警附件信息消息)
public class T1210 extends JTMessage {

    @Field(length = 7, desc = "终端ID", version = {-1, 0})
    @Field(length = 30, desc = "终端ID(粤标)", version = 1)
    private String deviceId;

    @Field(length = 7, desc = "终端ID", version = {-1, 0})
    @Field(length = 30, desc = "终端ID(粤标)", version = 1)
    private String deviceId_;
    @Field(length = 6, charset = "BCD", desc = "时间(YYMMDDHHMMSS)")
    private LocalDateTime dateTime;
    @Field(length = 1, desc = "序号(同一时间点报警的序号，从0循环累加)")
    private int sequenceNo;
    @Field(length = 1, desc = "附件数量")
    private int fileTotal;
    @Field(length = 1, desc = "预留", version = {-1, 0})
    @Field(length = 2, desc = "预留(粤标)", version = 1)
    private int reserved;

    @Field(length = 32, desc = "报警编号")
    private String platformAlarmId;
    @Field(length = 1, desc = "信息类型：0.正常报警文件信息 1.补传报警文件信息")
    private int type;
    @Field(totalUnit = 1, desc = "附件信息列表")
    private List<Item> items;

    public String getDeviceId() {
        if (StrUtils.isBlank(deviceId))
            return deviceId_;
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        this.deviceId_ = deviceId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public int getFileTotal() {
        return fileTotal;
    }

    public void setFileTotal(int fileTotal) {
        this.fileTotal = fileTotal;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public String getPlatformAlarmId() {
        return platformAlarmId;
    }

    public void setPlatformAlarmId(String platformAlarmId) {
        this.platformAlarmId = platformAlarmId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        @Field(lengthUnit = 1, desc = "文件名称")
        private String name;
        @Field(length = 4, desc = "文件大小")
        private long size;

        private transient T1210 parent;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public T1210 parent() {
            return parent;
        }

        public Item parent(T1210 parent) {
            this.parent = parent;
            return this;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(80);
            sb.append("{name=").append(name);
            sb.append(",size=").append(size);
            sb.append('}');
            return sb.toString();
        }
    }
}
