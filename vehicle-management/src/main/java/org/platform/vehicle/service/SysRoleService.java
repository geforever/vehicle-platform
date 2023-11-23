package org.platform.vehicle.service;

import org.platform.vehicle.entity.SysMenu;
import org.platform.vehicle.param.SysRoleAddParam;
import org.platform.vehicle.param.SysRoleConditionQueryParam;
import org.platform.vehicle.param.SysRoleEditParam;
import org.platform.vehicle.vo.SysRoleDetailVo;
import org.platform.vehicle.vo.SysRoleVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author gejiawei
 * @Date 2023/8/25 09:06
 */
public interface SysRoleService {

    /**
     * 系统设置-角色设置-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<SysRoleVo>> conditionQuery(SysRoleConditionQueryParam param);

    /**
     * 系统设置-角色设置-新增
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse add(SysRoleAddParam param, HttpServletRequest request);

    /**
     * 系统设置-角色设置-修改
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse edit(SysRoleEditParam param, HttpServletRequest request);

    /**
     * 系统设置-角色设置-删除
     *
     * @param id
     * @param phone
     * @param request
     * @return
     */
    BaseResponse delete(Integer id, String phone, HttpServletRequest request);

    /**
     * 系统设置-角色设置-查看角色权限
     *
     * @param roleId
     * @return
     */
    BaseResponse<Set<SysMenu>> getRoleMenu(Integer roleId);

    /**
     * 系统设置-角色设置-查看角色详情
     *
     * @param id
     * @return
     */
    BaseResponse<SysRoleDetailVo> getRoleDetail(Integer id);

    /**
     * 根据customerId查询角色
     *
     * @param customerId
     * @return
     */
    BaseResponse<List<SysRoleVo>> getRoleByCustomerId(Integer customerId);
}
