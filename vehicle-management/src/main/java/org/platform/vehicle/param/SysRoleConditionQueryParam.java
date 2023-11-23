package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/8/25 09:13
 */
@Getter
@Setter
public class SysRoleConditionQueryParam extends PageParam {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 客户ID
     */
    private Integer customerId;
}
