package org.platform.vehicle.constants;

/**
 * @Author gejiawei
 * @Date 2021/11/15 9:30 上午
 */
public class SmsCodeConstant {

    /**
     * 验证码redis key prefix
     */
    public static final String LOGIN_PHONE_SMS_CODE = "login_phone_sms_code_";
    public static final String RESET_PASSWORD_PHONE_SMS_CODE = "reset_password_phone_sms_code_";

    /**
     * 验证码登录次数 redis key prefix
     */
    public static final String LOGIN_USER_SEND_SMS_COUNT = "login_user_send_sms_count_";
    public static final String RESET_PASSWORD_USER_SEND_SMS_COUNT = "reset_password_user_send_sms_count_";

    /**
     * 手机登录验证
     */
    public static final String LOGIN_PHONE_CODE = "login_phone_code_";

}
