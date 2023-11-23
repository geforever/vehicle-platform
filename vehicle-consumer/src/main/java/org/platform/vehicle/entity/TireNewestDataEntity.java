package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/11/7 09:17
 */
@Data
@TableName("jtt808_tire_newest_data")
public class TireNewestDataEntity implements Serializable {

    private static final long serialVersionUID = 3625594722158314629L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 终端ID号
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 消息流水号
     */
    @TableField("serial_no")
    private String serialNo;

    /**
     * 挂车状态:1-上挂,2-下挂
     */
    @TableField("trailer_status")
    private Integer trailerStatus;

    /**
     * 挂车中继器ID
     */
    @TableField("gua_repeater_id")
    private String guaRepeaterId;

    /**
     * 经度
     */
    @TableField("longitude")
    private String longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private String latitude;

    /**
     * 轮胎类型:0-主车,1-挂车
     */
    @TableField("tire_type")
    private Integer tireType;

    /**
     * 轮胎位置
     */
    @TableField("tire_site_id")
    private String tireSiteId;

    /**
     * 胎压传感器ID
     */
    @TableField("tire_sensor_id")
    private String tireSensorId;

    /**
     * 电压
     */
    @TableField("voltage")
    private String voltage;

    /**
     * 胎压
     */
    @TableField("tire_pressure")
    private String tirePressure;

    /**
     * 胎温
     */
    @TableField("tire_temperature")
    private String tireTemperature;

    /**
     * 电池电压状态:0-正常,1-电池电压低
     */
    @TableField("battery_voltage_status")
    private Integer batteryVoltageStatus;

    /**
     * 是否超时:0-正常,1-当长时间(60 分钟)没有收到发射器的数据后此位置 1，(此时忽略压力温度 状态字节)
     */
    @TableField("is_timeout")
    private Integer isTimeout;

    /**
     * 方案:0:自动定位关闭 1:自动定位开启(自动定位方案)/0:智能甩挂关闭 1:智能甩挂开启(智能甩挂方案)
     */
    @TableField("scheme")
    private Integer scheme;

    /**
     * 胎压状态:0-正常,1-高压,2-低压
     */
    @TableField("tire_pressure_status")
    private Integer tirePressureStatus;

    /**
     * 胎温状态:0-正常,1-异常
     */
    @TableField("tire_temperature_status")
    private Integer tireTemperatureStatus;

    /**
     * 轮胎状态:0-正常状态,1-急漏气,2-加气,3-未定义
     */
    @TableField("tire_status")
    private Integer tireStatus;

    /**
     * 设备时间
     */
    @TableField("device_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deviceTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TireNewestDataEntity that = (TireNewestDataEntity) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(tireType, that.tireType) &&
                Objects.equals(tireSiteId, that.tireSiteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, tireType, tireSiteId);
    }
}
