package org.platform.vehicle.service;


import org.platform.vehicle.response.BaseResponse;

/**
 * @author cwz
 * @version 1.0
 * @date 2020/4/28 16:32
 */
public interface WxSendMessageService {


    BaseResponse sendMessage(String jsonData);
}
