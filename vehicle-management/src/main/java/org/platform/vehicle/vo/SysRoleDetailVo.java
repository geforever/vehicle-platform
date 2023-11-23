package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/29 15:23
 */
@Data
public class SysRoleDetailVo {

    private Integer id;

    /**
     * 客户ID
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色名称
     */
    private String nickname;

    /**
     * 角色等级,1-默认角色,2-可维护角色
     */
    private Integer level;

    /**
     * 描述
     */
    private String description;

    /**
     * 禁用状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 前端缓存
     */
    private String temp;

    /**
     * 菜单ID集合
     */
    private List<Integer> menuIdList;
}
