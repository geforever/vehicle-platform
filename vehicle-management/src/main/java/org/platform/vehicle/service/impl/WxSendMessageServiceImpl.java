package org.platform.vehicle.service.impl;

import cn.hutool.log.StaticLog;
import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.entity.WxPayApi;
import org.platform.vehicle.service.WxSendMessageService;
import org.platform.vehicle.service.WxService;
import org.platform.vehicle.util.ToStringConverterFactory;
import org.platform.vehicle.enums.SendMessageEnum;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.response.SendMessageResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author cwz
 * @version 1.0
 * @date 2020/4/28 16:32
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WxSendMessageServiceImpl implements WxSendMessageService {

    private final WxService wxService;

    @Override
    public BaseResponse sendMessage(String jsonData) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(new ToStringConverterFactory())
                    .baseUrl("https://api.weixin.qq.com/")
                    .build();
            //验证access_token有效性
            String accessToken = wxService.getGzhAccessToken();
            Call<String> templateId = retrofit.create(WxPayApi.class)
                    .putTemplateId(accessToken, jsonData);
            Response<String> sendMessage = null;
            try {
                sendMessage = templateId.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SendMessageResponse sendMessageResponse = JSONObject.parseObject(sendMessage.body(),
                    SendMessageResponse.class);
            StaticLog.info(JSONObject.toJSONString(sendMessageResponse));
            if (!"ok".equals(sendMessageResponse.getErrmsg())) {
                //匹配信息
                SendMessageEnum matching = SendMessageEnum.matching(
                        sendMessageResponse.getErrcode());
                return new BaseResponse(matching.getCode(), matching.getMessage());
            }
        } catch (Exception e) {
            StaticLog.info("公众号消息推送失败" + e);
        }
        return BaseResponse.ok();
    }
}
