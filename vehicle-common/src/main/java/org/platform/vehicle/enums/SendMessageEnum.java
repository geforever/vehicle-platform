package org.platform.vehicle.enums;

import lombok.Getter;

/**
 * @author cwz
 * @version 1.0
 * @date 2020/4/28 17:56
 */
@Getter
public enum SendMessageEnum {
    USER_NOT_KEEP("43004", "当前暂未关注公众号，请关注公众号【****】"),
    NULL("999999", "公众号接收消息失败");

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;

    SendMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static SendMessageEnum matching(String code) {
        for (SendMessageEnum sendMessageEnum : SendMessageEnum.values()) {
            if (code.equals(sendMessageEnum.getCode())) {
                return sendMessageEnum;
            }
        }
        return NULL;
    }
}
