package org.platform.vehicle.constant;

/**
 * @Author gejiawei
 * @Date 2023/8/23 17:38
 */
public class SysUserConstant {

    /**
     * 用户类型:1-超级管理员,2-客户管理员,3-一般用户
     */
    public static final int ADMINISTRATOR_ID = 1;
    public static final int CLIENT_ADMINISTRATOR_ID = 2;
    public static final int GENERAL_USER_ID = 3;

    public static final String DEFAULT_PASSWORD = "888888";

    /**
     * 是否删除:0-未删除,1-已删除
     */
    public static final int IS_DELETED = 1;

    public static final int NOT_DELETED = 0;

    /**
     * 用户默认code
     */
    public static final String DEFAULT_USER_CODE = "U_M";

    public static final String CUSTOMER_DEFAULT_USER_CODE = "U_C";

    /**
     * 启用状态:0-禁用,1-启用
     */
    public static final int ENABLE = 1;
    public static final int DISABLE = 0;
}
