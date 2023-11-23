package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.platform.vehicle.entity.SysNoticeConfig;
import org.platform.vehicle.entity.WxUserInfo;
import org.platform.vehicle.mapper.SysNoticeConfigMapper;
import org.platform.vehicle.mapper.WxUserInfoMapper;
import org.platform.vehicle.param.WarningParam;
import org.platform.vehicle.service.SysNoticeConfigService;
import org.platform.vehicle.util.WxUtil;
import org.platform.vehicle.vo.SysNoticeConfigVo;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/8/30 14:42
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysNoticeConfigServiceImpl implements SysNoticeConfigService {

    private final SysNoticeConfigMapper sysNoticeConfigMapper;
    private final WxUtil wxUtil;
    private final WxUserInfoMapper wxUserInfoMapper;


    /**
     * 系统设置-通知配置-获取所有通知配置
     */
    @Override
    public BaseResponse getAll() {
        List<SysNoticeConfig> sysNoticeConfigs = sysNoticeConfigMapper.selectList(
                new LambdaQueryWrapper<>());
        List<SysNoticeConfigVo> sysNoticeConfigVoList = new ArrayList<>();
        for (SysNoticeConfig sysNoticeConfig : sysNoticeConfigs) {
            SysNoticeConfigVo sysNoticeConfigVo = new SysNoticeConfigVo();
            sysNoticeConfigVo.setId(sysNoticeConfig.getId());
            sysNoticeConfigVo.setName(sysNoticeConfig.getName());
            sysNoticeConfigVo.setModel(sysNoticeConfig.getModel());
            sysNoticeConfigVo.setType(sysNoticeConfig.getType());
            sysNoticeConfigVo.setLevel(sysNoticeConfig.getLevel());
            sysNoticeConfigVo.setDescription(sysNoticeConfig.getDescription());
            sysNoticeConfigVo.setCreatePerson(sysNoticeConfig.getCreatePerson());
            sysNoticeConfigVo.setUpdatePerson(sysNoticeConfig.getUpdatePerson());
            sysNoticeConfigVo.setCreateTime(sysNoticeConfig.getCreateTime());
            sysNoticeConfigVo.setUpdateTime(sysNoticeConfig.getUpdateTime());
            sysNoticeConfigVoList.add(sysNoticeConfigVo);
        }
        return BaseResponse.ok(sysNoticeConfigVoList);
    }

    /**
     * 发送微信消息(测试)
     */
    @Override
    public void sendWarningMessage() {
        UserVo user = UserContext.getUser();
        WxUserInfo wxUserInfo = wxUserInfoMapper.selectOne(
                Wrappers.<WxUserInfo>lambdaQuery().eq(WxUserInfo::getUserId,
                        user.getUserId()));
        if (wxUserInfo != null) {
            SysNoticeConfig sysNoticeConfig = sysNoticeConfigMapper.selectById(1);
            WarningParam warningParam = new WarningParam();
            warningParam.setLicencePlate("abcde");
            warningParam.setTyreLocation("主车3轴右外");
            warningParam.setWarningValue("10");
            wxUtil.warningNoticePushMessage(wxUserInfo.getOpenId(), sysNoticeConfig, warningParam);
        }
    }
}
