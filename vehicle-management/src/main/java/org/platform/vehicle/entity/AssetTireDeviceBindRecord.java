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
 * (AssetTireDeviceBindRecord)实体类
 *
 * @author geforever
 * @since 2023-09-15 15:34:40
 */
@Data
@TableName("asset_tire_device_bind_record")
public class AssetTireDeviceBindRecord implements Serializable {

    private static final long serialVersionUID = -55911075418844300L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 各个配件唯一编号
     */
    @TableField("code")
    private String code;

    /**
     * 设备
     */
    @TableField("device_type")
    private String deviceType;

    /**
     * 车牌
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 轮位号
     */
    @TableField("tire_site_name")
    private String tireSiteName;

    /**
     * 轮胎号
     */
    @TableField("tire_code")
    private String tireCode;

    /**
     * 创建人
     */
    @TableField("create_person")
    private String createPerson;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}

