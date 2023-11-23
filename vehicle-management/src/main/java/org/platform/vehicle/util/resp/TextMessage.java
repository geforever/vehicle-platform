package org.platform.vehicle.util.resp;

import lombok.Data;

/**
 * 请求消息之文本消息
 *
 * @author wangchengcheng
 * @date 2022/1/6 - 9:22
 */
@Data
public class TextMessage extends BaseMessage {

    // 回复的消息内容
    private String Content;
}
