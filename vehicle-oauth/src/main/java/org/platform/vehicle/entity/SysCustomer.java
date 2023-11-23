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
 * sys_customer
 *
 * @author
 */
@Data
@TableName("sys_customer")
public class SysCustomer implements Serializable {

    private static final long serialVersionUID = -3008646711317891550L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 公司ID:客户的customerId是自身主键ID
     */
    @TableField("company_id")
    private Integer companyId;

    /**
     * 联系人姓名
     */
    @TableField("contact_name")
    private String contactName;

    /**
     * 联系人
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 省ID
     */
    @TableField("province_id")
    private Integer provinceId;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 市ID
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 区/县
     */
    @TableField("county")
    private String county;

    /**
     * 区/县ID
     */
    @TableField("county_id")
    private Integer countyId;

    /**
     * 管理员ID
     */
    @TableField("admin_user_id")
    private Integer adminUserId;

    /**
     * 客户类型:0-面心,1-客户,2-一级车队,3-二级车队
     */
    @TableField("type")
    private Integer type;

    /**
     * 状态:0-停用,1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 最大车辆数
     */
    @TableField("max_vehicle")
    private Integer maxVehicle;

    /**
     * 发票信息
     */
    @TableField("bill_info")
    private String billInfo;

    /**
     * 系统有效期限
     */
    @TableField("invalid_time")
    private Date invalidTime;

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
     * 是否删除:0-未删除，1-已删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 父ID
     */
    @TableField("parent_id")
    private Integer parentId;

}
