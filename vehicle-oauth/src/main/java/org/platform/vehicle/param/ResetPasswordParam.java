package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/28 14:22
 */
@Data
public class ResetPasswordParam {

    /**
     * 手机号
     *
     * @required
     */
    private String phone;

    /**
     * 手机验证码
     *
     * @required
     */
    private String smsCode;

    /**
     * 新密码
     *
     * @required
     */
    private String newPassword;

    /**
     * 确认密码
     *
     * @required
     */
    private String confirmPassword;
}
