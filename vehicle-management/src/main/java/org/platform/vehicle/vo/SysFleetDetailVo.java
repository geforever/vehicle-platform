package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/30 16:12
 */
@Data
public class SysFleetDetailVo {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 车队名称
     */
    private String name;

    /**
     * 所属客户id
     */
    private Integer clientId;

    /**
     * 所属客户名称
     */
    private String clientName;

    /**
     * 上级车队Id(不传则创建一级车队)
     */
    private Integer parentFleetId;

    /**
     * 上级车队名称
     */
    private String parentFleetName;

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
     * 状态 0-停用 1-启用
     */
    private Integer status;

    /**
     * 车队提醒设置
     */
    List<FleetRemindSettingVo> fleetRemindSettingVoList;
}
