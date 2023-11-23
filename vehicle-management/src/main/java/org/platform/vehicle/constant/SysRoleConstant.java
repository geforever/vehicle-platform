package org.platform.vehicle.constant;

/**
 * @Author gejiawei
 * @Date 2023/8/25 10:17
 */
public class SysRoleConstant {

    /**
     * 角色id,1-默认角色,2-司机小程序,3-管理小程序
     */
    public static final int DEFAULT_ADMIN_ROLE_ID = 1;

    public static final int DRIVER_DEFAULT_ROLE_ID = 2;

    public static final int MANAGEMENT_DEFAULT_ROLE_ID = 3;

    /**
     * 角色等级,1-默认角色,2-可维护角色
     */
    public static final int DEFAULT_LEVEL = 1;
    public static final int CAN_MODIFY_LEVEL = 2;


    /**
     * 是否删除：0-未删除，1-已删除
     */
    public static final int IS_DELETE = 1;
    public static final int NOT_DELETE = 0;

    /**
     * 启用状态:0-禁用,1-启用
     */
    public static final int ENABLE = 1;
    public static final int DISABLE = 0;

}
