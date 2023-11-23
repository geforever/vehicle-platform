package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/25 16:19
 */
@Data
public class TireSiteResult {

    /**
     * 轮位名称
     */
    private String tireSiteName = "";

    /**
     * 轮位类型id
     */
    private Integer tireSiteType;

    /**
     * 轮位类型名称
     */
    private String tireSiteTypeName = "";

}
