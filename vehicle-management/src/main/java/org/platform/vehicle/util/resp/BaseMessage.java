package org.platform.vehicle.util.resp;

import lombok.Data;

/**
 * 消息基类（公众帐号 -> 普通用户）
 *
 * @author wangchengcheng
 * @date 2022/1/6 - 9:41
 */
@Data
public class BaseMessage {

    // 接收方帐号（收到的OpenID）
    private String ToUserName;
    // 开发者微信号
    private String FromUserName;
    // 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型
    private String MsgType;
}
