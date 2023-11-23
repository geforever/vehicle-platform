package org.platform.vehicle.utils.sms;


import org.platform.vehicle.utils.sms.strategy.SmsStrategy;

/**
 * @Author gejiawei
 * @Date 2022/1/19 2:31 下午
 */
public class SmsContext {

    private SmsStrategy strategy;

    public SmsContext(SmsStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 执行策略
     */
    public String exec(String phone) {
        // 执行
        return strategy.exec(phone);
    }

    /**
     * 校验短信验证码
     *
     * @param phone
     * @param smsCode
     * @param keyPrefix
     * @return
     */
    public boolean checkSmsCode(String phone, String smsCode, String keyPrefix) {
        return strategy.checkSmsCode(phone, smsCode, keyPrefix);
    }
}
