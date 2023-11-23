package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/29 11:22
 */
@Data
public class ClientDetailVo {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactPhone;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份id
     */
    private Integer provinceId;

    /**
     * 城市
     */
    private String city;

    /**
     * 城市ID
     */
    private Integer cityId;

    /**
     * 区县
     */
    private String county;

    /**
     * 区县ID
     */
    private Integer countyId;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 管理员ID
     */
    private Integer adminUserId;

    /**
     * 有效期(YYYY-MM-DD)
     */
    private String validDate;

    /**
     * 最大车辆数
     */
    private Integer maxVehicle;

    /**
     * 管理员账号
     */
    private String adminAccount;

    /**
     * 开票信息
     */
    private String billInfo;

    /**
     * 客户权限
     */
    private List<Integer> menuIdList;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;

}
