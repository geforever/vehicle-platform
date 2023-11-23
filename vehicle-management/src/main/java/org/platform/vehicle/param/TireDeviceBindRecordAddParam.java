package org.platform.vehicle.param;

import java.util.Date;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/15 15:43
 */
@Data
public class TireDeviceBindRecordAddParam {

    /**
     * 客户ID
     */
    private Integer clientId;

    /**
     * 车队ID
     */
    private Integer fleetId;

    /**
     * id(各个配件唯一编号)
     */
    private String code;

    /**
     * 设备:传感器,中继器,GPS
     */
    private String deviceType;

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 轮位号
     */
    private String tireSiteName;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private Date createTime;

}
