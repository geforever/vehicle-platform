package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/15 11:18
 */
@Data
public class AssetTireEditParam {

    /**
     * 主键ID
     *
     * @required
     */
    private Integer id;

    /**
     * 轮胎编号
     *
     * @required
     */
    private String tireCode;

    /**
     * 轮胎品牌
     *
     * @required
     */
    private Integer tireBrandId;

    /**
     * 轮胎型号
     */
    private Integer tireSpecId;

    /**
     * 新旧程度(30%,50%,70%,100%)
     */
    private String degree;

    /**
     * 传感器ID
     */
    private String sensorId;

}
