package org.platform.vehicle.web.model.entity;

import java.util.Date;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/7 09:17
 */
@Data
public class TireNewestDataEntity {

    private Integer id;

    /**
     * 终端ID号
     */
    private String clientId;

    /**
     * 消息流水号
     */
    private String serialNo;

    /**
     * 挂车状态:1-上挂,2-下挂
     */
    private Integer trailerStatus;

    /**
     * 挂车中继器ID
     */
    private String guaRepeaterId;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 轮胎类型:0-主车,1-挂车
     */
    private Integer tireType;

    /**
     * 轮胎位置
     */
    private String tireSiteId;

    /**
     * 胎压传感器ID
     */
    private String tireSensorId;

    /**
     * 电压
     */
    private String voltage;

    /**
     * 胎压
     */
    private String tirePressure;

    /**
     * 胎温
     */
    private String tireTemperature;

    /**
     * 电池电压状态:0-正常,1-电池电压低
     */
    private Integer batteryVoltageStatus;

    /**
     * 是否超时:0-正常,1-当长时间(60 分钟)没有收到发射器的数据后此位置 1，(此时忽略压力温度 状态字节)
     */
    private Integer isTimeout;

    /**
     * 方案:0:自动定位关闭 1:自动定位开启(自动定位方案)/0:智能甩挂关闭 1:智能甩挂开启(智能甩挂方案)
     */
    private Integer scheme;

    /**
     * 胎压状态:0-正常,1-高压,2-低压
     */
    private Integer tirePressureStatus;

    /**
     * 胎温状态:0-正常,1-异常
     */
    private Integer tireTemperatureStatus;

    /**
     * 轮胎状态:0-正常状态,1-急漏气,2-加气,3-未定义
     */
    private Integer tireStatus;

    /**
     * 设备时间
     */
    private Date deviceTime;
}
