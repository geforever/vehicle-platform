package org.platform.vehicle.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.platform.vehicle.aspect.RepeatSubmit;
import org.platform.vehicle.entity.SysMenu;
import org.platform.vehicle.param.SysRoleAddParam;
import org.platform.vehicle.param.SysRoleConditionQueryParam;
import org.platform.vehicle.param.SysRoleEditParam;
import org.platform.vehicle.service.SysRoleService;
import org.platform.vehicle.vo.SysRoleDetailVo;
import org.platform.vehicle.vo.SysRoleVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统设置-角色设置
 *
 * @Author gejiawei
 * @Date 2023/8/25 09:05
 */

@Slf4j
@RestController
@RequestMapping("/sysRole")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 系统设置-角色设置-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<SysRoleVo>> conditionQuery(
            @RequestBody SysRoleConditionQueryParam param) {
        log.info("系统设置-角色设置-条件查询, url:/sysRole/conditionQuery, param:{}",
                param);
        return sysRoleService.conditionQuery(param);
    }

    /**
     * 系统设置-角色设置-查看角色详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<SysRoleDetailVo> getRoleDetail(@PathVariable("id") Integer id) {
        log.info("系统设置-角色设置-查看角色详情, url:/sysRole/detail/{}", id);
        return sysRoleService.getRoleDetail(id);
    }

    /**
     * 系统设置-角色设置-查看角色权限
     *
     * @param roleId
     * @return
     */
    @GetMapping("/menu/{roleId}")
    public BaseResponse<Set<SysMenu>> getRoleMenu(@PathVariable("roleId") Integer roleId) {
        log.info("系统设置-角色设置-查看角色权限, url:/sysRole/menu/{}", roleId);
        return sysRoleService.getRoleMenu(roleId);
    }

    /**
     * 系统设置-角色设置-新增
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/add")
    public BaseResponse add(@RequestBody SysRoleAddParam param, HttpServletRequest request) {
        log.info("系统设置-角色设置-新增, url:/sysRole/add, param:{}", param);
        return sysRoleService.add(param, request);
    }

    /**
     * 系统设置-角色设置-修改
     *
     * @param param
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody SysRoleEditParam param, HttpServletRequest request) {
        log.info("系统设置-角色设置-修改, url:/sysRole/edit, param:{}", param);
        return sysRoleService.edit(param, request);
    }

    /**
     * 系统设置-角色设置-删除
     *
     * @param id
     * @param phone
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        log.info("系统设置-角色设置-删除, url:/sysRole/delete, param:{},{}", id, phone);
        return sysRoleService.delete(id, phone, request);
    }

    /**
     * 根据customerId查询角色
     *
     * @param customerId
     * @return
     */
    @GetMapping("/getRoleByCustomerId/{customerId}")
    public BaseResponse<List<SysRoleVo>> getRoleByCustomerId(
            @PathVariable("customerId") Integer customerId) {
        log.info("系统设置-角色设置-根据customerId查询角色, url:/sysRole/getRoleByCustomerId/{}",
                customerId);
        return sysRoleService.getRoleByCustomerId(customerId);
    }
}
