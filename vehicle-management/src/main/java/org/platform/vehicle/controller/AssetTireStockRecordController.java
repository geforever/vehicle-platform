package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.AssetTireStockRecordConditionQueryParam;
import org.platform.vehicle.service.AssetTireStockRecordService;
import org.platform.vehicle.vo.AssetTireStockRecordPageVo;
import org.platform.vehicle.response.BasePageResponse;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产管理-轮胎出入库记录
 *
 * @author geforever
 * @since 2023-09-14 14:42:23
 */
@Slf4j
@RestController
@RequestMapping("/asset/tire/stockRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireStockRecordController {

    private final AssetTireStockRecordService assetTireStockRecordService;

    /**
     * 资产管理-轮胎出入库记录-分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<AssetTireStockRecordPageVo>> conditionQuery(
            @RequestBody AssetTireStockRecordConditionQueryParam param) {
        log.info(
                "资产管理-轮胎出入库记录-分页查询, url:/assetTire/stockRecord/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return assetTireStockRecordService.conditionQuery(param);
    }

    /**
     * 资产管理-轮胎入库记录-导出
     *
     * @param param
     * @return
     */
    @PostMapping("/stockIn/export")
    public void export(
            @RequestBody AssetTireStockRecordConditionQueryParam param,
            HttpServletResponse response) {
        log.info("资产管理-轮胎入库记录-导出, url:/assetTire/stockRecord/stockIn/export, param:{}",
                JSONObject.toJSON(param));
        assetTireStockRecordService.stockInExport(param, response);
    }

    /**
     * 资产管理-轮胎出库记录-导出
     *
     * @param param
     * @return
     */
    @PostMapping("/stockOut/export")
    public void stockOutExport(
            @RequestBody AssetTireStockRecordConditionQueryParam param,
            HttpServletResponse response) {
        log.info("资产管理-轮胎出库记录-导出, url:/assetTire/stockRecord/stockOut/export, param:{}",
                JSONObject.toJSON(param));
        assetTireStockRecordService.stockOutExport(param, response);
    }

}

