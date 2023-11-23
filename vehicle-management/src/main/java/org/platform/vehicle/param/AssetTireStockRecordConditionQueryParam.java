package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/9/15 17:22
 */
@Getter
@Setter
public class AssetTireStockRecordConditionQueryParam extends PageParam {

    /**
     * 轮胎编码
     */
    private String tireCode;

    /**
     * 所属车队
     */
    private String fleetName;

    /**
     * 库存记录类型:1-入库,2-出库
     *
     * @required
     */
    private Integer type;

    /**
     * 该记录出入库类型类型: 入库类型:1:采集,2赔付,3:拆卸入库,4-调拨入库 出库类型:1:安装出库,2-调拨出库,3-变卖出库
     */
    private Integer stockType;
}
