package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/8/28 16:00
 */
@Getter
@Setter
public class ClientConditionQuery extends PageParam {

    /**
     * 客户名称
     */
    private String name;
}
