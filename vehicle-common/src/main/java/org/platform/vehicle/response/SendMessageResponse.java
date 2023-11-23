package org.platform.vehicle.response;

import lombok.Data;

/**
 * @author cwz
 * @version 1.0
 * @date 2020/4/28 16:40
 */
@Data
public class SendMessageResponse {

    private String errcode;
    private String errmsg;
    private String msgid;
}
