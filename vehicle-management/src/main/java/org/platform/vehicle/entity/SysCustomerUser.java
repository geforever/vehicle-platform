package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/24 10:44
 */
@Data
@TableName("sys_customer_user")
public class SysCustomerUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Integer customerId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;
}
