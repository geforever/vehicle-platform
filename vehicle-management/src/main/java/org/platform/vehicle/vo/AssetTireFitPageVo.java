package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/18 16:00
 */
@Data
public class AssetTireFitPageVo {

    /**
     * 主车车牌
     */
    private String licensePlate;

    /**
     * 挂车车牌
     */
    private String guaLicensePlate;

    /**
     * 车辆型号ID
     */
    private Integer vehicleSpecId;

    /**
     * 颜色:1-绿色, 2-灰色, 3-蓝色, 4-红色
     */
    private Integer color;
}
