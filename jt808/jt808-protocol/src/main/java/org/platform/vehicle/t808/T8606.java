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
@Message(JT808.设置路线)
public class T8606 extends JTMessage {

    @Field(length = 4, desc = "路线ID")
    private int id;
    @Field(length = 2, desc = "路线属性")
    private int attribute;
    @Field(length = 6, charset = "BCD", desc = "起始时间(若区域属性0位为0则没有该字段)")
    private LocalDateTime startTime;
    @Field(length = 6, charset = "BCD", desc = "结束时间(若区域属性0位为0则没有该字段)")
    private LocalDateTime endTime;
    @Field(totalUnit = 2, desc = "拐点列表")
    private List<Line> items;
    @Field(lengthUnit = 2, desc = "区域名称", version = 1)
    private String name;

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

    public List<Line> getItems() {
        return items;
    }

    public void setItems(List<Line> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Line {
        @Field(length = 4, desc = "拐点ID")
        private int id;
        @Field(length = 4, desc = "路段ID")
        private int routeId;
        @Field(length = 4, desc = "纬度")
        private int latitude;
        @Field(length = 4, desc = "经度")
        private int longitude;
        @Field(length = 1, desc = "宽度(米)")
        private int width;
        @Field(length = 1, desc = "属性")
        private int attribute;
        @Field(length = 2, desc = "路段行驶过长阈值(秒,若区域属性0位为0则没有该字段)")
        private Integer upperLimit;
        @Field(length = 2, desc = "路段行驶不足阈值(秒,若区域属性0位为0则没有该字段)")
        private Integer lowerLimit;
        @Field(length = 2, desc = "路段最高速度(公里每小时,若区域属性1位为0则没有该字段)")
        private Integer maxSpeed;
        @Field(length = 1, desc = "路段超速持续时间(秒,若区域属性1位为0则没有该字段)")
        private Integer duration;
        @Field(length = 2, desc = "夜间最高速度(公里每小时,若区域属性1位为0则没有该字段)", version = 1)
        private Integer nightMaxSpeed;

        public Line() {
        }

        public Line(int id, int routeId, int latitude, int longitude, int width, int attribute, Integer upperLimit, Integer lowerLimit, Integer maxSpeed, Integer duration) {
            this.id = id;
            this.routeId = routeId;
            this.latitude = latitude;
            this.longitude = longitude;
            this.width = width;
            this.attribute = attribute;
            this.setUpperLimit(upperLimit);
            this.setLowerLimit(lowerLimit);
            this.setMaxSpeed(maxSpeed);
            this.setDuration(duration);
        }

        public Line(int id, int routeId, int latitude, int longitude, int width, int attribute, Integer upperLimit, Integer lowerLimit, Integer maxSpeed, Integer duration, Integer nightMaxSpeed) {
            this(id, routeId, latitude, longitude, width, attribute, upperLimit, lowerLimit, maxSpeed, duration);
            this.setNightMaxSpeed(nightMaxSpeed);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRouteId() {
            return routeId;
        }

        public void setRouteId(int routeId) {
            this.routeId = routeId;
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

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getAttribute() {
            return attribute;
        }

        public void setAttribute(int attribute) {
            this.attribute = attribute;
        }

        public Integer getUpperLimit() {
            return upperLimit;
        }

        public void setUpperLimit(Integer upperLimit) {
            this.attribute = Bit.set(attribute, 0, upperLimit != null);
            this.upperLimit = upperLimit;
        }

        public Integer getLowerLimit() {
            return lowerLimit;
        }

        public void setLowerLimit(Integer lowerLimit) {
            this.attribute = Bit.set(attribute, 0, lowerLimit != null);
            this.lowerLimit = lowerLimit;
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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(240);
            sb.append("{id=").append(id);
            sb.append(",routeId=").append(routeId);
            sb.append(",longitude=").append(longitude);
            sb.append(",latitude=").append(latitude);
            sb.append(",width=").append(width);
            sb.append(",attribute=[").append(Integer.toBinaryString(attribute)).append(']');
            sb.append(",upperLimit=").append(upperLimit);
            sb.append(",lowerLimit=").append(lowerLimit);
            sb.append(",maxSpeed=").append(maxSpeed);
            sb.append(",duration=").append(duration);
            sb.append('}');
            return sb.toString();
        }
    }
}
