package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/30 10:10
 */
@Data
public class FleetConditionQuery extends PageParam {

    /**
     * 车队名称
     */
    private String name;

    /**
     * 上级车队名称
     */
    private String parentName;

}
