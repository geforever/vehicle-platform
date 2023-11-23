package org.platform.vehicle.utils.phone.model;

import java.util.List;

/**
 * 未读上行
 * Created by
 * xiashuai
 * on 2017/10/25
 */
public class DeliverResp extends Resp {

    /**
     * status : 0
     * result : [{"phone":"18262276782","destid":"-6004182853835414462","content":"PHONERR","receivetime":"20171025111347"}]
     */

    private List<Deliver> result;

    public List<Deliver> getResult() {
        return result;
    }

    public void setResult(List<Deliver> result) {
        this.result = result;
    }

}
