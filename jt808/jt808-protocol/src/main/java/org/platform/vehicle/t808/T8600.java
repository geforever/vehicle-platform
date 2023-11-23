package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import java.time.LocalDateTime;
import java.util.List;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.Bit;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.设置圆形区域)
public class T8600 extends JTMessage {

    /** @see org.yzh.protocol.commons.ShapeAction */
    @Field(length = 1, desc = "设置属性")
    private int action;
    @Field(totalUnit = 1, desc = "区域项")
    private List<Circle> items;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List<Circle> getItems() {
        return items;
    }

    public void setItems(List<Circle> items) {
        this.items = items;
    }

    public static class Circle {
        @Field(length = 4, desc = "区域ID")
        private int id;
        @Field(length = 2, desc = "区域属性")
        private int attribute;
        @Field(length = 4, desc = "中心点纬度")
        private int latitude;
        @Field(length = 4, desc = "中心点经度")
        private int longitude;
        @Field(length = 4, desc = "半径(米)")
        private int radius;
        @Field(length = 6, lengthExpression = "attrBit(0) ? 6 : 0", charset = "BCD", desc = "起始时间(若区域属性0位为0则没有该字段)")
        private LocalDateTime startTime;
        @Field(length = 6, lengthExpression = "attrBit(0) ? 6 : 0", charset = "BCD", desc = "结束时间(若区域属性0位为0则没有该字段)")
        private LocalDateTime endTime;
        @Field(length = 2, lengthExpression = "attrBit(1) ? 2 : 0", desc = "最高速度(公里每小时,若区域属性1位为0则没有该字段)")
        private Integer maxSpeed;
        @Field(length = 1, lengthExpression = "attrBit(1) ? 1 : 0", desc = "超速持续时间(秒,若区域属性1位为0则没有该字段)")
        private Integer duration;
        @Field(length = 2, lengthExpression = "attrBit(1) ? 2 : 0", desc = "夜间最高速度(公里每小时,若区域属性1位为0则没有该字段)", version = 1)
        private Integer nightMaxSpeed;
        @Field(lengthUnit = 2, desc = "区域名称", version = 1)
        private String name;

        public Circle() {
        }

        public Circle(int id, int attribute, int latitude, int longitude, int radius, LocalDateTime startTime, LocalDateTime endTime, Integer maxSpeed, Integer duration) {
            this.id = id;
            this.attribute = attribute;
            this.latitude = latitude;
            this.longitude = longitude;
            this.radius = radius;
            this.setStartTime(startTime);
            this.setEndTime(endTime);
            this.setMaxSpeed(maxSpeed);
            this.setDuration(duration);
        }

        public Circle(int id, int attribute, int latitude, int longitude, int radius, LocalDateTime startTime, LocalDateTime endTime, Integer maxSpeed, Integer duration, Integer nightMaxSpeed, String name) {
            this(id, attribute, latitude, longitude, radius, startTime, endTime, maxSpeed, duration);
            this.setNightMaxSpeed(nightMaxSpeed);
            this.name = name;
        }

        public boolean attrBit(int i) {
            return Bit.isTrue(attribute, i);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAttribute() {
            return attribute;
        }

        public void setAttribute(int attribute) {
            this.attribute = attribute;
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

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.attribute = Bit.set(attribute, 0, startTime != null);
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.attribute = Bit.set(attribute, 0, endTime != null);
            this.endTime = endTime;
        }

        public Integer getMaxSpeed() {
            return maxSpeed;
        }

        public void setMaxSpeed(Integer maxSpeed) {
            this.attribute = Bit.set(attribute, 1, maxSpeed != null);
            this.maxSpeed = maxSpeed;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.attribute = Bit.set(attribute, 1, duration != null);
            this.duration = duration;
        }

        public Integer getNightMaxSpeed() {
            return nightMaxSpeed;
        }

        public void setNightMaxSpeed(Integer nightMaxSpeed) {
            this.attribute = Bit.set(attribute, 1, nightMaxSpeed != null);
            this.nightMaxSpeed = nightMaxSpeed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(240);
            sb.append("{id=").append(id);
            sb.append(",attribute=[").append(Integer.toBinaryString(attribute)).append(']');
            sb.append(",longitude=").append(longitude);
            sb.append(",latitude=").append(latitude);
            sb.append(",radius=").append(radius);
            sb.append(",startTime=").append(startTime);
            sb.append(",endTime=").append(endTime);
            sb.append(",maxSpeed=").append(maxSpeed);
            sb.append(",duration=").append(duration);
            sb.append(",nightMaxSpeed=").append(nightMaxSpeed);
            sb.append(",name=").append(name);
            sb.append('}');
            return sb.toString();
        }
    }
}
