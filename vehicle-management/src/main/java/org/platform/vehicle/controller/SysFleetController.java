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
import org.platform.vehicle.param.FleetConditionQuery;
import org.platform.vehicle.param.FleetWxUserInfo;
import org.platform.vehicle.param.SysFleetAddParam;
import org.platform.vehicle.param.SysFleetEditParam;
import org.platform.vehicle.service.SysFleetService;
import org.platform.vehicle.vo.SysFleetDetailVo;
import org.platform.vehicle.vo.SysFleetVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 档案管理-车队档案
 *
 * @Author gejiawei
 * @Date 2023/8/30 10:01
 */
@Slf4j
@RestController
@RequestMapping("/sysFleet")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysFleetController {

    private final SysFleetService sysFleetService;

    /**
     * 车队档案-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<SysFleetVo>> conditionQuery(
            @RequestBody FleetConditionQuery param) {
        log.info("车队档案-条件查询, url:/sysClient/conditionQuery, param:{}",
                JSONObject.toJSONString(param));
        return sysFleetService.conditionQuery(param);
    }

    /**
     * 车队档案-查看车队详情
     *
     * @param fleetId
     * @return
     */
    @GetMapping("/detail/{fleetId}")
    public BaseResponse<SysFleetDetailVo> getFleetDetail(@PathVariable("fleetId") Integer fleetId) {
        log.info("车队档案-查看车队详情, url:/sysClient/{}", fleetId);
        return sysFleetService.getFleetDetail(fleetId);
    }

    /**
     * 车队档案-新增
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/add")
    public BaseResponse addFleet(@RequestBody SysFleetAddParam param, HttpServletRequest request) {
        log.info("车队档案-新增, url:/sysClient/add, param:{}",
                JSONObject.toJSONString(param));
        return sysFleetService.addFleet(param, request);
    }

    /**
     * 车队档案-修改
     *
     * @param param
     * @return
     */
    @RepeatSubmit(lockField = "id")
    @PostMapping("/edit")
    public BaseResponse editFleet(@RequestBody SysFleetEditParam param,
            HttpServletRequest request) {
        log.info("车队档案-修改, url:/sysClient/edit, param:{}",
                JSONObject.toJSONString(param));
        return sysFleetService.editFleet(param, request);
    }

    /**
     * 车队档案-删除
     *
     * @param id
     * @param phone
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse deleteFleet(Integer id, String phone, HttpServletRequest request) {
        log.info("车队档案-删除, url:/sysClient/delete, id:{}, phone:{}", id, phone);
        return sysFleetService.deleteFleet(id, phone, request);
    }

    /**
     * 获取当前登录人所属客户列表
     *
     * @return
     */
    @GetMapping("/getClientList")
    public BaseResponse<List<SysFleetVo>> getClientList() {
        log.info("客户档案-获取当前登录人所属客户列表, url:/sysClient/getClientList");
        return sysFleetService.getClientList();
    }

    /**
     * 获取当前登录人上级车队列表
     *
     * @return
     */
    @GetMapping("/getParentFleetList")
    public BaseResponse<List<SysFleetVo>> getParentFleetList(Integer clientId) {
        log.info("客户档案-获取当前登录人上级车队列表, url:/sysClient/getParentFleetList");
        return sysFleetService.getParentFleetList(clientId);
    }

    /**
     * 获取当前用户及下属已绑定微信的用户(通知接收人)
     *
     * @return
     */
    @GetMapping("/notice/getWxUser")
    public BaseResponse<List<FleetWxUserInfo>> getWxUser() {
        log.info("客户档案-获取当前用户及下属已绑定微信的用户, url:/sysClient/notice/getWxUser");
        return sysFleetService.getwxUser();
    }


}
