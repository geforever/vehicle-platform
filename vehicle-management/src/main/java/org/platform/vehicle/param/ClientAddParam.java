package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/28 16:47
 */
@Data
public class ClientAddParam {

    /**
     * 客户名称
     *
     * @required
     */
    private String name;

    /**
     * 联系人姓名
     *
     * @required
     */
    private String contactName;

    /**
     * 联系人手机号
     *
     * @required
     */
    private String contactPhone;

    /**
     * 省份
     *
     * @required
     */
    private String province;

    /**
     * 省份id
     *
     * @required
     */
    private Integer provinceId;

    /**
     * 城市
     *
     * @required
     */
    private String city;

    /**
     * 城市ID
     *
     * @required
     */
    private Integer cityId;

    /**
     * 区县
     *
     * @required
     */
    private String county;

    /**
     * 区县ID
     *
     * @required
     */
    private Integer countyId;

    /**
     * 详细地址
     *
     * @required
     */
    private String address;

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
     *
     * @required
     */
    private String adminAccount;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 开票信息
     */
    private String billInfo;

    /**
     * 客户最大权限
     */
    private List<Integer> menuIdList;
}
