package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/25 10:12
 */
@Data
public class SysRoleAddParam {

    /**
     * 角色归属(客户ID)
     *
     * @required
     */
    private Integer customerId;

    /**
     * 角色名称
     *
     * @required
     */
    private String name;

    /**
     * 角色等级,1-默认角色,2-可维护角色
     */
    private Integer level = 2;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 权限ID列表
     *
     * @required
     */
    private List<Integer> menuIdList;
}
