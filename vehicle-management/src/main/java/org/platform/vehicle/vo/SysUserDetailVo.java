package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/29 15:29
 */
@Data
public class SysUserDetailVo {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 账号归属ID
     */
    private Integer customerId;

    /**
     * 账号归属名称
     */
    private String customerName;

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
     * 角色ID
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

}
