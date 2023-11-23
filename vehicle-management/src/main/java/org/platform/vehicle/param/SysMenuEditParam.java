package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/24 16:52
 */
@Data
public class SysMenuEditParam {

    /**
     * 主键ID
     *
     * @required
     */
    private Integer id;

    /**
     * 上级菜单ID
     *
     * @required
     */
    private Integer parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 菜单类型:1-菜单, 2-按钮
     *
     * @required
     */
    private Integer type = 1;

    /**
     * 菜单序号
     */
    private Integer sort;

    /**
     * 菜单路径
     */
    private String url;

    /**
     * 菜单状态:0-停用, 1-启用
     *
     * @required
     */
    private Integer status;

}
