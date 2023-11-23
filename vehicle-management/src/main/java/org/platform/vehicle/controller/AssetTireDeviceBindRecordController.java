package org.platform.vehicle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.AssetTireDeviceBindRecordConditionQueryParam;
import org.platform.vehicle.service.AssetTireDeviceBindRecordService;
import org.platform.vehicle.vo.AssetTireDeviceBindRecordPageVo;
import org.platform.vehicle.response.BasePageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 资产管理-胎压部件绑定记录
 *
 * @author geforever
 * @since 2023-09-14 14:42:23
 */
@Slf4j
@RestController
@RequestMapping("/asset/tire/deviceBind")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireDeviceBindRecordController {

    private final AssetTireDeviceBindRecordService assetTireDeviceBindRecordService;

    /**
     * 资产管理-胎压部件绑定记录-条件查询
     *
     * @Param param
     * @Return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<AssetTireDeviceBindRecordPageVo>> conditionQuery(
            @RequestBody AssetTireDeviceBindRecordConditionQueryParam param) {
        log.info(
                "资产管理-胎压部件绑定记录-条件查询, url:/assetTire/deviceBind/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return assetTireDeviceBindRecordService.conditionQuery(param);
    }

}

