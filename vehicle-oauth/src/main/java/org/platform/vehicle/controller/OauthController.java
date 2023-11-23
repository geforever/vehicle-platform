package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.LoginParam;
import org.platform.vehicle.param.ResetPasswordParam;
import org.platform.vehicle.service.OauthService;
import org.platform.vehicle.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 *
 * @Author gejiawei
 * @Date 2023/8/23 11:23
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthController {

    private final OauthService oauthService;

    /**
     * 登录-发送验证码
     *
     * @return
     */
    @GetMapping("/login/sendSmsCode")
    public BaseResponse sendSmsCode(@RequestParam("phone") String phone) {
        log.info("发送验证码, url:/sendSmsCode, param:{}", phone);
        return oauthService.sendLoginSmsCode(phone);
    }

    /**
     * 登录
     *
     * @param loginParam
     * @return
     */
    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginParam loginParam) {
        log.info("登录, url:/login, param:{}", JSONObject.toJSON(loginParam));
        return oauthService.login(loginParam);
    }


    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse logout() {
        log.info("登出, url:/logout");
        return oauthService.logout();
    }

    /**
     * 修改密码
     *
     * @param param
     * @return
     */
    @PostMapping("/password/reset")
    public BaseResponse resetPassword(@RequestBody ResetPasswordParam param) {
        log.info("重置密码, url:/password/reset, param:{}", JSONObject.toJSON(param));
        return oauthService.resetPassword(param);
    }

    /**
     * 修改密码-发送验证码
     *
     * @return
     */
    @GetMapping("/password/reset/sendSmsCode")
    public BaseResponse sendResetPasswordSmsCode(@RequestParam("phone") String phone) {
        log.info("发送验证码, url:/sendSmsCode, param:{}", phone);
        return oauthService.sendResetPasswordSmsCode(phone);
    }

}
