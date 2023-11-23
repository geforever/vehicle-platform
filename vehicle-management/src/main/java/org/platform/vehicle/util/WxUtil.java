package org.platform.vehicle.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.constant.NoticeConfigConstant;
import org.platform.vehicle.entity.SysNoticeConfig;
import org.platform.vehicle.param.PutMessageParam;
import org.platform.vehicle.param.WarningParam;
import org.platform.vehicle.service.WxSendMessageService;
import org.platform.vehicle.util.NoticeModule.ValueColorParam;
import org.platform.vehicle.util.NoticeModule.WarningNotice;
import org.platform.vehicle.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/9/1 15:09
 */

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WxUtil {

    private final WxSendMessageService sendMessageService;

    @Value("${wxpay.warning_template_id}")
    private String warningTemplateId;

    public void warningNoticePushMessage(String openId, SysNoticeConfig sysNoticeConfig,
            WarningParam warningParam) {
        // 判断公众号openid是否为空,如果为空直接返回
        if (ObjectUtil.isNotNull(openId)) {
            WarningNotice warningNotice = this.getWarningNotice(sysNoticeConfig, warningParam);
            // 所有赋值完成后给统一请求参数赋值
            PutMessageParam putMessageParam = new PutMessageParam(openId,
                    warningTemplateId, null, warningNotice);
            BaseResponse sendResponse = sendMessageService.sendMessage(
                    JSONObject.toJSONString(putMessageParam));
        }
    }

    private WarningNotice getWarningNotice(SysNoticeConfig sysNoticeConfig,
            WarningParam warningParam) {
        String content = this.getNoticeMsg(sysNoticeConfig, warningParam);
        WarningNotice warningNotice = new WarningNotice();
        warningNotice.setFirst(
                new ValueColorParam("【路安】您好，您有一个告警。", "#000000"));
        warningNotice.setKeyword1(new ValueColorParam(sysNoticeConfig.getName(), "#000000"));
        warningNotice.setKeyword2(new ValueColorParam(content, "#000000"));
        warningNotice.setRemark(new ValueColorParam("请您尽快处理。", "#666666"));
        return warningNotice;
    }

    private String getNoticeMsg(SysNoticeConfig sysNoticeConfig, WarningParam warningParam) {
        Integer type = sysNoticeConfig.getType();
        String model = sysNoticeConfig.getModel();
        String content = "";
        if (type == NoticeConfigConstant.WARNING) {
            content = model.replace("#车牌#", warningParam.getLicencePlate())
                    .replace("#轮位#", warningParam.getTyreLocation())
                    .replace("#通知名称#", sysNoticeConfig.getName())
                    .replace("#当前胎压#", warningParam.getWarningValue());
        } else if (type == NoticeConfigConstant.MAINTAIN) {
            content = model.replace("#车牌#", warningParam.getLicencePlate())
                    .replace("#订单号#", warningParam.getOrderNo())
                    .replace("#通知名称#", sysNoticeConfig.getName());
        }
        return content;
    }

}
