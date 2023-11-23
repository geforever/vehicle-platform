package org.platform.vehicle.constant;

/**
 * @Author gejiawei
 * @Date 2023/8/24 16:08
 */
public class SysMenuConstant {

    /**
     * 默认父菜单ID:0-查询包含小程序菜单,1-只查询后台管理菜单
     */
    public static final int DEFAULT_PARENT_ID = 0;

    public static final int VEHICLE_MANAGEMENT_PARENT_ID = 1;


    /**
     * 菜单类型:1-页面,2-按钮
     */
    public static final int TYPE_PAGE = 1;
    public static final int TYPE_BUTTON = 2;

    /**
     * 默认菜单排序
     */
    public static final int DEFAULT_SORT = 1;

    /**
     * 菜单状态:1-启用,0-禁用
     */
    public static final int STATUS_ENABLE = 1;
    public static final int STATUS_DISABLE = 0;

    /**
     * 菜单状态:1-已删除,0-未删除
     */
    public static final int IS_DELETE = 1;
    public static final int NOT_DELETE = 0;
}
