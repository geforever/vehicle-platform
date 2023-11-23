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
import org.platform.vehicle.aspect.RepeatSubmit;
import org.platform.vehicle.param.ClientAddParam;
import org.platform.vehicle.param.ClientConditionQuery;
import org.platform.vehicle.param.ClientEditParam;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.vo.ClientDetailVo;
import org.platform.vehicle.vo.ClientNameVo;
import org.platform.vehicle.vo.SysClientVo;
import org.platform.vehicle.vo.SysCustomerTreeVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 档案管理-客户档案
 *
 * @Author gejiawei
 * @Date 2023/8/28 15:57
 */
@Slf4j
@RestController
@RequestMapping("/sysClient")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysClientController {

    private final SysClientService sysClientService;

    /**
     * 客户档案-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<SysClientVo>> conditionQuery(
            @RequestBody ClientConditionQuery param) {
        log.info("客户档案-条件查询, url:/sysClient/conditionQuery, param:{}",
                JSONObject.toJSONString(param));
        return sysClientService.conditionQuery(param);
    }

    /**
     * 客户档案-查看客户详情
     *
     * @param clientId
     * @return
     */
    @GetMapping("/detail/{clientId}")
    public BaseResponse<ClientDetailVo> getClientDetail(
            @PathVariable("clientId") Integer clientId) {
        log.info("客户档案-查看客户详情, url:/sysClient/{}", clientId);
        return sysClientService.getClientDetail(clientId);
    }

    /**
     * 客户档案-新增客户
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/add")
    public BaseResponse add(@RequestBody ClientAddParam param, HttpServletRequest request) {
        log.info("客户档案-新增客户, url:/sysClient/add, param:{}", JSONObject.toJSONString(param));
        return sysClientService.addClient(param, request);
    }

    /**
     * 客户档案-修改客户
     *
     * @param param
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody ClientEditParam param, HttpServletRequest request) {
        log.info("客户档案-修改客户, url:/sysClient/edit, param:{}",
                JSONObject.toJSONString(param));
        return sysClientService.editClient(param, request);
    }

    /**
     * 客户档案-删除客户
     *
     * @param clientId
     * @param phone
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse delete(Integer clientId, String phone, HttpServletRequest request) {
        log.info("客户档案-删除客户, url:/sysClient/delete, clientId:{}, phone:{}", clientId,
                phone);
        return sysClientService.deleteClient(clientId, phone, request);
    }

    /**
     * 客户档案-获取所有客户名称
     *
     * @return
     */
    @GetMapping("/getAllClientName")
    public BaseResponse<List<ClientNameVo>> getAllClientName() {
        log.info("客户档案-获取所有客户名称, url:/sysClient/getAllClientName");
        return sysClientService.getAllClientName();
    }

    /**
     * 客户档案-查询当前用户归属
     */
    @GetMapping("/getCustomerTree")
    public BaseResponse<List<SysCustomerTreeVo>> getCustomerTree() {
        log.info("客户档案-查询当前用户归属, url:/sysClient/getCustomerTree");
        return sysClientService.getCustomerTree();
    }


    /**
     * 客户档案-查询当前用户及下属的客户列表
     */
    @GetMapping("/getMyCustomers")
    public BaseResponse<?> getMyCustomers() {
        log.info("客户档案-查询当前用户及下属的客户列表, url:/sysClient/getMyCustomers");
        return BaseResponse.ok(this.sysClientService.getCustomersLite());

    }

    /**
     * 客户档案-查询当前用户及下属的车队列表
     */
    @GetMapping("/getMyFleets")
    public BaseResponse<?> getMyFleets() {
        log.info("客户档案-查询当前用户及下属的车队列表, url:/sysClient/getMyFleets");
        return BaseResponse.ok(this.sysClientService.getFleetsLite());
    }


}
