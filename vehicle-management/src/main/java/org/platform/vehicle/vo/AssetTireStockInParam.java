package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/15 09:01
 */
@Data
public class AssetTireStockInParam {

    /**
     * 入库类型:1:采集,2赔付,3:拆卸入库,4-调拨入库
     *
     * @required
     */
    private Integer stockInType;

    /**
     * 轮胎编号
     *
     * @required
     */
    private String tireCode;

    /**
     * 供给方
     */
    private String target;

    /**
     * 车队id
     *
     * @required
     */
    private Integer fleetId;

    /**
     * 仓库Id
     */
    private Integer warehouseId;

    /**
     * 新旧程度:30%,50%,70%,100%
     */
    private String degree;

    /**
     * 当前里程
     */
    private Integer mileage;

    /**
     * 轮胎品牌id
     *
     * @required
     */
    private Integer tireBrandId;

    /**
     * 轮胎规格id
     */
    private Integer tireSpecId;

    /**
     * 传感器ID(入库不传)
     */
    private String sensorId;

}
