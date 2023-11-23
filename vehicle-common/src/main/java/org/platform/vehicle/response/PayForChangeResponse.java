package org.platform.vehicle.response;

import lombok.Data;

/**
 * @Auther hhlong
 * @Date 2019/8/19 11:41
 **/
@Data
public class PayForChangeResponse {
    private String partnerTradeNo;//商户转账订单号

    private String payMentNO;//微信订单号

    private String payMentTime;//微信支付成功时间

}
