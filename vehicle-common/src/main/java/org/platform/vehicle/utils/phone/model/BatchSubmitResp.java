package org.platform.vehicle.utils.phone.model;

import java.util.List;

/**
 * 批量内容提交反馈
 * Created by
 * momo
 * on 2018/02/07
 */
public class BatchSubmitResp extends Resp {

    /**
     * status : 0
     * result : [{"msgid":"-5999107783039507381","status":0},{"msgid":"-5999107783039507380","status":0}]
     */

    private List<SubmitResp> result;

    public List<SubmitResp> getResult() {
        return result;
    }

    public void setResult(List<SubmitResp> result) {
        this.result = result;
    }

}
