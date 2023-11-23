package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.constant.SysNoticeConstant;
import org.platform.vehicle.constant.SysUserConstant;
import org.platform.vehicle.constant.WarningTypeEnum;
import org.platform.vehicle.entity.SysFleetNotice;
import org.platform.vehicle.entity.SysNotice;
import org.platform.vehicle.entity.SysUser;
import org.platform.vehicle.mapper.SysFleetNoticeMapper;
import org.platform.vehicle.mapper.SysNoticeMapper;
import org.platform.vehicle.mapper.SysUserMapper;
import org.platform.vehicle.param.NoticeBatchReadParam;
import org.platform.vehicle.param.SysNoticeConditionQueryParam;
import org.platform.vehicle.param.SysNoticeWarningParam;
import org.platform.vehicle.service.SysNoticeService;
import org.platform.vehicle.vo.NoticeSimpleVo;
import org.platform.vehicle.vo.SysNoticeDetailVo;
import org.platform.vehicle.vo.SysNoticeVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/9/6 11:10
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeMapper sysNoticeMapper;
    private final SysFleetNoticeMapper sysFleetNoticeMapper;
    private final SysUserMapper sysUserMapper;

    /**
     * 消息列表-获取当前用户未读消息数量
     *
     * @return
     */
    @Override
    public BaseResponse getNotReadCount() {
        // 当前登录人
        UserVo user = UserContext.getUser();
        Long count = sysNoticeMapper.selectCount(new LambdaQueryWrapper<SysNotice>()
                .eq(SysNotice::getReceiverId, user.getUserId())
                .eq(SysNotice::getIsRead, SysNoticeConstant.NOT_READ));
        return BaseResponse.ok(count);
    }

    /**
     * 消息列表-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse conditionQuery(SysNoticeConditionQueryParam param) {
        UserVo user = UserContext.getUser();
        Page<SysNotice> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<SysNotice> wrapper = this.getWrapper(
                param, user);
        Page<SysNotice> sysNoticePage = sysNoticeMapper.selectPage(page, wrapper);
        List<SysNotice> sysNoticeList = sysNoticePage.getRecords();
        List<SysNoticeVo> sysNoticeVoList = new ArrayList<>();
        for (SysNotice sysNotice : sysNoticeList) {
            SysNoticeVo sysNoticeVo = this.getSysNoticeVo(sysNotice);
            sysNoticeVoList.add(sysNoticeVo);
        }
        return BasePageResponse.ok(sysNoticeVoList, page);
    }

    /**
     * 消息列表-查看
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse getDetail(Integer id) {
        // 查看标记已读
        this.markIsRead(id);
        SysNotice sysNotice = sysNoticeMapper.selectById(id);
        SysNoticeDetailVo sysNoticeDetailVo = this.getSysNoticeDetailVo(sysNotice);
        return BaseResponse.ok(sysNoticeDetailVo);
    }

    /**
     * 首页-我的待办
     *
     * @return
     */
    @Override
    public BaseResponse<List<SysNoticeVo>> getMyTodoList() {
        UserVo user = UserContext.getUser();
        List<SysNotice> sysNoticeList = sysNoticeMapper.selectList(
                new LambdaQueryWrapper<SysNotice>()
                        .eq(SysNotice::getReceiverId, user.getUserId())
                        .eq(SysNotice::getIsRead, SysNoticeConstant.NOT_READ)
                        .orderByDesc(SysNotice::getCreateTime));
        List<SysNoticeVo> sysNoticeVoList = new ArrayList<>();
        for (SysNotice sysNotice : sysNoticeList) {
            SysNoticeVo sysNoticeVo = this.getSysNoticeVo(sysNotice);
            sysNoticeVoList.add(sysNoticeVo);
        }
        return BaseResponse.ok(sysNoticeVoList);
    }

    /**
     * 发送消息
     *
     * @param param
     */

    @Override
    @Async("asyncTaskExecutor")
    public void sendTireWarningNotice(SysNoticeWarningParam param) {
        // 查询车队信息
        List<SysFleetNotice> sysFleetNoticeList = sysFleetNoticeMapper.selectList(
                new LambdaQueryWrapper<SysFleetNotice>()
                        .eq(SysFleetNotice::getCustomerId, param.getFleetId()));
        // 已发手机号
        List<String> isSendedList = new ArrayList<>();
        for (WarningTypeEnum warningTypeEnum : param.getWarningTypeEnumList()) {
            for (SysFleetNotice sysFleetNotice : sysFleetNoticeList) {
                // 告警类型是否相同
                if (warningTypeEnum.getType() == sysFleetNotice.getId()) {
                    List<String> phoneList = new ArrayList<>();
                    String receivedAccounts = sysFleetNotice.getReceivedAccount();
                    if (StringUtils.isNotBlank(receivedAccounts)) {
                        for (String account : receivedAccounts.split(",")) {
                            // 判断account是否为手机号
                            if (this.isValidPhoneNumber(account)
                                    && !isSendedList.contains(account)) {
                                phoneList.add(account);
                                isSendedList.add(account);
                            }
                        }
                    }
                    if (!phoneList.isEmpty()) {
                        List<SysUser> sysUserList = sysUserMapper.selectList(
                                new LambdaQueryWrapper<SysUser>()
                                        .in(SysUser::getPhone, phoneList)
                                        .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
                        for (SysUser sysUser : sysUserList) {
                            SysNotice sysNotice = new SysNotice();
                            sysNotice.setTargetId(param.getTargetId());
                            sysNotice.setType(param.getType());
                            sysNotice.setMainTitle(param.getMainTitle());
                            sysNotice.setSecondTitle(param.getSecondTitle());
                            sysNotice.setContent(param.getContent());
                            sysNotice.setSenderId(param.getSenderId());
                            sysNotice.setSender(param.getSender());
                            sysNotice.setReceiverId(sysUser.getId());
                            sysNotice.setReceiver(sysUser.getName());
                            sysNoticeMapper.insert(sysNotice);
                        }
                    }
                }
            }
        }
    }

    /**
     * 温压管理-报警记录与跟进-报警提醒轮询
     *
     * @param
     * @return
     */
    @Override
    public BaseResponse<List<NoticeSimpleVo>> poll() {
        UserVo user = UserContext.getUser();
        List<SysNotice> sysNoticeList = sysNoticeMapper.selectList(
                new LambdaQueryWrapper<SysNotice>()
                        .eq(SysNotice::getReceiverId, user.getUserId())
                        .eq(SysNotice::getIsRead, SysNoticeConstant.NOT_READ)
                        .orderByDesc(SysNotice::getCreateTime));
        List<NoticeSimpleVo> noticeSimpleVoList = new ArrayList<>();
        for (SysNotice sysNotice : sysNoticeList) {
            NoticeSimpleVo noticeSimpleVo = new NoticeSimpleVo();
            noticeSimpleVo.setId(sysNotice.getId());
            noticeSimpleVo.setTargetId(sysNotice.getTargetId());
            noticeSimpleVo.setMainTitle(sysNotice.getMainTitle());
            noticeSimpleVo.setSecondTitle(sysNotice.getSecondTitle());
            noticeSimpleVo.setContent(sysNotice.getContent());
            noticeSimpleVo.setCreateTime(sysNotice.getCreateTime());
            noticeSimpleVoList.add(noticeSimpleVo);
        }
        return BaseResponse.ok(noticeSimpleVoList);
    }

    /**
     * 温压管理-报警记录与跟进-批量已读(我知道了)
     *
     * @param
     * @param param
     * @return
     */
    @Override
    public BaseResponse batchRead(NoticeBatchReadParam param) {
        List<Integer> idList = param.getIdList();
        for (Integer id : idList) {
            this.markIsRead(id);
        }
        return BaseResponse.ok();
    }

    /**
     * 根据目标主键和类型修改为已读
     *
     * @param targetId
     * @param type
     * @return
     */
    @Override
    public BaseResponse updateReadByTargetId(Integer targetId, Integer type) {
        List<SysNotice> sysNoticeList = sysNoticeMapper.selectList(
                new LambdaQueryWrapper<SysNotice>()
                        .eq(SysNotice::getTargetId, targetId)
                        .eq(SysNotice::getType, type)
                        .eq(SysNotice::getIsRead, SysNoticeConstant.NOT_READ));
        for (SysNotice sysNotice : sysNoticeList) {
            // 修改为已读
            this.markIsRead(sysNotice.getId());
        }
        return BaseResponse.ok();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        // 使用正则表达式匹配手机号
        String regex = "^1[0-9]{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private void markIsRead(Integer id) {
        SysNotice updateParam = new SysNotice();
        updateParam.setId(id);
        updateParam.setIsRead(SysNoticeConstant.IS_READ);
        sysNoticeMapper.updateById(updateParam);
    }

    private SysNoticeDetailVo getSysNoticeDetailVo(SysNotice sysNotice) {
        SysNoticeDetailVo sysNoticeDetailVo = new SysNoticeDetailVo();
        sysNoticeDetailVo.setId(sysNotice.getId());
        sysNoticeDetailVo.setMainTitle(sysNotice.getMainTitle());
        sysNoticeDetailVo.setSecondTitle(sysNotice.getSecondTitle());
        sysNoticeDetailVo.setContent(sysNotice.getContent());
        sysNoticeDetailVo.setSenderId(sysNotice.getSenderId());
        sysNoticeDetailVo.setSender(sysNotice.getSender());
        sysNoticeDetailVo.setReceiverId(sysNotice.getReceiverId());
        sysNoticeDetailVo.setReceiver(sysNotice.getReceiver());
        sysNoticeDetailVo.setIsRead(sysNotice.getIsRead());
        sysNoticeDetailVo.setCreateTime(sysNotice.getCreateTime());
        return sysNoticeDetailVo;
    }

    private LambdaQueryWrapper<SysNotice> getWrapper(SysNoticeConditionQueryParam param,
            UserVo user) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotice::getReceiverId, user.getUserId());
        wrapper.eq(SysNotice::getIsRead, SysNoticeConstant.NOT_READ);
        if (StringUtils.isNotBlank(param.getTitle())) {
            wrapper.like(SysNotice::getMainTitle, param.getTitle());
        }
        wrapper.orderByDesc(SysNotice::getCreateTime);
        return wrapper;
    }

    private SysNoticeVo getSysNoticeVo(SysNotice sysNotice) {
        SysNoticeVo sysNoticeVo = new SysNoticeVo();
        sysNoticeVo.setId(sysNotice.getId());
        sysNoticeVo.setMainTitle(sysNotice.getMainTitle());
        sysNoticeVo.setSecondTitle(sysNotice.getSecondTitle());
        sysNoticeVo.setContent(sysNotice.getContent());
        sysNoticeVo.setSenderId(sysNotice.getSenderId());
        sysNoticeVo.setSender(sysNotice.getSender());
        sysNoticeVo.setReceiverId(sysNotice.getReceiverId());
        sysNoticeVo.setReceiver(sysNotice.getReceiver());
        sysNoticeVo.setIsRead(sysNotice.getIsRead());
        sysNoticeVo.setCreateTime(sysNotice.getCreateTime());
        return sysNoticeVo;
    }
}
