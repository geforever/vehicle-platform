package org.platform.vehicle.vo.amap;

import lombok.Data;

/**
 * 高德 地址元素列表
 *
 * @Author gejiawei
 * @Date 2023/10/11 16:54
 */
@Data
public class AddressComponentVo {

    /**
     * 坐标点所在省名称
     */
    private String province;

    /**
     * 坐标点所在城市名称
     */
    private String city;

    /**
     * 城市编码
     */
    private String citycode;

    /**
     * 坐标点所在区
     */
    private String district;

    /**
     * 区域编码
     */
    private String adcode;

    /**
     * 坐标点所在乡镇
     */
    private String township;

    /**
     * 坐标点所在社区
     */
    private String neighborhood;


}
