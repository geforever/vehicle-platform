package org.platform.vehicle.response;

import lombok.Data;

/**
 * @author wangchengcheng
 * @date 2022/1/18 - 10:03
 */
@Data
public class IpListResponse {

    private String[] ip_list;
    private String errcode;
    private String errmsg;
}
