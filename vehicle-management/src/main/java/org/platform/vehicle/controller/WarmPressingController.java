package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.RelayBindParam;
import org.platform.vehicle.param.TireTrackParam;
import org.platform.vehicle.param.TrailerInstallCallbackParam;
import org.platform.vehicle.param.VehicleHangParam;
import org.platform.vehicle.param.WarmPressingConditionQuery;
import org.platform.vehicle.param.WarmPressingExportParam;
import org.platform.vehicle.param.WarningThresholdSyncParam;
import org.platform.vehicle.param.WheelSyncParam;
import org.platform.vehicle.service.WarmPressingService;
import org.platform.vehicle.vo.TireStatusVo;
import org.platform.vehicle.vo.TireTrendParam;
import org.platform.vehicle.vo.TireTrendVo;
import org.platform.vehicle.vo.VehicleTrackVo;
import org.platform.vehicle.vo.WarmPressingDetailVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
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
 * 温压管理-实时温压
 *
 * @Author gejiawei
 * @Date 2023/10/12 10:34
 */
@Slf4j
@RestController
@RequestMapping("/warmPressing")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarmPressingController {

    private final WarmPressingService warmPressingService;

    /**
     * 温压管理-实时温压-车辆条件查询
     *
     * @param param
     * @return return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse conditionQuery(@RequestBody WarmPressingConditionQuery param) {
        log.info("温压管理-实时温压-车辆条件查询, url:/warmPressing/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return warmPressingService.conditionQuery(param);
    }

    /**
     * 温压管理-实时温压-详情
     *
     * @param licensePlate
     * @return return
     */
    @GetMapping("/detail/{licensePlate}")
    public BaseResponse<WarmPressingDetailVo> getWarmPressingDetailDetail(
            @PathVariable("licensePlate") String licensePlate) {
        log.info("温压管理-实时温压-详情, url:/warmPressing/detail/{}", licensePlate);
        return warmPressingService.getWarmPressingDetailDetail(licensePlate);
    }

    /**
     * 温压管理-实时温压-轮胎状态查询
     *
     * @param licensePlate 车牌
     * @param tireSite     轮位
     * @return
     */
    @GetMapping("/tireStatus")
    public BaseResponse<TireStatusVo> getTireStatus(String licensePlate, String tireSite) {
        log.info(
                "温压管理-实时温压-轮胎状态查询, url:/warmPressing/tireStatus, licensePlate:{}, tireSite:{}",
                licensePlate, tireSite);
        return warmPressingService.getTireStatus(licensePlate, tireSite);
    }

    /**
     * 温压管理-实时温压-查看轮胎温压趋势
     *
     * @param param
     * @return
     */
    @PostMapping("/tireTrend")
    public BaseResponse<TireTrendVo> getTireTrend(@RequestBody TireTrendParam param) {
        log.info("温压管理-实时温压-查看轮胎温压趋势, url:/warmPressing/tireTrend, param:{}",
                JSONObject.toJSON(param));
        return warmPressingService.getTireTrend(param);
    }

    /**
     * 温压管理-实时温压-查看车辆行驶轨迹
     *
     * @param param
     * @return
     */
    @PostMapping("/vehicleTrack")
    public BaseResponse<List<VehicleTrackVo>> getVehicleTrack(@RequestBody TireTrackParam param) {
        log.info("温压管理-实时温压-查看车辆行驶轨迹, url:/warmPressing/vehicleTrack, param:{}",
                JSONObject.toJSON(param));
        return warmPressingService.getVehicleTrack(param);
    }

    /**
     * 温压管理-实时温压-手动上下挂
     *
     * @param param
     * @return
     */
    @PostMapping("/editTrailerStatus")
    public BaseResponse hang(@RequestBody VehicleHangParam param) {
        log.info("温压管理-实时温压-上下挂, url:/warmPressing/hang, param:{}",
                JSONObject.toJSON(param));
        return warmPressingService.editTrailerStatus(param);
    }

    /**
     * 温压管理-实时温压-根据车牌获取中继器ID
     *
     * @param licensePlate
     * @return
     */
    @GetMapping("/getRelayId/{licensePlate}")
    public BaseResponse<String> getRelayId(@PathVariable("licensePlate") String licensePlate) {
        log.info("温压管理-实时温压-根据车牌获取中继器ID, url:/warmPressing/getRelayId/{}",
                licensePlate);
        return warmPressingService.getRelayId(licensePlate);
    }


    /**
     * 温压管理-实时温压-绑定中继器
     *
     * @param param
     * @return
     */
    @PostMapping("/bindRelay")
    public BaseResponse bindRelay(@RequestBody RelayBindParam param) {
        log.info("温压管理-实时温压-绑定中继器, url:/warmPressing/bindRelay, param:{}",
                JSONObject.toJSON(param));
        return warmPressingService.bindRelay(param);
    }

    /**
     * 温压管理-实时温压-轮位同步
     *
     * @param param
     * @return
     */
    @PostMapping("/syncWheel")
    public BaseResponse syncWheel(@RequestBody WheelSyncParam param) {
        log.info("温压管理-实时温压-轮位同步, url:/warmPressing/syncWheel, param:{}",
                JSONObject.toJSON(param));
        return warmPressingService.syncWheel(param);
    }

    /**
     * 温压管理-实时温压-阈值同步
     *
     * @param param
     * @return
     */
    @PostMapping("/syncThreshold")
    public BaseResponse syncThreshold(@RequestBody WarningThresholdSyncParam param) {
        log.info("温压管理-实时温压-阈值同步, url:/warmPressing/syncThreshold, param:{}",
                JSONObject.toJSON(param));
        return warmPressingService.syncThreshold(param);
    }


    /**
     * 温压管理-实时温压-上下挂回调
     *
     * @param param
     */
    @PostMapping("/trailerInstallCallback")
    public void trailerInstallCallback(@RequestBody TrailerInstallCallbackParam param) {
        log.info("温压管理-实时温压-上下挂回调, url:/warmPressing/trailerInstallCallback, param:{}",
                JSONObject.toJSON(param));
        warmPressingService.trailerUnInstallCallback(param);
    }

    /**
     * 温压管理-实时温压-数据导出
     *
     * @param param
     */
    @PostMapping("/export")
    public void export(@RequestBody WarmPressingExportParam param,
            HttpServletResponse response) {
        log.info("温压管理-实时温压-数据导出, url:/warmPressing/export, param:{}",
                JSONObject.toJSON(param));
        warmPressingService.export(param, response);
    }
}
