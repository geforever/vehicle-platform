package org.platform.vehicle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author gejiawei
 * @Date 2023/9/18 10:22
 */

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.aspect.RepeatSubmit;
import org.platform.vehicle.param.AssetTireFitConditionQuery;
import org.platform.vehicle.param.InstallTireParam;
import org.platform.vehicle.param.UninstallTireParam;
import org.platform.vehicle.service.AssetTireService;
import org.platform.vehicle.vo.AssetTireFitDetailVo;
import org.platform.vehicle.vo.AssetTireFitPageVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 资产管理-轮胎拆装
 *
 * @author geforever
 * @since 2023-09-14 14:42:23
 */
@Slf4j
@RestController
@RequestMapping("/asset/tire/fit")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireFitController {

    private final AssetTireService assetTireService;

    /**
     * 资产管理-轮胎拆装-车辆分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<AssetTireFitPageVo>> conditionQuery(
            @RequestBody AssetTireFitConditionQuery param) {
        log.info("资产管理-轮胎拆装-车辆分页查询, url:/asset/tire/fit/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.fitConditionQuery(param);
    }

    /**
     * 资产管理-轮胎拆装-明细
     *
     * @param licensePlate
     * @return
     */
    @GetMapping("/detail/{licensePlate}")
    public BaseResponse<List<AssetTireFitDetailVo>> getFitDetail(
            @PathVariable("licensePlate") String licensePlate) {
        log.info("资产管理-轮胎拆装-明细, url:/asset/tire/fit/detail, licensePlate:{}",
                licensePlate);
        return assetTireService.getFitDetail(licensePlate);
    }

    /**
     * 资产管理-轮胎拆装-安装轮胎
     *
     * @param param
     * @return
     */
    @RepeatSubmit(lockField = "tireCode")
    @PostMapping("/installTire")
    public BaseResponse installTire(@RequestBody InstallTireParam param) {
        log.info("资产管理-轮胎拆装-安装轮胎, url:/asset/tire/fit/installTire, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.installTire(param);
    }

    /**
     * 资产管理-轮胎拆装-拆卸轮胎
     *
     * @param param
     * @return
     */
    @RepeatSubmit(lockField = "tireCode")
    @PostMapping("/uninstallTire")
    public BaseResponse uninstallTire(@RequestBody UninstallTireParam param) {
        log.info("资产管理-轮胎拆装-拆卸轮胎, url:/asset/tire/fit/uninstallTire, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.uninstallTire(param);
    }

}
