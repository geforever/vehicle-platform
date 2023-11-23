package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.aspect.RepeatSubmit;
import org.platform.vehicle.param.AssetTireConditionQueryParam;
import org.platform.vehicle.param.AssetTireSensorBindParam;
import org.platform.vehicle.param.AssetTireStockOutParam;
import org.platform.vehicle.service.AssetTireService;
import org.platform.vehicle.vo.AssertTireDetailVo;
import org.platform.vehicle.vo.AssetTireEditParam;
import org.platform.vehicle.vo.AssetTirePageVo;
import org.platform.vehicle.vo.AssetTireStockInParam;
import org.platform.vehicle.vo.FleetWarehouseVo;
import org.platform.vehicle.vo.TireSiteSimpleVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资产管理-轮胎列表
 *
 * @author geforever
 * @since 2023-09-14 14:42:23
 */
@Slf4j
@RestController
@RequestMapping("/asset/tire")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireController {

    /**
     * 服务对象
     */
    private final AssetTireService assetTireService;

    /**
     * 资产管理-轮胎列表-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<AssetTirePageVo>> conditionQuery(
            @RequestBody AssetTireConditionQueryParam param) {
        log.info("资产管理-轮胎列表-条件查询, url:/assetTire/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.conditionQuery(param);
    }

    /**
     * 资产管理-轮胎列表-导出
     *
     * @param param
     * @return
     */
    @PostMapping("/export")
    public void export(@RequestBody AssetTireConditionQueryParam param,
            HttpServletResponse response) {
        log.info("资产管理-轮胎列表-导出, url:/assetTire/export, param:{}",
                JSONObject.toJSON(param));
        assetTireService.export(param, response);
    }

    /**
     * 资产管理-轮胎列表-轮胎入库
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/stockIn")
    public BaseResponse stockIn(@RequestBody AssetTireStockInParam param,
            HttpServletRequest request) {
        log.info("资产管理-轮胎列表-轮胎入库, url:/assetTire/stockIn, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.stockIn(param, request);
    }

    /**
     * 资产管理-轮胎列表-轮胎出库
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/stockOut")
    public BaseResponse stockOut(@RequestBody AssetTireStockOutParam param,
            HttpServletRequest request) {
        log.info("资产管理-轮胎列表-轮胎出库, url:/assetTire/stockOut, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.stockOut(param, request);
    }

    /**
     * 资产管理-轮胎列表-获取明细
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<AssertTireDetailVo> getAssetTireDetail(@PathVariable("id") Integer id) {
        log.info("资产管理-轮胎列表-获取明细, url:/assetTire/getAssetTireDetail/{id}, id:{}", id);
        return assetTireService.getAssetTireDetail(id);
    }

    /**
     * 资产管理-轮胎列表-轮胎编辑
     *
     * @param param
     * @return
     */
    @RepeatSubmit(lockField = "id")
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody AssetTireEditParam param, HttpServletRequest request) {
        log.info("资产管理-轮胎列表-轮胎编辑, url:/assetTire/edit, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.edit(param, request);
    }

    /**
     * 资产管理-轮胎列表-传感器解绑捆绑
     *
     * @param param
     * @return
     */
    @PostMapping("/sensorBind")
    public BaseResponse editSensorBind(@RequestBody AssetTireSensorBindParam param) {
        log.info("资产管理-轮胎列表-传感器解绑捆绑, url:/assetTire/editSensorBind, param:{}",
                JSONObject.toJSON(param));
        return assetTireService.editSensorBind(param);
    }

    /**
     * 资产管理-轮胎列表-获取轮胎编码(轮胎入库是获取轮胎编码用)
     *
     * @return
     */
    @GetMapping("/getTireCode")
    public BaseResponse getTireCode() {
        log.info("资产管理-轮胎列表-获取轮胎编码, url:/assetTire/getTireCode");
        return assetTireService.getTireCode();
    }

    /**
     * 资产管理-轮胎列表-根据轮胎编号获取传感器ID
     *
     * @param tireCode
     * @return
     */
    @GetMapping("/sensorId/{tireCode}")
    public BaseResponse getSensorId(@PathVariable("tireCode") String tireCode) {
        log.info(
                "资产管理-轮胎列表-根据轮胎编号获取传感器ID, url:/assetTire/sensorId/{tireCode}, tireCode:{}",
                tireCode);
        return assetTireService.getSensorId(tireCode);
    }

    /**
     * 资产管理-轮胎列表-轮胎号模糊查询
     *
     * @param tireCode
     * @return
     */
    @GetMapping("/tireCode/{tireCode}")
    public BaseResponse<List<String>> getTireCodeLikeTireCode(
            @PathVariable("tireCode") String tireCode) {
        log.info(
                "资产管理-轮胎列表-轮胎号模糊查询, url:/assetTire/tireCode/{tireCode}, tireCode:{}",
                tireCode);
        return assetTireService.getTireCodeLikeTireCode(tireCode);
    }

    /**
     * 资产管理-轮胎列表-删除
     *
     * @param id
     * @param phone
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        log.info("资产管理-轮胎列表-删除, url:/assetTire/delete, id:{}, phone:{}", id, phone);
        return assetTireService.delete(id, phone, request);
    }

    /**
     * 资产管理-轮胎列表-导入模版下载
     *
     * @param type:1-轮胎导入,2-轮胎安装
     * @retirm
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(Integer type, HttpServletResponse response) {
        log.info("资产管理-轮胎列表-导入模版下载, url:/assetTire/downloadTemplate/{type}, type:{}",
                type);
        assetTireService.downloadTemplate(type, response);
    }

    /**
     * 资产管理-轮胎列表-批量入库
     *
     * @param file
     * @return
     */
    @PostMapping("/import/batchStockIn")
    public BaseResponse batchStockIn(MultipartFile file) {
        log.info("资产管理-轮胎列表-导入数据, url:/assetTire/import/batchStockIn");
        return assetTireService.batchStockIn(file);
    }

    /**
     * 资产管理-轮胎列表-批量安装
     *
     * @param file
     * @return
     */
    @PostMapping("/import/batchInstall")
    public BaseResponse batchInstall(MultipartFile file) {
        log.info("资产管理-轮胎列表-导入数据, url:/assetTire/import/batchInstall");
        return assetTireService.batchInstall(file);
    }

    /**
     * 资产管理-轮胎列表-选择轮位
     *
     * @param licensePlate
     * @return
     */
    @GetMapping("/getTireSite/{licensePlate}")
    public BaseResponse<List<TireSiteSimpleVo>> getTireSite(
            @PathVariable("licensePlate") String licensePlate) {
        log.info("资产管理-轮胎列表-选择轮位, url:/assetTire/getTireSite/{licensePlate}");
        return assetTireService.getTireSite(licensePlate);
    }

    /**
     * 资产管理-轮胎列表-选择调入仓库
     *
     * @return
     */
    @GetMapping("/getFleetWarehouse")
    public BaseResponse<FleetWarehouseVo> getFleetWarehouse() {
        log.info("资产管理-轮胎列表-选择调入仓库, url:/assetTire/getFleetWarehouse");
        return assetTireService.getFleetWarehouse();
    }

    /**
     * 资产管理-轮胎列表-通过车队ID选择调入仓库
     *
     * @param fleetId
     * @return
     */
    @GetMapping("/getWarehouse/{fleetId}")
    public BaseResponse<List<FleetWarehouseVo>> getWarehouseByFleetId(
            @PathVariable("fleetId") Integer fleetId) {
        log.info("资产管理-轮胎列表-通过车队ID选择调入仓库, url:/assetTire/getWarehouse/{fleetId}");
        return assetTireService.getWarehouseByFleetId(fleetId);
    }
}

