package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/25 15:20
 */
@Data
public class SysUserEditParam {

    /**
     * 主键ID
     *
     * @required
     */
    private Integer id;

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
     * 状态：1-启用，0-禁用
     *
     * @required
     */
    private Integer status;
}
