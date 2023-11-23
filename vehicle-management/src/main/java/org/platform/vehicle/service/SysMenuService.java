package org.platform.vehicle.service;

import org.platform.vehicle.entity.SysMenu;
import org.platform.vehicle.param.SysMenuAddParam;
import org.platform.vehicle.param.SysMenuEditParam;
import org.platform.vehicle.vo.SysMenuDetailVO;
import org.platform.vehicle.response.BaseResponse;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author gejiawei
 * @Date 2023/8/24 15:56
 */
public interface SysMenuService {

    /**
     * 系统设置-权限设置-新增一级菜单
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse addFirstLevel(SysMenuAddParam param, HttpServletRequest request);

    /**
     * 系统设置-权限设置-新增二级菜单
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse addSecondLevel(SysMenuAddParam param, HttpServletRequest request);

    /**
     * 系统设置-权限设置-编辑菜单
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse edit(SysMenuEditParam param, HttpServletRequest request);

    /**
     * 系统设置-权限设置-删除菜单
     *
     * @param id
     * @param phone
     * @param request
     * @return
     */
    BaseResponse delete(Integer id, String phone, HttpServletRequest request);

    /**
     * 系统设置-权限设置-获取菜单树
     *
     * @return
     */
    BaseResponse<Set<SysMenu>> getTree();

    /**
     * 系统设置-权限设置-获取所有菜单数(包含小程序)
     *
     * @return
     */
    BaseResponse<Set<SysMenu>> getAllTree();

    /**
     * 获取当前用户菜单树
     *
     * @return
     */
    BaseResponse<Set<SysMenu>> getCurrentUserMenuTree();

    /**
     * 获取菜单树
     *
     * @param roleId
     * @param parentId
     * @return
     */
    Set<SysMenu> getMenuTree(Integer roleId, Integer parentId);

    /**
     * 系统设置-权限设置-获取菜单详情
     *
     * @param id
     * @return
     */
    BaseResponse<SysMenuDetailVO> getDetailMenu(Integer id);

    /**
     * 根据客户id获取菜单树(客户新增)
     *
     * @return
     */
    BaseResponse<Set<SysMenu>> getClientMenuTree(Integer clientId);
}
