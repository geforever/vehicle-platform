package org.platform.vehicle.utils.phone.model;

/**
 * 上行
 * Created by
 * momo
 * on 2018/02/07
 *
 * phone : 18262276782
 * extCode : -6004182853835414462
 * content : PHONERR
 * receivetime : 20171025111347
 */
public class Deliver extends Resp {

    private String phone;       //手机号
    private String extCode;     //通道的拓展码
    private String content;     //用户回的信息的内容
    private String receivetime; //接收到的用户回信息的时间

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(String extCode) {
        this.extCode = extCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime;
    }
}
