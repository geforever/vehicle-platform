package org.platform.vehicle.utils.phone.model;

/**
 * 反馈原型
 * Created by
 * momo
 * on 2018/02/07
 */
public class Resp {

    // http 状态码
    private int status;
    // 提示信息 默认null
    private String msg;

    public Resp() {
    }

    public Resp(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    // 是否返回正常
    public boolean success(){
        return status == 0;
    }
}
