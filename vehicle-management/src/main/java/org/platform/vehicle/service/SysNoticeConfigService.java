package org.platform.vehicle.service;

import org.platform.vehicle.vo.SysNoticeConfigVo;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;

/**
 * @Author gejiawei
 * @Date 2023/8/30 14:42
 */
public interface SysNoticeConfigService {

    /**
     * 系统设置-通知配置-获取所有通知配置
     */
    BaseResponse<List<SysNoticeConfigVo>> getAll();

    /**
     * 发送微信消息(测试)
     */
    void sendWarningMessage();
}
