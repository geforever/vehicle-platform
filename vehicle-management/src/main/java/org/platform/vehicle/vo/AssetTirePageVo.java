package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/14 14:56
 */
@Data
public class AssetTirePageVo {

    private Integer id;

    /**
     * 轮胎编号
     */
    private String code;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 安装车辆
     */
    private String licensePlate;

    /**
     * 轮位
     */
    private String tireSiteName;

    /**
     * 轮位类型
     */
    private String tireSiteTypeName;


    /**
     * 轮胎品牌名称
     */
    private String tireBrandName;

    /**
     * 轮胎型号
     */
    private String tireSpecName;

    /**
     * 新旧程度(30,50,70,100)
     */
    private String degree;

    /**
     * 当前里程KM
     */
    private Integer mileage;

    /**
     * 传感器ID
     */
    private String sensorId;

    /**
     * 轮胎状态:1-仓库待用,2-使用中,3-已变卖,4-已调拨
     */
    private Integer tireStatus;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updatePerson;
}
