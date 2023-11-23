package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Transient;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/11/2 11:34
 */

@Data
@TableName("t_vehicle")
public class VehicleEntity {

    /**
     * 流水ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Integer customerId;

    /**
     * 车队ID
     */
    @TableField("fleet_id")
    private Integer fleetId;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 创建人
     */
    @TableField("create_person")
    private String createPerson;

    /**
     * 更新人
     */
    @TableField("update_person")
    private String updatePerson;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 车辆品牌
     */
    @TableField("brand_id")
    private Integer brandId;

    /**
     * 车辆型号
     */
    @TableField("spec_id")
    private Integer specId;

    /**
     * 车牌号
     */
    @TableField(value = "license_plate")
    private String licensePlate;

    /**
     * 挂车车牌号
     */
    @TableField(value = "gua_license_plate")
    private String guaLicensePlate;

    /**
     * 运行路线
     */
    @TableField(value = "run_route", updateStrategy = FieldStrategy.IGNORED)
    private String runRoute;

    /**
     * 发动机号
     */
    @TableField(value = "engine_number")
    private String engineNumber;

    /**
     * 车辆识别号
     */
    @TableField(value = "vehicle_id_number")
    private String vehicleIdNumber;

    /**
     * 中继器ID
     */
    @TableField(value = "repeater_id_number", updateStrategy = FieldStrategy.IGNORED)
    private String repeaterIdNumber;

    /**
     * 拖车中继器ID
     */
    @TableField(value = "trailer_repeater_id_number", updateStrategy = FieldStrategy.IGNORED)
    private String trailerRepeaterIdNumber;

    /**
     * 接收器ID
     */
    @TableField(value = "receiver_id_number", updateStrategy = FieldStrategy.IGNORED)
    private String receiverIdNumber;

    /**
     * 上牌日期
     */
    @TableField("regist_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registDate;

    /**
     * 当前里程
     */
    @TableField(value = "current_mileage")
    private BigDecimal currentMileage;

    /**
     * 轮胎是否全部安装
     */
    @TableField("wheels_complete")
    private Boolean wheelsComplete;

    /**
     * 传感器是否全部安装
     */
    @TableField("sensors_complete")
    private Boolean sensorsComplete;

    /**
     * 所属客户名称
     */
    @Transient
    @TableField(exist = false)
    private String customerName;

    /**
     * 图片列表
     */
    @Transient
    @TableField(exist = false)
    private List<String> imageList;

    /**
     * 车辆品牌名称
     */
    @Transient
    @TableField(exist = false)
    private String brandName;

    /**
     * 车辆规格名称
     */
    @Transient
    @TableField(exist = false)
    private String specName;

    /**
     * 所属车队名称
     */
    @Transient
    @TableField(exist = false)
    private String fleetName;

    /**
     * 是否运营中:0-否,1-是
     */
    @TableField("is_running")
    private Integer isRunning;

    /**
     * 运营开始时间
     */
    @TableField("running_start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runningStartTime;

    /**
     * 运营结束时间
     */
    @TableField("running_end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runningEndTime;

}
