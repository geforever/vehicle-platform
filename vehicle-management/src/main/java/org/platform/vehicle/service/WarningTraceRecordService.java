package org.platform.vehicle.service;

import org.platform.vehicle.param.RunningStatusChangeParam;
import org.platform.vehicle.param.TireCheckDataParam;
import org.platform.vehicle.param.WarningDetailRecordConditionQueryParam;
import org.platform.vehicle.param.WarningTraceRecordConditionQueryParam;
import org.platform.vehicle.param.WarningTraceRecordFollowUpParam;
import org.platform.vehicle.vo.FollowUpHistoryRecordVo;
import org.platform.vehicle.vo.TirePressureAndTemperatureStatisticVo;
import org.platform.vehicle.vo.TrendStatisticVo;
import org.platform.vehicle.vo.UnhandLedWarningCountVo;
import org.platform.vehicle.vo.VehicleAndTireStatisticVo;
import org.platform.vehicle.vo.WarningRemarkTemplateVo;
import org.platform.vehicle.vo.WarningTraceDetailRecordVo;
import org.platform.vehicle.vo.WarningTraceRecordVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * (WarningTraceRecord)表服务接口
 *
 * @author geforever
 * @since 2023-09-27 16:05:55
 */
public interface WarningTraceRecordService {

    /**
     * 温压管理-报警记录与跟进-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<WarningTraceRecordVo>> conditionQuery(
            WarningTraceRecordConditionQueryParam param);

    /**
     * 温压管理-报警记录与跟进-查看明细
     *
     * @param param
     * @return
     */
    BasePageResponse<List<WarningTraceDetailRecordVo>> getDetailRecord(
            WarningDetailRecordConditionQueryParam param);

    /**
     * 温压管理-报警记录与跟进-跟进
     *
     * @param param
     * @return
     */
    BaseResponse followUp(WarningTraceRecordFollowUpParam param);

    /**
     * 温压管理-报警记录与跟进-报警跟踪处理（去处理）
     *
     * @param warningTraceRecordId
     * @return
     */
    BaseResponse<FollowUpHistoryRecordVo> followUpRecord(Integer warningTraceRecordId);

    /**
     * 温压管理-报警记录与跟进-运营状态变更
     *
     * @param param
     * @return
     */
    BaseResponse changeRunningStatus(RunningStatusChangeParam param);

    /**
     * 温压管理-报警记录与跟进-导出
     *
     * @param param
     * @param response
     * @return
     */
    void exportWarningTraceRecord(WarningTraceRecordConditionQueryParam param,
            HttpServletResponse response);

    /**
     * 温压管理-胎温胎压概览-胎温胎压报警
     *
     * @return
     */
    BaseResponse<TirePressureAndTemperatureStatisticVo> getWarningData();

    /**
     * 温压管理-胎温胎压概览-车辆轮胎概况
     *
     * @return
     */
    BaseResponse<VehicleAndTireStatisticVo> getVehicleAndTireData();

    /**
     * 温压管理-胎温胎压概览-趋势图
     *
     * @return
     */
    BaseResponse<TrendStatisticVo> getTrendData();

    /**
     * 温压管理-报警记录与跟进-未处理过的报警数量
     *
     * @return
     */
    BaseResponse<UnhandLedWarningCountVo> unhandledWarningCount();

    /**
     * 轮胎异常数据校验
     *
     * @param paramList
     * @return
     */
    BaseResponse checkTireData(List<TireCheckDataParam> paramList);

    /**
     * 温压管理-报警记录与跟进-获取跟进备注模版
     *
     * @param warningTypeList
     * @return
     */
    BaseResponse<List<WarningRemarkTemplateVo>> followUpTemplate(List<Integer> warningTypeList);

}
