package org.platform.vehicle.utils.phone.model;

/**
 * 短信内容，打包数组
 * Created by
 * xiashuai
 * on 2017/10/25
 */
public class SMS {
    // 短信内容，【签名】内容；具体参考单内容发送
    private String content;
    // 内容对应的手机号,可多个手机号
    private String mobile;
    // msgId,不传则对应为空，平台会自动生成
    private Long msgId;
    // 平台拓展码
    private String extCode;

    public SMS() {
    }

    public SMS(String mobile, String content, String extCode, Long msgId) {
        this.content = content;
        this.mobile = mobile;
        this.msgId = msgId;
        this.extCode = extCode;
    }

    public SMS(String mobile, String content, String extCode) {
        this.content = content;
        this.mobile = mobile;
        this.extCode = extCode;
    }

    public SMS(String mobile, String content) {
        this.content = content;
        this.mobile = mobile;
    }

    public SMS(String mobile, String content, Long msgId) {
        this.content = content;
        this.mobile = mobile;
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(String extCode) {
        this.extCode = extCode;
    }

}
