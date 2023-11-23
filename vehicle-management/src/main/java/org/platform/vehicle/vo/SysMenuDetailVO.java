package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/29 15:25
 */
@Data
public class SysMenuDetailVO {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 上级菜单ID
     */
    private Integer parentId;

    /**
     * 上级菜单名称
     */
    private String parentName;

    /**
     * 菜单名称
     */
    private String code;

    /**
     * 类型为页面时，代表前端路由地址，类型为按钮时，代表后端接口地址
     */
    private String url;

    /**
     * 权限类型，页面-1，按钮-2
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;
}
