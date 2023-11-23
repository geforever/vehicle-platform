package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/4 11:47
 */
@Data
public class WarningParam {

    /**
     * 车牌号
     */
    private String licencePlate;

    /**
     * 轮位
     */
    private String tyreLocation;

    /**
     * 告警当前数值
     */
    private String warningValue;

    /**
     * 订单号
     */
    private String orderNo;
}
