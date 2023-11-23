package org.platform.vehicle.constant;

/**
 * @Author gejiawei
 * @Date 2023/9/15 10:33
 */
public class AssetTireConstant {

    public static final String BATCH_STOCK_IN_LOCK_KEY = "tire_batch_stock_in_";
    public static final String BATCH_INSTALL_LOCK_KEY = "tire_batch_install_";


    public static final int NOT_USED_TIRE_SITE = 0;
    public static final int NOT_USED_TIRE_SITE_TYPE = 0;

    /**
     * 轮位分类:1-导向轮,2-驱动轮,3-挂车轮,4-承重轮
     */
    public static final int TIRE_TYPE_GUIDE = 1;
    public static final int TIRE_TYPE_DRIVE = 2;
    public static final int TIRE_TYPE_TRAILER = 3;
    public static final int TIRE_TYPE_LOAD = 4;

    /**
     * 是否绑定传感器:0-未绑定,1-已绑定
     */
    public static final int NOT_BIND_SENSOR = 0;
    public static final int IS_BIND_SENSOR = 1;


    /**
     * 传感器绑定类型:1-捆绑式,2-背贴式,3-气门嘴式
     */
    public static final int SENSOR_BIND_TYPE_BUNDLE = 1;
    public static final int SENSOR_BIND_TYPE_BACK = 2;
    public static final int SENSOR_BIND_TYPE_VALVE = 3;

    /**
     * 轮胎状态:1-仓库待用,2-使用中,3-已变卖,4-已调拨,5-待用
     */
    public static final int TIRE_STATUS_WAREHOUSE = 1;
    public static final int TIRE_STATUS_USING = 2;
    public static final int TIRE_STATUS_SOLD = 3;
    public static final int TIRE_STATUS_TRANSFER = 4;
    public static final int TIRE_STATUS_WAITING = 5;

    /**
     * 库存记录类型:1-入库,2-出库
     */
    public static final int STOCK_RECORD_TYPE_IN = 1;
    public static final int STOCK_RECORD_TYPE_OUT = 2;

    /**
     * 入库类型:1:采集,2赔付,3:拆卸入库,4-调拨入库
     */
    public static final int STOCK_IN_TYPE_COLLECT = 1;
    public static final int STOCK_IN_TYPE_COMPENSATE = 2;
    public static final int STOCK_IN_TYPE_UNINSTALL = 3;
    public static final int STOCK_IN_TYPE_TRANSFER = 4;

    /**
     * 出库类型:1:安装出库,2-调拨出库,3-变卖出库
     */
    public static final int STOCK_OUT_TYPE_INSTALL = 1;
    public static final int STOCK_OUT_TYPE_TRANSFER = 2;
    public static final int STOCK_OUT_TYPE_SOLD = 3;

    /**
     * 删除状态: 0-未删除, 1-已删除
     */
    public static final int NOT_DELETE = 0;
    public static final int IS_DELETE = 1;

    /**
     * 设备:传感器,中继器,GPS
     */
    public static final String DEVICE_TYPE_SENSOR = "传感器";
    public static final String DEVICE_TYPE_RELAY = "中继器";
    public static final String DEVICE_TYPE_GPS = "GPS";

    /**
     * 绑定状态:1.车轮已绑定传感器, 2.车辆未设置轮位, 3.存在车轮未绑传感器, 4.存在轮位未装车轮;对应颜色:1-绿色, 2-灰色, 3-蓝色, 4-红色
     */
    public static final int BIND_TYPE_TIRE_BIND_SENSOR = 1;
    public static final int BIND_TYPE_VEHICLE_NOT_SET_TIRE = 2;
    public static final int BIND_TYPE_TIRE_NOT_BIND_SENSOR = 3;
    public static final int BIND_TYPE_TIRE_NOT_MOUNT = 4;

    /**
     * 轮胎装卸:1-安装,2-拆卸
     */
    public static final int TIRE_INSTALL = 1;
    public static final int TIRE_UNINSTALL = 2;

    /**
     * 传感器解除绑定:1-解除,0-不解除
     */
    public static final int UNBIND_SENSOR_YES = 1;
    public static final int UNBIND_SENSOR_NO = 0;

}
