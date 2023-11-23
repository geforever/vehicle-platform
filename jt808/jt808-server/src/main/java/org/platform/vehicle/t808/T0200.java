package org.platform.vehicle.t808;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import java.time.LocalDateTime;
import java.util.Map;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;
import org.platform.vehicle.commons.transform.AttributeConverter;
import org.platform.vehicle.commons.transform.AttributeConverterYue;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@JsonIgnoreProperties({"attributes", "alarmList", "dateTime", "messageId", "properties", "protocolVersion", "clientId", "serialNo", "packageTotal", "packageNo", "verified", "bodyLength", "encryption", "subpackage", "version", "reserved"})
@Message(JT808.位置信息汇报)
public class T0200 extends JTMessage {

    @Field(length = 4, desc = "报警标志")
    private int warnBit;
    @Field(length = 4, desc = "状态")
    private int statusBit;
    @Field(length = 4, desc = "纬度")
    private int latitude;
    @Field(length = 4, desc = "经度")
    private int longitude;
    @Field(length = 2, desc = "高程(米)")
    private int altitude;
    @Field(length = 2, desc = "速度(1/10公里每小时)")
    private int speed;
    @Field(length = 2, desc = "方向")
    private int direction;
    @Field(length = 6, charset = "BCD", desc = "时间(YYMMDDHHMMSS)")
    private LocalDateTime deviceTime;
    @Field(converter = AttributeConverter.class, desc = "位置附加信息", version = {-1, 0})
    @Field(converter = AttributeConverterYue.class, desc = "位置附加信息(粤标)", version = 1)
    private Map<Integer, Object> attributes;

    public int getWarnBit() {
        return warnBit;
    }

    public void setWarnBit(int warnBit) {
        this.warnBit = warnBit;
    }

    public int getStatusBit() {
        return statusBit;
    }

    public void setStatusBit(int statusBit) {
        this.statusBit = statusBit;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public LocalDateTime getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(LocalDateTime deviceTime) {
        this.deviceTime = deviceTime;
    }

    public Map<Integer, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Integer, Object> attributes) {
        this.attributes = attributes;
    }

    public int getAttributeInt(int key) {
        if (attributes != null) {
            Integer value = (Integer) attributes.get(key);
            if (value != null) {
                return value;
            }
        }
        return 0;
    }

    public long getAttributeLong(int key) {
        if (attributes != null) {
            Long value = (Long) attributes.get(key);
            if (value != null) {
                return value;
            }
        }
        return 0L;
    }

    private boolean updated;
    private double lng;
    private double lat;
    private float speedKph;

    @Override
    public boolean transform() {
        if (deviceTime == null)
            return false;
        lng = longitude / 1000000d;
        lat = latitude / 1000000d;
        speedKph = speed / 10f;
        return true;
    }


    public boolean updated() {
        return updated || !(updated = true);
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public float getSpeedKph() {
        return speedKph;
    }

    @Override
    public String toString() {
        StringBuilder sb = toStringHead();
        sb.append("T0200{deviceTime=").append(deviceTime);
        sb.append(",longitude=").append(longitude);
        sb.append(",latitude=").append(latitude);
        sb.append(",altitude=").append(altitude);
        sb.append(",speed=").append(speed);
        sb.append(",direction=").append(direction);
        sb.append(",warnBit=").append(Integer.toBinaryString(warnBit));
        sb.append(",statusBit=").append(Integer.toBinaryString(statusBit));
        sb.append(",attributes=").append(attributes);
        sb.append('}');
        return sb.toString();
    }
}
