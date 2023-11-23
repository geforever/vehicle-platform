package org.platform.vehicle.util;

import org.platform.vehicle.constants.SmsCodeConstant;
import org.platform.vehicle.utils.sms.strategy.SmsStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2022/1/19 2:33 下午
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginSmsStrategy implements SmsStrategy {
    private final MessageUtil messageUtil;
    private final SmsHelpUtil smsHelpUtil;

    @Override
    public String exec(String phone) {
        // 校验短信发送次数
        Integer count = smsHelpUtil.checkSmsLimit(phone,
                SmsCodeConstant.LOGIN_PHONE_SMS_CODE,
                SmsCodeConstant.LOGIN_USER_SEND_SMS_COUNT);
        String msg = "【路安车管平台】验证码：{}，您正在登录路安车辆维管系统账号，为避免账号泄露造成损失，请勿泄露验证码。";
        String smsCode = messageUtil.sendMessage(phone, msg, SmsCodeConstant.LOGIN_PHONE_SMS_CODE);
        smsHelpUtil.addCount(SmsCodeConstant.LOGIN_USER_SEND_SMS_COUNT, phone, smsCode, count);
        return smsCode;
    }

    @Override
    public boolean checkSmsCode(String phone, String smsCode, String keyPrefix) {
        return messageUtil.checkCode(phone, smsCode, SmsCodeConstant.LOGIN_PHONE_SMS_CODE);
    }
}
