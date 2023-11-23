package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.AssetTireFitRecordConditionQueryParam;
import org.platform.vehicle.service.AssetTireFitRecordService;
import org.platform.vehicle.response.BasePageResponse;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产管理-轮胎装卸记录
 *
 * @author geforever
 * @since 2023-09-19 15:26:22
 */
@Slf4j
@RestController
@RequestMapping("/asset/tire/fitRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireFitRecordController {

    @Resource
    private AssetTireFitRecordService assetTireFitRecordService;

    /**
     * 资产管理-轮胎装卸记录-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse conditionQuery(
            @RequestBody AssetTireFitRecordConditionQueryParam param) {
        log.info(
                "资产管理-轮胎装卸记录-条件查询, url:/asset/tire/fitRecord/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return assetTireFitRecordService.conditionQuery(param);
    }

    /**
     * 资产管理-轮胎装卸记录-导出
     *
     * @param param
     */
    @PostMapping("/export")
    public void export(@RequestBody AssetTireFitRecordConditionQueryParam param,
            HttpServletResponse response) {
        log.info(
                "资产管理-轮胎装卸记录-导出, url:/asset/tire/fitRecord/export, param:{}",
                JSONObject.toJSON(param));
        assetTireFitRecordService.export(param, response);
    }
}

