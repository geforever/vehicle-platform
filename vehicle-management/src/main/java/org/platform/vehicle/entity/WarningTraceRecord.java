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
 * (WarningTraceRecord)实体类
 *
 * @author geforever
 * @since 2023-09-27 16:05:55
 */
@Data
@TableName("warning_trace_record")
public class WarningTraceRecord implements Serializable {

    private static final long serialVersionUID = -21984838937402380L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 流水号
     */
    @TableField("trace_no")
    private String traceNo;

    /**
     * 轮胎号
     */
    @TableField("tire_code")
    private String tireCode;

    /**
     * 车队ID
     */
    @TableField("fleet_id")
    private Integer fleetId;

    /**
     * 车队名称
     */
    @TableField("fleet_name")
    private String fleetName;

    /**
     * 客户ID
     */
    @TableField("client_id")
    private Integer clientId;

    /**
     * 客户名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 车牌
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 轮位号
     */
    @TableField("tire_site_id")
    private Integer tireSiteId;

    /**
     * 轮位名称
     */
    @TableField("tire_site_name")
    private String tireSiteName;

    /**
     * 报警类型:逗号分割
     */
    @TableField("warning_type")
    private String warningType;

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
     * 电压
     */
    @TableField("voltage")
    private String voltage;

    /**
     * 当前地址
     */
    @TableField("location")
    private String location;

    /**
     * 跟进类型:1-紧急类,2-常规类
     */
    @TableField("type")
    private Integer type;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否跟进:0-否,1-是
     */
    @TableField("is_follow")
    private Integer isFollow;

    /**
     * 跟进时间
     */
    @TableField("follow_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date followTime;

}

