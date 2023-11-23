package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/30 16:13
 */
@Data
public class FleetRemindSettingVo {

    /**
     * 提醒配置ID
     */
    private Integer noticeConfigId;

    /**
     * 提醒配置名称
     */
    private String noticeConfigName;

    /**
     * 提醒类型:1-短信,2-微信,3-短信和微信
     */
    private Integer remindType;

    /**
     * 接收人设置
     */
    private String receivedAccount;
}
