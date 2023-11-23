package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/13 11:28
 */
@Data
public class WarehouseSettingEditParam {

    private Integer id;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 车队ID
     */
    private Integer fleetId;

    /**
     * 库存最小值
     */
    private Integer stockMin;

    /**
     * 库存最大值
     */
    private Integer stockMax;

}
