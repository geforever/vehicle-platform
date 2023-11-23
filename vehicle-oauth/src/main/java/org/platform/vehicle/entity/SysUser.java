package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/8/23 15:28
 */
@Data
@TableName("sys_user")
public class SysUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    @TableField("code")
    private String code;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

    /**
     * 头像地址
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 状态：0-禁用, 1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否删除：1-删除， 0-未删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

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
     * 最后登录时间
     */
    @TableField("last_login_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
}
