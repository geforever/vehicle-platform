package org.platform.vehicle.param;

import lombok.Data;

/**
 * @author cwz
 * @version 1.0
 * @date 2020/4/28 15:06
 */
@Data
public class PutMessageParam {

    private String touser;
    private String template_id;
    private String url;
    private Object data;

    public PutMessageParam() {
    }

    public PutMessageParam(String touser, String template_id, String url, Object data) {
        this.touser = touser;
        this.template_id = template_id;
        this.url = url;
        this.data = data;
    }
}
