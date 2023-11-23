package org.platform.vehicle.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @Auther hhlong
 * @Date 2019/8/19 13:25
 **/
@Data
@Root(name = "xml", strict = false)
public class WxPayForChangeResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    /**
     * 以下字段在return_code为SUCCESS的时候有返回.
     */
    @Element(name = "mch_appid", required = false)
    private String appid;

    @Element(name = "mchid", required = false)
    private String mchId;

    @Element(name = "device_info", required = false)
    private String deviceInfo;

    @Element(name = "nonce_str", required = false)
    private String nonceStr;

//    @Element(name = "sign", required = false)
//    private String sign;

    @Element(name = "result_code", required = false)
    private String resultCode;

    @Element(name = "err_code", required = false)
    private String errCode;

    @Element(name = "err_code_des", required = false)
    private String errCodeDes;

    /**
     * 以下字段在return_code 和result_code都为SUCCESS的时候有返回.
     */
    @Element(name = "partner_trade_no", required = false)
    private String partnerTradeNo;

    @Element(name = "payment_no", required = false)
    private String paymentNo;

    @Element(name = "payment_time", required = false)
    private String paymentTime;


}
