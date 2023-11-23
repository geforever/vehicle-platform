package org.platform.vehicle.utils.phone.model;

/**
 * 未读反馈记录
 * Created by
 * xiashuai
 * on 2017/10/25
 */
public class Report {

    /**
     * phone : 18262276782
     * msgid : -6004182853835414462
     * status : PHONERR
     * donetime : 20171025111347
     */

    private String phone;       // 短信手机号
    private String msgid;       // 短信msgId，与短信submitResp中的MsgId 一致
    private String status;      // 状态码 DELIVRD 是发送成功
    private String donetime;    // 手机收到短信时间

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDonetime() {
        return donetime;
    }

    public void setDonetime(String donetime) {
        this.donetime = donetime;
    }
}
