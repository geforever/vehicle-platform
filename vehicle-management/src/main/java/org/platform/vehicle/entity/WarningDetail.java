package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (WarningDetail)实体类
 *
 * @author geforever
 * @since 2023-09-27 16:05:00
 */
@Data
@TableName("warning_detail")
public class WarningDetail implements Serializable {

    private static final long serialVersionUID = -74280773154686625L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 接收器ID(GPS_ID,设备client_id)
     */
    @TableField("receiver_id")
    private String receiverId;

    /**
     * 设备记录流水号
     */
    @TableField("serial_no")
    private String serialNo;

    /**
     * 流水号
     */
    @TableField("trace_no")
    private String traceNo;


    /**
     * 客户ID
     */
    @TableField("client_id")
    private Integer clientId;

    /**
     * 车队ID
     */
    @TableField("fleet_id")
    private Integer fleetId;

    /**
     * 车牌
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 轮胎号
     */
    @TableField("tire_code")
    private String tireCode;

    /**
     * 轮位
     */
    @TableField("tire_site")
    private Integer tireSite;

    /**
     * 轮位名称
     */
    @TableField("tire_site_name")
    private String tireSiteName;

    /**
     * 告警类型:1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
     */
    @TableField("warning_type")
    private Integer warningType;

    /**
     * 压力
     */
    @TableField("pressure")
    private String pressure;

    /**
     * 温度
     */
    @TableField("temperature")
    private String temperature;

    /**
     * 压力阈值
     */
    @TableField("pressure_threshold")
    private String pressureThreshold;

    /**
     * 温度阈值
     */
    @TableField("temperature_threshold")
    private String temperatureThreshold;


    /**
     * 是否恢复:0-否,1是
     */
    @TableField("is_recovery")
    private Integer isRecovery;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}

