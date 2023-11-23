package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.utils.UserContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Transient;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("t_vehicle_spec")
public class VehicleSpecEntity implements Serializable {

    private static final long serialVersionUID = 3238701353418985921L;

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
     * 型号名称
     */
    @TableField("spec_name")
    private String specName;

    /**
     * 品牌ID
     */
    @TableField("brand_id")
    private Integer brandId;

    /**
     * 车型分类（1：主车；2：挂车；3：主挂一体）
     */
    @TableField("spec_type")
    private Integer specType;

    /**
     * 车型轴数（2-6）
     */
    @TableField("wheelbase_count")
    private Integer wheelbaseCount;

    /**
     * 车轴类型
     */
    @TableField("wheelbase_type")
    private String wheelbaseType;

    /**
     * 每轴轮数
     */
    @TableField("wheel_count")
    private String wheelCount;

    /**
     * 轮位排布
     */
    @TableField(value = "wheel_arrange", updateStrategy = FieldStrategy.IGNORED)
    private String wheelArrange;


    /**
     * 低压警报等级1
     */
    @TableField(value = "low_pressure_alarm_level1", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal lowPressureAlarmLevel1;

    /**
     * 高压警报等级1
     */
    @TableField(value = "high_pressure_alarm_level1", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal highPressureAlarmLevel1;

    /**
     * 高温警报等级1
     */
    @TableField(value = "high_temperature_alarm_level1", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal highTemperatureAlarmLevel1;

    /**
     * 低压警报等级2
     */
    @TableField(value = "low_pressure_alarm_level2", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal lowPressureAlarmLevel2;

    /**
     * 高压警报等级2
     */
    @TableField(value = "high_pressure_alarm_level2", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal highPressureAlarmLevel2;

    /**
     * 高温警报等级2
     */
    @TableField(value = "high_temperature_alarm_level2", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal highTemperatureAlarmLevel2;

    /**
     * 二级警报启用状态
     */
    @TableField("enable_lev2_alarm")
    private Boolean enableLev2Alarm;

    /**
     * 所属客户名称
     */
    @Transient
    @TableField(exist = false)
    private String customerName;

    /**
     * 保养手册
     */
    @Transient
    @TableField(exist = false)
    private List<VehicleSpecMaintenaceEntity> maintenaceList;

    /**
     * 品牌名称
     */
    @Transient
    @TableField(exist = false)
    private String brandName;

    public VehicleSpecEntity(Integer customerId, String specName, Integer brandId, Integer specType,
            Integer wheelbaseCount, String wheelbaseType, String wheelCount,
            BigDecimal lowPressureAlarmLevel1,
            BigDecimal highPressureAlarmLevel1, BigDecimal highTemperatureAlarmLevel1,
            BigDecimal lowPressureAlarmLevel2, BigDecimal highPressureAlarmLevel2,
            BigDecimal highTemperatureAlarmLevel2, Boolean enableLev2Alarm) {
        this(customerId, specName, brandId, specType, wheelbaseCount, wheelbaseType, wheelCount,
                lowPressureAlarmLevel1,
                highPressureAlarmLevel1, highTemperatureAlarmLevel1, lowPressureAlarmLevel2,
                highPressureAlarmLevel2,
                highTemperatureAlarmLevel2, enableLev2Alarm, UserContext.getUser().getName(),
                UserContext.getUser().getName());
    }

    public VehicleSpecEntity() {
        this.enableLev2Alarm = Boolean.TRUE;
        this.isDeleted = Boolean.FALSE;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public VehicleSpecEntity(Integer customerId, String specName, Integer brandId, Integer specType,
            Integer wheelbaseCount, String wheelbaseType, String wheelCount,
            BigDecimal lowPressureAlarmLevel1,
            BigDecimal highPressureAlarmLevel1, BigDecimal highTemperatureAlarmLevel1,
            BigDecimal lowPressureAlarmLevel2, BigDecimal highPressureAlarmLevel2,
            BigDecimal highTemperatureAlarmLevel2, Boolean enableLev2Alarm, String createPerson,
            String updatePerson) {
        this();
        this.customerId = customerId;
        this.specName = specName;
        this.brandId = brandId;
        this.specType = specType;
        this.wheelbaseCount = wheelbaseCount;
        this.wheelbaseType = wheelbaseType;
        this.wheelCount = wheelCount;
        this.lowPressureAlarmLevel1 = lowPressureAlarmLevel1;
        this.highPressureAlarmLevel1 = highPressureAlarmLevel1;
        this.highTemperatureAlarmLevel1 = highTemperatureAlarmLevel1;
        this.lowPressureAlarmLevel2 = lowPressureAlarmLevel2;
        this.highPressureAlarmLevel2 = highPressureAlarmLevel2;
        this.highTemperatureAlarmLevel2 = highTemperatureAlarmLevel2;
        this.enableLev2Alarm = enableLev2Alarm;
        this.createPerson = createPerson;
        this.updatePerson = updatePerson;
    }

}
