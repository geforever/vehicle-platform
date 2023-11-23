package org.platform.vehicle.controller;

import org.platform.vehicle.param.NoticeBatchReadParam;
import org.platform.vehicle.param.SysNoticeConditionQueryParam;
import org.platform.vehicle.service.SysNoticeService;
import org.platform.vehicle.vo.NoticeSimpleVo;
import org.platform.vehicle.vo.SysNoticeDetailVo;
import org.platform.vehicle.vo.SysNoticeVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息列表
 *
 * @Author gejiawei
 * @Date 2023/9/6 11:09
 */
@Slf4j
@RestController
@RequestMapping("/sysNotice")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    /**
     * 消息列表-获取当前用户未读消息数量
     *
     * @return
     */
    @GetMapping("/getNotReadCount")
    public BaseResponse getNotReadCount() {
        log.info("消息列表-获取当前用户未读消息数量, url:/sysNotice/getNotReadCount");
        return sysNoticeService.getNotReadCount();
    }

    /**
     * 首页-我的待办
     *
     * @return
     */
    @GetMapping("/getMyTodoList")
    public BaseResponse<List<SysNoticeVo>> getMyTodoList() {
        log.info("首页-我的待办, url:/sysNotice/getMyTodoList");
        return sysNoticeService.getMyTodoList();
    }


    /**
     * 消息列表-条件查询
     *
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<SysNoticeVo>> conditionQuery(
            @RequestBody SysNoticeConditionQueryParam param) {
        log.info("消息列表-条件查询, url:/sysNotice/conditionQuery, param:{}", param);
        return sysNoticeService.conditionQuery(param);
    }

    /**
     * 消息列表-查看明细
     *
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<SysNoticeDetailVo> getDetail(@PathVariable("id") Integer id) {
        log.info("消息列表-查看, url:/sysNotice/detail/{}", id);
        return sysNoticeService.getDetail(id);
    }

    /**
     * 温压管理-报警记录与跟进-报警提醒轮询
     *
     * @return
     */
    @GetMapping("/poll")
    public BaseResponse<List<NoticeSimpleVo>> polling() {
        log.info("温压管理-报警记录与跟进-报警提醒轮询, url:/sysNotice/poll");
        return sysNoticeService.poll();
    }

    /**
     * 温压管理-报警记录与跟进-批量已读(我知道了)
     *
     * @return
     */
    @PostMapping("/batchRead")
    public BaseResponse batchRead(@RequestBody NoticeBatchReadParam param) {
        log.info("温压管理-报警记录与跟进-批量已读(我知道了), url:/sysNotice/batchRead");
        return sysNoticeService.batchRead(param);
    }

}
