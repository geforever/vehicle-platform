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
 * (AssetTireFitRecord)实体类
 *
 * @author geforever
 * @since 2023-09-19 15:26:22
 */
@Data
@TableName("asset_tire_fit_record")
public class AssetTireFitRecord implements Serializable {

    private static final long serialVersionUID = -36949787718587372L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 客户id
     */
    @TableField("client_id")
    private Integer clientId;

    /**
     * 车队名称
     */
    @TableField("fleet_name")
    private String fleetName;

    /**
     * 车队id
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
     * 轮位名称
     */
    @TableField("tire_site_name")
    private String tireSiteName;

    /**
     * 品牌
     */
    @TableField("brand_name")
    private String brandName;

    /**
     * 类型:1-安装,2-拆卸
     */
    @TableField("type")
    private Integer type;

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

