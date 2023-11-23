package org.platform.vehicle.param;

import org.platform.vehicle.constant.WarningTypeEnum;
import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/26 14:59
 */
@Data
public class SysNoticeWarningParam {

    /**
     * 消息类型主键ID
     */
    private String targetId;

    /**
     * 消息类型:1-告警,2-维保
     */
    private Integer type;

    /**
     * 主标题
     */
    private String mainTitle;

    /**
     * 副标题
     */
    private String secondTitle;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送人ID,1-系统
     */
    private Integer senderId;

    /**
     * 发送人
     */
    private String sender;

    /**
     * 车队ID
     */
    private Integer fleetId;

    /**
     * 告警类型ENUM
     */
    private List<WarningTypeEnum> warningTypeEnumList;


}
