package org.platform.vehicle.service;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.JssdkParam;
import org.platform.vehicle.response.BaseResponse;
import javax.servlet.http.HttpServletRequest;

public interface WxService {

    BaseResponse mpAuthorize(String code);

    BaseResponse jssdk(JssdkParam param);

    String getMiniAppAccessToken();

    JSONObject authorizeWxPhone(String jsCode);

    BaseResponse getQrCode();

    String processRequest(HttpServletRequest request);

    boolean check(String timestamp, String nonce, String signature);

    String getGzhAccessToken();

    BaseResponse getAccountLoginShow();

}
