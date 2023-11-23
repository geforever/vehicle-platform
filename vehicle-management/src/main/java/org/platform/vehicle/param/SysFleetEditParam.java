package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/30 15:29
 */
@Data
public class SysFleetEditParam {

    /**
     * 主键id
     *
     * @required
     */
    private Integer id;

    /**
     * 创建类型:1-创建一级车队,2-创建二级车队
     *
     * @required
     */
    private Integer type;

    /**
     * 车队名称
     *
     * @required
     */
    private String name;

    /**
     * 所属客户id
     *
     * @required
     */
    private Integer clientId;

    /**
     * 上级车队Id
     */
    private Integer parentFleetId;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
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
     * 状态 0-停用 1-启用
     *
     * @required
     */
    private Integer status;

    /**
     * 车队提醒设置
     */
    List<FleetRemindSettingParam> remindSettingList;
}
