package org.platform.vehicle.vo.amap;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/11 16:09
 */
@Data
public class LiveVo {

    /**
     * 省份名称
     */
    private String province;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 区域编码
     */
    private String adcode;

    /**
     * 天气现象（汉字描述）
     */
    private String weather;

    /**
     * 实时气温，单位：摄氏度
     */
    private String temperature;

    /**
     * 风向描述
     */
    private String winddirection;

    /**
     * 风力级别，单位：级
     */
    private String windpower;

    /**
     * 空气湿度
     */
    private String humidity;

    /**
     * 数据发布的时间
     */
    private String reporttime;
}
