package org.platform.vehicle.utils.sms.strategy;

/**
 * @Author gejiawei
 * @Date 2022/1/19 2:31 下午
 */
public interface SmsStrategy {

    /**
     * 执行
     *
     * @param phone
     */
    String exec(String phone);

    /**
     * 校验短信验证码
     *
     * @param phone
     * @param smsCode
     * @param keyPrefix
     * @return
     */
    boolean checkSmsCode(String phone, String smsCode, String keyPrefix);
}
