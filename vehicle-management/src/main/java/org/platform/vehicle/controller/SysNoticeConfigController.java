package org.platform.vehicle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.platform.vehicle.service.SysNoticeConfigService;
import org.platform.vehicle.vo.SysNoticeConfigVo;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统设置-通知配置
 *
 * @Author gejiawei
 * @Date 2023/8/30 14:41
 */

@Slf4j
@RestController
@RequestMapping("/sysNoticeConfig")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysNoticeConfigController {

    private final SysNoticeConfigService sysNoticeConfigService;

    /**
     * 系统设置-通知配置-获取所有通知配置
     */
    @RequestMapping("/getAll")
    public BaseResponse<List<SysNoticeConfigVo>> getAll() {
        log.info("系统设置-通知配置-获取所有通知配置, url:/sysNoticeConfig/getAll");
        return sysNoticeConfigService.getAll();
    }

    /**
     * 发送微信消息(测试)
     */
    @GetMapping("/sendWarningMessage")
    public void sendWarningMessage() {
        sysNoticeConfigService.sendWarningMessage();
    }
}
