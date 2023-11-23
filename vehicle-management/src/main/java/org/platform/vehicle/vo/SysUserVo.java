package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/8/23 17:43
 */
@Data
public class SysUserVo {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户编号
     */
    private String code;

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 账号归属
     */
    private String customerName;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 状态：0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 用户类型:1-超级管理员,2-客户管理员,3-一般用户
     */
    private Integer type;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 最后登录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
