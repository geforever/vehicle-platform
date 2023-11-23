package org.platform.vehicle.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author zhangyuekun
 * @date 2020/11/4 10:48
 */
@Data
@Root(name = "xml", strict = false)
public class WxPayToBankResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    @Element(name = "result_code")
    private String resultCode;

    /**
     * 以下字段在return_code为SUCCESS的时候有返回.
     */
    @Element(name = "err_code", required = false)
    private String errCode;

    @Element(name = "err_code_des", required = false)
    private String errCodeDes;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "partner_trade_no", required = false)
    private String partnerTradeNo;

    @Element(name = "amount", required = false)
    private String amount;

    @Element(name = "nonce_str", required = false)
    private String nonceStr;

    @Element(name = "sign", required = false)
    private String sign;

    /**
     * 以下字段在return_code 和result_code都为SUCCESS的时候有返回.
     */
    @Element(name = "payment_no", required = false)
    private String paymentNo;

    @Element(name = "cmms_amt", required = false)
    private String cmmsAmt;

}
