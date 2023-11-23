package org.platform.vehicle.entity;

import org.platform.vehicle.response.WeiXinRefundResponse;
import org.platform.vehicle.response.WxOrderQueryResponse;
import org.platform.vehicle.response.WxPayAsyncResponse;
import org.platform.vehicle.response.WxPayForChangeResponse;
import org.platform.vehicle.response.WxPaySandboxKeyResponse;
import org.platform.vehicle.response.WxPayToBankResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Auther hhlong
 * @Date 2019/7/10 16:30
 **/
public interface WxPayApi {

    /**
     * 统一下单
     *
     * @param body
     * @return
     */
    @POST("/pay/unifiedorder")
    Call<WxPayAsyncResponse> unifiedorder(@Body RequestBody body);

    /**
     * 申请退款
     *
     * @param body
     * @return
     */
    @POST("/secapi/pay/refund")
    Call<WeiXinRefundResponse> refund(@Body RequestBody body);

    /**
     * 申请沙箱密钥
     *
     * @param body
     * @return
     */
    @POST("/sandboxnew/pay/getsignkey")
    Call<WxPaySandboxKeyResponse> getsignkey(@Body RequestBody body);

    /**
     * 订单查询
     *
     * @param body
     * @return
     */
    @POST("/pay/orderquery")
    Call<WxOrderQueryResponse> orderquery(@Body RequestBody body);
//
//    @POST("/pay/downloadbill")
//    Call<ResponseBody> downloadBill(@Body RequestBody body);


    /**
     * 企业付款到银行卡
     */
    @POST("/mmpaysptrans/pay_bank")
    Call<WxPayToBankResponse> payToBank(@Body RequestBody body);

    @GET("/cgi-bin/token")
    Call<String> getAccessToken(@Query(value = "appid") String appid,
            @Query(value = "grant_type") String grantType,
            @Query(value = "secret") String secret);

    /**
     * 获取微信服务器IP地址
     */
    @GET("/cgi-bin/get_api_domain_ip")
    Call<String> getApiDomainIp(@Query(value = "access_token") String accessToken);


    //    ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
    @GET("/cgi-bin/ticket/getticket")
    Call<String> getTicket(@Query(value = "access_token") String accessToken,
            @Query(value = "type") String type);

    @POST("cgi-bin/message/template/send")
    Call<String> putTemplateId(@Query(value = "access_token") String access_token,
            @Body String data);

    /**
     * 创建二维码
     *
     * @param access_token
     * @param data
     * @return
     */
    @POST("cgi-bin/qrcode/create")
    Call<String> qrcodeCreate(@Query(value = "access_token") String access_token,
            @Body String data);

    /**
     * 企业付款到零钱
     *
     * @param body
     * @return
     */
    @POST("/mmpaymkttransfers/promotion/transfers")
    Call<WxPayForChangeResponse> payForChange(@Body RequestBody body);

    @GET("/cgi-bin/user/info")
    Call<String> getUserInfo(@Query(value = "access_token") String accessToken,
            @Query(value = "openid") String openid);


}
