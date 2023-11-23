package org.platform.vehicle.constants;

/**
 * @Author gejiawei
 * @Date 2023/8/24 11:18
 */
public class LoginConstant {

    public static final String LOGIN_REDIS_PREFIX = "login_user_id_";

    /**
     * 登录验证方式:1-账号密码登录,2-手机号验证码登录
     */
    public static final int LOGIN_TYPE_PASSWORD = 1;
    public static final int LOGIN_TYPE_SMS = 2;

    /**
     * 账号状态:0-禁用,1-启用
     */
    public static final int ACCOUNT_DISABLE = 0;
    public static final int ACCOUNT_ENABLE = 1;

    /**
     * 登录来源:1-管理后台,2-司机小程序,3-管理小程序
     */
    public static final int LOGIN_SOURCE_MANAGEMENT = 1;
    public static final int LOGIN_SOURCE_MINI_APP_DRIVER = 2;

    public static final int LOGIN_SOURCE_MINI_APP_MANAGEMENT = 1;

}
