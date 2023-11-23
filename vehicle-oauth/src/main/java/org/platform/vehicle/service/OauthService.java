package org.platform.vehicle.service;

import org.platform.vehicle.param.LoginParam;
import org.platform.vehicle.param.ResetPasswordParam;
import org.platform.vehicle.response.BaseResponse;

/**
 * @Author gejiawei
 * @Date 2023/8/23 11:23
 */
public interface OauthService {

    /**
     * 登录
     *
     * @param loginParam
     * @return
     */
    BaseResponse login(LoginParam loginParam);

    /**
     * 获取验证码
     *
     * @return
     */
    BaseResponse getVerifyCode();

    /**
     * 发送验证码
     *
     * @return
     */
    BaseResponse sendLoginSmsCode(String phone);

    /**
     * 开发环境-登录
     *
     * @param loginParam
     * @return
     */
    BaseResponse devLogin(LoginParam loginParam);

    /**
     * 登出
     *
     * @return
     */
    BaseResponse logout();

    /**
     * 修改密码
     *
     * @param param
     * @return
     */
    BaseResponse resetPassword(ResetPasswordParam param);

    /**
     * 修改密码-发送验证码
     *
     * @return
     */
    BaseResponse sendResetPasswordSmsCode(String phone);
}
