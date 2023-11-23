package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.aspect.FieldName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (AssetTire)实体类
 *
 * @author geforever
 * @since 2023-09-14 14:42:31
 */
@Data
@TableName("asset_tire")
public class AssetTire implements Serializable {

    private static final long serialVersionUID = 117375869450728046L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 轮胎编号
     */
    @TableField("code")
    @FieldName(value = "轮胎编号")
    private String code;

    /**
     * 客户ID
     */
    @TableField("client_id")
    @FieldName(value = "客户ID")
    private Integer clientId;

    /**
     * 车队ID
     */
    @TableField("fleet_id")
    @FieldName(value = "车队ID")
    private Integer fleetId;

    /**
     * 仓库ID
     */
    @TableField("warehouse_id")
    @FieldName(value = "仓库ID")
    private Integer warehouseId;

    /**
     * 安装车辆
     */
    @TableField("license_plate")
    @FieldName(value = "安装车辆")
    private String licensePlate;

    /**
     * 轮位(主车备胎轮位为90和91，备胎轮位为 92,93)
     */
    @TableField("tire_site")
    @FieldName(value = "轮位")
    private Integer tireSite;

    /**
     * 轮位具体名称
     */
    @TableField("tire_site_name")
    @FieldName(value = "轮位具体名称")
    private String tireSiteName;

    /**
     * 轮位分类(轴位)
     */
    @TableField("tire_site_type")
    @FieldName(value = "轮位分类")
    private Integer tireSiteType;

    /**
     * 轮位分类名称
     */
    @TableField("tire_site_type_name")
    @FieldName(value = "轮位分类名称")
    private String tireSiteTypeName;

//    /**
//     * 车辆型号ID
//     */
//    @TableField("vehicle_spec_id")
//    @FieldName(value = "车辆型号ID")
//    private Integer vehicleSpecId;
//
    /**
     * 轮胎品牌ID
     */
    @TableField("tire_brand_id")
    @FieldName(value = "轮胎品牌ID")
    private Integer tireBrandId;

    /**
     * 轮胎型号ID
     */
    @TableField("tire_spec_id")
    @FieldName(value = "轮胎型号ID")
    private Integer tireSpecId;

    /**
     * 新旧程度(30,50,70,100)
     */
    @TableField("degree")
    @FieldName(value = "新旧程度")
    private String degree;

    /**
     * 当前里程
     */
    @TableField("mileage")
    @FieldName(value = "当前里程")
    private Integer mileage;

    /**
     * 传感器ID
     */
    @TableField("sensor_id")
    @FieldName(value = "传感器ID")
    private String sensorId;

    /**
     * 是否绑定传感器:0-未绑定,1-已绑定
     */
    @TableField("has_sensor")
    @FieldName(value = "是否绑定传感器")
    private Integer hasSensor;

    /**
     * 传感器绑定类型:1-捆绑式,2-背贴式,3-气门嘴式
     */
    @TableField("sensor_type")
    @FieldName(value = "传感器绑定类型")
    private Integer sensorType;

    /**
     * 轮胎状态:1-仓库待用,2-使用中,3-已变卖,4-已调拨,5-待用
     */
    @TableField("tire_status")
    @FieldName(value = "轮胎状态")
    private Integer tireStatus;

    /**
     * 是否删除:0-未删除,1-已删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("create_person")
    private String createPerson;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField("update_person")
    private String updatePerson;
}

