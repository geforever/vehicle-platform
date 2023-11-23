package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/25 16:13
 */
@Data
public class SysRoleEditParam {

    /**
     * 角色主键ID
     *
     * @required
     */
    private Integer id;

    /**
     * 角色名称
     *
     * @required
     */
    private String name;

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

    /**
     * 状态：1-启用，0-禁用
     *
     * @required
     */
    private Integer status;

}
