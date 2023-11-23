package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/15 16:21
 */
@Data
public class AssetTireStockOutParam {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 出库类型:1:安装出库,2-调拨出库,3-变卖出库
     *
     * @required
     */
    private Integer stockOutType;

    /**
     * 轮胎编号
     *
     * @required
     */
    private String tireCode;

    /**
     * 轮胎号列表(调拨,变卖)
     */
    private List<String> tireCodeList;

    /**
     * 接收方(领用司机)
     */
    private String target;

    /**
     * 出库备注
     */
    private String remark;

    /**
     * 车牌(1:安装出库必传)
     */
    private String licensePlate;

    /**
     * 轮胎位置ID
     */
    private Integer tireSite;

    /**
     * 轮胎位置
     */
//    private String tireSiteName;

    /**
     * 轮位分类
     */
//    private Integer tireSiteType;

    /**
     * 轮位分类名称
     */
//    private String tireSiteTypeName;

    /**
     * 传感器ID
     *
     * @required
     */
    private String sensorId;

    /**
     * 仓库ID(调拨必传)
     */
    private Integer warehouseId;

}
