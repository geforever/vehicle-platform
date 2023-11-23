package org.platform.vehicle.entity;

import java.util.Date;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/23 15:07
 */
@Data
public class TirePressureData {

    /**
     * 流水ID
     */
    private Long id;

    /**
     * 主表ID
     */
    private Long parentCode;

    /**
     * 终端ID号
     */
    private String clientId;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 轮胎类型:0-主车,1-挂车
     */
    private Integer type;

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
     * 轮胎报警数据
     */
    private TireWarningData tireWarningData;

}
