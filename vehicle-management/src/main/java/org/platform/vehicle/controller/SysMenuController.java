package org.platform.vehicle.controller;

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

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.aspect.RepeatSubmit;
import org.platform.vehicle.entity.SysMenu;
import org.platform.vehicle.param.SysMenuAddParam;
import org.platform.vehicle.param.SysMenuEditParam;
import org.platform.vehicle.service.SysMenuService;
import org.platform.vehicle.vo.SysMenuDetailVO;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统设置-权限设置
 *
 * @Author gejiawei
 * @Date 2023/8/24 15:57
 */
@Slf4j
@RestController
@RequestMapping("/sysMenu")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 系统设置-权限设置-获取菜单树
     *
     * @return
     */
    @GetMapping("/getTree")
    public BaseResponse<Set<SysMenu>> getTree() {
        log.info("系统设置-权限设置-获取菜单树, url:/sysMenu/getTree");
        return sysMenuService.getTree();
    }

    /**
     * 系统设置-权限设置-获取菜单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<SysMenuDetailVO> getDetailMenu(@PathVariable("id") Integer id) {
        log.info("系统设置-权限设置-获取菜单详情, url:/sysMenu/detail/{}", id);
        return sysMenuService.getDetailMenu(id);
    }

    /**
     * 系统设置-权限设置-获取所有菜单数(包含小程序)
     *
     * @return
     */
    @GetMapping("/getAllTree")
    public BaseResponse<Set<SysMenu>> getAllTree() {
        log.info("系统设置-权限设置-获取所有菜单树, url:/sysMenu/getAllTree");
        return sysMenuService.getAllTree();
    }

    /**
     * 获取当前用户菜单树(用户新增角色)
     *
     * @return
     */
    @GetMapping("/getCurrentUserMenuTree")
    public BaseResponse<Set<SysMenu>> getCurrentUserMenuTree() {
        log.info("获取当前用户菜单树, url:/sysMenu/getCurrentUserMenuTree");
        return sysMenuService.getCurrentUserMenuTree();
    }

    /**
     * 根据客户id获取菜单树(客户新增)
     *
     * @return
     */
    @GetMapping("/clientMenuTree/{clientId}")
    public BaseResponse<Set<SysMenu>> getClientMenuTree(
            @PathVariable("clientId") Integer clientId) {
        log.info("根据客户id获取菜单树, url:/sysMenu/clientMenuTree/{}", clientId);
        return sysMenuService.getClientMenuTree(clientId);
    }

    /**
     * 系统设置-权限设置-新增一级菜单
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/firstLevel/add")
    public BaseResponse addFirstLevel(@RequestBody SysMenuAddParam param,
            HttpServletRequest request) {
        log.info("系统设置-权限设置-新增一级菜单, url:/sysMenu/FirstLevel/add, param:{}",
                JSONObject.toJSON(param));
        return sysMenuService.addFirstLevel(param, request);
    }

    /**
     * 系统设置-权限设置-新增二级菜单
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/secondLevel/add")
    public BaseResponse addSecondLevel(@RequestBody SysMenuAddParam param,
            HttpServletRequest request) {
        log.info("系统设置-权限设置-新增二级菜单, url:/sysMenu/secondLevel/add, param:{}",
                JSONObject.toJSON(param));
        return sysMenuService.addSecondLevel(param, request);
    }

    /**
     * 系统设置-权限设置-编辑菜单
     *
     * @param param
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody SysMenuEditParam param, HttpServletRequest request) {
        log.info("系统设置-权限设置-编辑菜单, url:/sysMenu/edit, param:{}",
                JSONObject.toJSON(param));
        return sysMenuService.edit(param, request);
    }

    /**
     * 系统设置-权限设置-删除菜单
     *
     * @param id
     * @param phone
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        log.info("系统设置-权限设置-删除菜单, url:/sysMenu/delete, id:{}, phone:{}", id, phone);
        return sysMenuService.delete(id, phone, request);
    }

}
