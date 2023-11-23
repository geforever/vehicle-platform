package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/23 11:36
 */
@Data
public class LoginParam {

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录方式:1-账号密码登录,2-手机号验证码登录
     *
     * @required
     */
    private Integer type;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 登录来源:1-管理后台,2-司机小程序,3-管理小程序
     *
     * @required
     */
    private Integer source;
}
