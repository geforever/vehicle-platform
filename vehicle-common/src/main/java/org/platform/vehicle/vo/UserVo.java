package org.platform.vehicle.vo;

import java.util.List;
import java.util.Set;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/23 10:06
 */
@Data
public class UserVo {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户类型:0-总公司, 1-客户, 2-一级车队, 3-二级车队
     */
    private Integer customerType;

    /**
     * 客户/车队包含下级
     */
    private List<Integer> customerIds;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * token
     */
    private String token;

    /**
     * 菜单树
     */
    private Set<MenuVo> menuTree;

    /**
     * 登录来源:1-管理后台,2-司机小程序,3-管理小程序
     */
    private Integer source;
}
