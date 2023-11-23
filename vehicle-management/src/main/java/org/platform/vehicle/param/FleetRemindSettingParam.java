package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/30 14:20
 */
@Data
public class FleetRemindSettingParam {

    /**
     * 提醒配置ID
     *
     * @required
     */
    private Integer noticeConfigId;

    /**
     * 提醒类型:1-短信,2-微信,3-短信和微信
     *
     * @required
     */
    private Integer remindType;

    /**
     * 接收人设置(手机号, 微信openId, 多个用逗号隔开)
     *
     * @required
     */
    private String receivedAccount;
}
