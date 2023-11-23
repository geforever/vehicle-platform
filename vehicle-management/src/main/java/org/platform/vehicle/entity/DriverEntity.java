package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Transient;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("t_driver")
public class DriverEntity implements Serializable {

    private static final long serialVersionUID = 7524639418774285453L;

    /**
     * 流水ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户ID
     */
    @TableField(value = "customer_id")
    private Integer customerId;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 创建人
     */
    @TableField(value = "create_person")
    private String createPerson;

    /**
     * 更新人
     */
    @TableField(value = "update_person")
    private String updatePerson;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 性别
     */
    @TableField(value = "sex", updateStrategy = FieldStrategy.IGNORED)
    private Integer sex;

    /**
     * 是否启用
     */
    @TableField(value = "is_enabled", updateStrategy = FieldStrategy.IGNORED)
    private Boolean isEnabled;

    /**
     * 姓名
     */
    @TableField(value = "driver_name", updateStrategy = FieldStrategy.IGNORED)
    private String driverName;

    /**
     * 手机号
     */
    @TableField(value = "mobile", updateStrategy = FieldStrategy.IGNORED)
    private String mobile;

    /**
     * 驾驶证编号
     */
    @TableField(value = "license_number", updateStrategy = FieldStrategy.IGNORED)
    private String licenseNumber;

    /**
     * 联系地址
     */
    @TableField(value = "contact_addr", updateStrategy = FieldStrategy.IGNORED)
    private String contactAddr;

    /**
     * 驾驶证照片
     */
    @TableField(value = "license_image", updateStrategy = FieldStrategy.IGNORED)
    private String licenseImage;

    /**
     * 准驾车型
     */
    @TableField(value = "approved_driving_type", updateStrategy = FieldStrategy.IGNORED)
    private String approvedDrivingType;

    /**
     * 出生日期
     */
    @TableField(value = "born_date", updateStrategy = FieldStrategy.IGNORED)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date bornDate;

    /**
     * 入职日期
     */
    @TableField(value = "join_date", updateStrategy = FieldStrategy.IGNORED)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date joinDate;

    /**
     * 领证日期
     */
    @TableField(value = "certificate_date", updateStrategy = FieldStrategy.IGNORED)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date certificateDate;

    /**
     * 所属客户名称
     */
    @Transient
    @TableField(exist = false)
    private String customerName;

    /**
     * 所属车队名称
     */
    @Transient
    @TableField(exist = false)
    private String fleetName;

}
