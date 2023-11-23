package org.platform.vehicle.constant;

/**
 * @Author gejiawei
 * @Date 2023/8/28 15:50
 */
public class SysCustomerConstant {

    /**
     * 默认父级id
     */
    public static final int DEFAULT_PARENT_ID = 0;

    public static final int ADMINISTRATOR_CUSTOMER_ID = 1;

    /**
     * 客户类型:0-面心,1-客户,2-一级车队,3-二级车队
     */
    public static final int PARENT_TYPE = 0;
    public static final int CLIENT_TYPE = 1;
    public static final int FIRST_LEVEL_FLEET_TYPE = 2;
    public static final int SECOND_LEVEL_FLEET_TYPE = 3;

    /**
     * 客户状态:0-禁用,1-启用
     */
    public static final int DISABLE = 0;
    public static final int ENABLE = 1;

    /**
     * 删除状态:0-未删除,1-已删除
     */
    public static final int NOT_DELETED = 0;
    public static final int DELETED = 1;

    /**
     * 创建客户时,默认创建的一级车队和二级车队的名称
     */
    public static final int CREATE_FIRST_LEVEL_FLEET = 1;
    public static final int CREATE_SECOND_LEVEL_FLEET = 2;
}
