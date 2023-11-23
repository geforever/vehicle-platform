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
import org.platform.vehicle.param.SysUserAddParam;
import org.platform.vehicle.param.SysUserConditionQueryParam;
import org.platform.vehicle.param.SysUserEditParam;
import org.platform.vehicle.service.SysUserService;
import org.platform.vehicle.vo.SysUserDetailVo;
import org.platform.vehicle.vo.SysUserVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统设置-用户设置
 *
 * @Author gejiawei
 * @Date 2023/8/23 17:13
 */
@Slf4j
@RestController
@RequestMapping("/sysUser")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * 系统设置-用户设置-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/conditionQuery")
    public BasePageResponse<List<SysUserVo>> conditionQuery(
            @RequestBody SysUserConditionQueryParam param) {
        log.info("系统设置-用户设置-条件查询, url:/sysUser/conditionQuery, param:{}",
                JSONObject.toJSON(param));
        return sysUserService.conditionQuery(param);
    }

    /**
     * 系统设置-用户设置-详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<SysUserDetailVo> getSysUserDetail(@PathVariable Integer id) {
        log.info("系统设置-用户设置-详情, url:/sysUser/detail, id:{}", id);
        return sysUserService.getSysUserDetail(id);
    }

    /**
     * 系统设置-用户设置-新增账号
     *
     * @param param
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/add")
    public BaseResponse add(@RequestBody SysUserAddParam param, HttpServletRequest request) {
        log.info("系统设置-用户设置-新增账号, url:/sysUser/add, param:{}",
                JSONObject.toJSON(param));
        return sysUserService.add(param, request);
    }

    /**
     * 系统设置-用户设置-修改账号
     *
     * @param param
     * @return
     */
    @RepeatSubmit(lockField = "id")
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody SysUserEditParam param, HttpServletRequest request) {
        log.info("系统设置-用户设置-修改账号, url:/sysUser/edit, param:{}",
                JSONObject.toJSON(param));
        return sysUserService.edit(param, request);
    }

    /**
     * 系统设置-用户设置-删除账号
     *
     * @param id
     * @param phone
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        log.info("系统设置-用户设置-删除账号, url:/sysUser/delete, userId:{}, phone:{}", id, phone);
        return sysUserService.delete(id, phone, request);
    }

    /**
     * 系统设置-用户设置-重置密码
     *
     * @param id
     * @return
     */
    @PutMapping("/resetPassword")
    public BaseResponse resetPassword(Integer id, HttpServletRequest request) {
        log.info("系统设置-用户设置-重置密码, url:/sysUser/resetPassword, userId:{}", id);
        return sysUserService.resetPassword(id, request);
    }
}
