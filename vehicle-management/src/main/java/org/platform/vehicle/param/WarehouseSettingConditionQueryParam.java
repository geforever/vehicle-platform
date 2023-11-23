package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/9/13 10:31
 */
@Setter
@Getter
public class WarehouseSettingConditionQueryParam extends PageParam {

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库类型
     */
    private String fleetName;
}
