package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.RunningStatusChangeParam;
import org.platform.vehicle.param.TireCheckDataParam;
import org.platform.vehicle.param.WarningDetailRecordConditionQueryParam;
import org.platform.vehicle.param.WarningTraceRecordConditionQueryParam;
import org.platform.vehicle.param.WarningTraceRecordFollowUpParam;
import org.platform.vehicle.service.WarningTraceRecordService;
import org.platform.vehicle.vo.FollowUpHistoryRecordVo;
import org.platform.vehicle.vo.UnhandLedWarningCountVo;
import org.platform.vehicle.vo.WarningRemarkTemplateVo;
import org.platform.vehicle.vo.WarningTraceDetailRecordVo;
import org.platform.vehicle.vo.WarningTraceRecordVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
 * 温压管理-报警记录与跟进
 *
 * @author geforever
 * @since 2023-09-27 16:05:55
 */
@Slf4j
@RestController
@RequestMapping("/warningTraceRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarningTraceRecordController {

    private final WarningTraceRecordService warningTraceRecordService;

    /**
     * 温压管理-报警记录与跟进-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<WarningTraceRecordVo>> conditionQuery(
            @RequestBody WarningTraceRecordConditionQueryParam param) {
        log.info(
                "温压管理-报警记录与跟进-条件查询, url:/warningTraceRecord/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return warningTraceRecordService.conditionQuery(param);
    }

    /**
     * 温压管理-报警记录与跟进-未处理过的报警数量
     *
     * @return
     */
    @GetMapping("/unhandledWarningCount")
    public BaseResponse<UnhandLedWarningCountVo> unhandledWarningCount() {
        log.info(
                "温压管理-报警记录与跟进-未处理过的报警数量, url:/warningTraceRecord/unhandledWarningCount");
        return warningTraceRecordService.unhandledWarningCount();
    }

    /**
     * 温压管理-报警记录与跟进-查看明细
     *
     * @param param
     * @return
     */
    @PostMapping("/detailRecord")
    public BasePageResponse<List<WarningTraceDetailRecordVo>> getDetailRecord(
            @RequestBody WarningDetailRecordConditionQueryParam param) {
        log.info(
                "温压管理-报警记录与跟进-查看明细, url:/warningTraceRecord/detailRecord, param:{}",
                JSONObject.toJSON(param));
        return warningTraceRecordService.getDetailRecord(param);
    }

    /**
     * 温压管理-报警记录与跟进-跟进
     *
     * @param param
     * @return
     */
    @PostMapping("/followUp")
    public BaseResponse followUp(@RequestBody WarningTraceRecordFollowUpParam param) {
        log.info("温压管理-报警记录与跟进-跟进, url:/warningTraceRecord/followUp, param:{}",
                JSONObject.toJSON(param));
        return warningTraceRecordService.followUp(param);
    }

    /**
     * 温压管理-报警记录与跟进-获取跟进备注模版
     *
     * @param warningTypeIds
     * @return
     */
    @GetMapping("/followUpTemplate")
    public BaseResponse<List<WarningRemarkTemplateVo>> followUpTemplate(String warningTypeIds) {
        log.info(
                "温压管理-报警记录与跟进-获取跟进备注模版, url:/warningTraceRecord/followUpTemplate");
        List<Integer> warningTypeList = new ArrayList<>();
        for (String idStr : warningTypeIds.split(",")) {
            warningTypeList.add(Integer.valueOf(idStr));
        }
        return warningTraceRecordService.followUpTemplate(warningTypeList);
    }

    /**
     * 温压管理-报警记录与跟进-获取报警跟进记录（去处理）
     *
     * @param warningTraceRecordId
     * @return
     */
    @GetMapping("/followUpRecord/{warningTraceRecordId}")
    public BaseResponse<FollowUpHistoryRecordVo> followUpRecord(
            @PathVariable("warningTraceRecordId") Integer warningTraceRecordId) {
        log.info(
                "温压管理-报警记录与跟进-报警跟踪处理（去处理）, url:/warningTraceRecord/followUpRecord, warningTraceRecordId:{}",
                warningTraceRecordId);
        return warningTraceRecordService.followUpRecord(warningTraceRecordId);
    }

    /**
     * 温压管理-报警记录与跟进-运营状态变更
     *
     * @param param
     * @return
     */
    @PostMapping("/changeRunningStatus")
    public BaseResponse changeRunningStatus(@RequestBody RunningStatusChangeParam param) {
        log.info(
                "温压管理-报警记录与跟进-运营状态变更, url:/warningTraceRecord/changeRunningStatus, param:{}",
                JSONObject.toJSON(param));
        return warningTraceRecordService.changeRunningStatus(param);
    }

    /**
     * 温压管理-报警记录与跟进-导出
     *
     * @param param
     * @return
     */
    @PostMapping("/export")
    public void exportWarningTraceRecord(
            @RequestBody WarningTraceRecordConditionQueryParam param,
            HttpServletResponse response) {
        log.info("温压管理-报警记录与跟进-导出, url:/warningTraceRecord/export, param:{}",
                JSONObject.toJSON(param));
        warningTraceRecordService.exportWarningTraceRecord(param, response);
    }

    /**
     * 轮胎异常数据校验
     *
     * @param paramList
     * @return
     */
    @PostMapping("/checkData")
    public BaseResponse checkData(@RequestBody List<TireCheckDataParam> paramList) {
        log.info(
                "温压管理-报警记录与跟进-轮胎异常数据校验, url:/warningTraceRecord/checkData, param:{}",
                JSONObject.toJSON(paramList));
        return warningTraceRecordService.checkTireData(paramList);
    }

}

