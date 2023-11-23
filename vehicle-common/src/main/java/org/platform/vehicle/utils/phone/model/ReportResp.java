package org.platform.vehicle.utils.phone.model;

import java.util.List;

/**
 * 未读反馈记录
 * Created by
 * xiashuai
 * on 2017/10/25
 */
public class ReportResp extends Resp {

    /**
     * status : 0
     * result : [{"phone":"18262276782","msgid":"-6004182853835414462","status":"PHONERR","donetime":"20171025111347"},{"phone":"18262276782","msgid":"-6003923918847073213","status":"PHONERR","donetime":"20171025112829"},{"phone":"18262276782","msgid":"-6003895331544751036","status":"PHONERR","donetime":"20171025113004"}]
     */

    private List<Report> result;

    public List<Report> getResult() {
        return result;
    }

    public void setResult(List<Report> result) {
        this.result = result;
    }

}
