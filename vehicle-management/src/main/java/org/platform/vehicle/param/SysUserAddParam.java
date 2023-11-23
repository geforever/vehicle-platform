package org.platform.vehicle.param;

import org.platform.vehicle.constant.SysUserConstant;
import java.io.Serializable;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/24 09:04
 */
@Data
public class SysUserAddParam implements Serializable {

    private static final long serialVersionUID = -5505982806124367738L;

    /**
     * 账号归属
     *
     * @required
     */
    private Integer customerId;

    /**
     * 账号
     *
     * @required
     */
    private String account;

    /**
     * 密码(默认传888888)
     *
     * @required
     */
    private String password;

    /**
     * 手机号
     *
     * @required
     */
    private String phone;

    /**
     * 姓名
     *
     * @required
     */
    private String name;

    /**
     * 角色ID
     *
     * @required
     */
    private Integer roleId;

    /**
     * 用户类型:1-超级管理员,2-客户管理员,3-一般用户
     */
    private Integer type = 3;

    public SysUserAddParam(Integer customerId, String account, String password, String phone,
            String name,
            Integer roleId) {
        super();
        this.customerId = customerId;
        this.account = account;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.roleId = roleId;
    }

    public SysUserAddParam(Integer customerId, String account, String phone, String name,
            Integer roleId) {
        super();
        this.customerId = customerId;
        this.account = account;
        this.phone = phone;
        this.name = name;
        this.roleId = roleId;
        this.password = SysUserConstant.DEFAULT_PASSWORD;
    }

    public SysUserAddParam() {
        super();
    }

}
