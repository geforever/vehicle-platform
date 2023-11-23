package org.platform.vehicle.constant;

/**
 * @Author gejiawei
 * @Date 2023/8/31 16:25
 */
public enum OperateModuleEnum {
    /**
     * 系统设置-客户档案
     */
    SYSTEM_USER_ADD("系统设置-用户设置", "新增账号"),
    SYSTEM_USER_EDIT("系统设置-用户设置", "编辑账号"),
    SYSTEM_USER_DELETE("系统设置-用户设置", "用户列表"),
    SYSTEM_USER_RESET_PASSWORD("系统设置-用户设置", "重置密码"),
    /**
     * 系统设置-权限设置
     */
    SYSTEM_MENU_ADD("系统设置-权限设置", "新增菜单"),
    SYSTEM_MENU_EDIT("系统设置-权限设置", "编辑菜单"),
    SYSTEM_MENU_DELETE("系统设置-权限设置", "菜单列表"),
    /**
     * 系统设置-角色设置
     */
    SYSTEM_ROLE_ADD("系统设置-角色设置", "新增角色"),
    SYSTEM_ROLE_EDIT("系统设置-角色设置", "编辑角色"),
    SYSTEM_ROLE_DELETE("系统设置-角色设置", "角色列表"),
    /**
     * 档案管理-客户档案
     */
    ARCHIVE_CLIENT_ADD("档案管理-客户档案", "新增客户"),
    ARCHIVE_CLIENT_EDIT("档案管理-客户档案", "编辑客户"),
    ARCHIVE_CLIENT_DELETE("档案管理-客户档案", "客户列表"),
    /**
     * 档案管理-车队档案
     */
    ARCHIVE_FLEET_ADD("档案管理-车队档案", "新增车队"),
    ARCHIVE_FLEET_EDIT("档案管理-车队档案", "编辑车队"),
    ARCHIVE_FLEET_DELETE("档案管理-车队档案", "车队列表"),

    /**
     * 档案管理-司机档案
     */
    ARCHIVE_DRIVER_ADD("档案管理-司机档案", "新增司机"),
    ARCHIVE_DRIVER_EDIT("档案管理-司机档案", "编辑司机"),
    ARCHIVE_DRIVER_DELETE("档案管理-司机档案", "司机列表"),
    /**
     * 档案管理-车辆品牌
     */
    ARCHIVE_VEHICLE_BRAND_ADD("档案管理-车辆品牌", "新增车辆品牌"),
    ARCHIVE_VEHICLE_BRAND_EDIT("档案管理-车辆品牌", "编辑车辆品牌"),
    ARCHIVE_VEHICLE_BRAND_DELETE("档案管理-车辆品牌", "车辆品牌列表"),
    /**
     * 档案管理-车辆型号
     */
    ARCHIVE_VEHICLE_SPEC_ADD("档案管理-车辆型号", "新增车辆型号"),
    ARCHIVE_VEHICLE_SPEC_EDIT("档案管理-车辆型号", "编辑车辆型号"),
    ARCHIVE_VEHICLE_SPEC_DELETE("档案管理-车辆型号", "车辆型号列表"),
    /**
     * 档案管理-车辆档案
     */
    ARCHIVE_VEHICLE_ADD("档案管理-车辆档案", "新增车辆"),
    ARCHIVE_VEHICLE_EDIT("档案管理-车辆档案", "编辑车辆"),
    ARCHIVE_VEHICLE_DELETE("档案管理-车辆档案", "车辆列表"),
    /**
     * 档案管理-轮胎品牌
     */
    ARCHIVE_TIRE_BRAND_ADD("档案管理-轮胎品牌", "新增轮胎品牌"),
    ARCHIVE_TIRE_BRAND_EDIT("档案管理-轮胎品牌", "编辑轮胎品牌"),
    ARCHIVE_TIRE_BRAND_DELETE("档案管理-轮胎品牌", "轮胎品牌列表"),
    /**
     * 档案管理-轮胎型号
     */
    ARCHIVE_TIRE_SPEC_ADD("档案管理-轮胎型号", "新增轮胎型号"),
    ARCHIVE_TIRE_SPEC_EDIT("档案管理-轮胎型号", "编辑轮胎型号"),
    ARCHIVE_TIRE_SPEC_DELETE("档案管理-轮胎型号", "轮胎型号列表"),

    /**
     * 资产管理-仓库管理
     */
    ASSET_WAREHOUSE_ADD("资产管理-仓库管理", "新增仓库"),
    ASSET_WAREHOUSE_EDIT("资产管理-仓库管理", "编辑仓库"),
    ASSET_WAREHOUSE_DELETE("资产管理-仓库管理", "仓库列表"),

    /**
     * 资产管理-轮胎列表
     */
    ASSET_TIRE_STOCK_IN("资产管理-轮胎列表", "入库"),
    ASSET_TIRE_STOCK_OUT("资产管理-轮胎列表", "出库"),
    ASSET_TIRE_EDIT("资产管理-轮胎列表", "编辑"),
    ASSET_TIRE_DELETE("资产管理-轮胎列表", "删除"),
    ;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 按钮名称
     */
    private String operation;

    public String getModule() {
        return this.module;
    }

    public String getOperation() {
        return this.operation;
    }

    /**
     * 构造方法
     *
     * @param module
     * @param operation
     */
    OperateModuleEnum(String module, String operation) {
        this.module = module;
        this.operation = operation;
    }

}
