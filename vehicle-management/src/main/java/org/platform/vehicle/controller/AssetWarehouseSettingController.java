package org.platform.vehicle.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.param.WarehouseSettingAddParam;
import org.platform.vehicle.param.WarehouseSettingConditionQueryParam;
import org.platform.vehicle.param.WarehouseSettingEditParam;
import org.platform.vehicle.service.WarehouseSettingService;
import org.platform.vehicle.vo.WarehouseSettingVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 资产管理-仓库管理
 *
 * @Author gejiawei
 * @Date 2023/8/25 09:05
 */

@Slf4j
@RestController
@RequestMapping("/asset/warehouseSetting")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetWarehouseSettingController {

    private final WarehouseSettingService warehouseSettingService;

    /**
     * 资产管理-仓库管理-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<WarehouseSettingVo>> conditionQuery(@RequestBody
    WarehouseSettingConditionQueryParam param) {
        log.info("资产管理-仓库管理-条件查询, url:/warehouseSetting/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return warehouseSettingService.conditionQuery(param);
    }

    /**
     * 资产管理-仓库管理-查看仓库详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<WarehouseSettingVo> getWarehouseDetail(@PathVariable("id") Integer id) {
        log.info("资产管理-仓库管理-查看仓库详情, url:/warehouseSetting/detail/{}", id);
        return warehouseSettingService.getWarehouseDetail(id);
    }

    /**
     * 资产管理-仓库管理-新增
     *
     * @param param
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@RequestBody WarehouseSettingAddParam param,
            HttpServletRequest request) {
        log.info("资产管理-仓库管理-新增, url:/warehouseSetting/add, param:{}",
                JSONObject.toJSON(param));
        return warehouseSettingService.add(param, request);
    }

    /**
     * 资产管理-仓库管理-修改
     *
     * @param param
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody WarehouseSettingEditParam param,
            HttpServletRequest request) {
        log.info("资产管理-仓库管理-修改, url:/warehouseSetting/edit, param:{}",
                JSONObject.toJSON(param));
        return warehouseSettingService.edit(param, request);
    }

    /**
     * 资产管理-仓库管理-删除
     *
     * @param id
     * @param phone
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        log.info("资产管理-仓库管理-删除, url:/warehouseSetting/delete, id:{}, phone:{}", id,
                phone);
        return warehouseSettingService.delete(id, phone, request);
    }


}
