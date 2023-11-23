package org.platform.vehicle.entity;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/24 10:15
 */
@Data
public class TireWarningData {

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
}
