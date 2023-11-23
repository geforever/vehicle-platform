package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysMenuConstant;
import org.platform.vehicle.constant.SysRoleConstant;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.SysMenu;
import org.platform.vehicle.entity.SysRoleMenu;
import org.platform.vehicle.entity.SysUserRole;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.SysMenuMapper;
import org.platform.vehicle.mapper.SysRoleMenuMapper;
import org.platform.vehicle.mapper.SysUserRoleMapper;
import org.platform.vehicle.param.SysMenuAddParam;
import org.platform.vehicle.param.SysMenuEditParam;
import org.platform.vehicle.service.SysMenuService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.vo.SysMenuDetailVO;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/8/24 15:56
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final OperateLogAsync operateLogAsync;
    private final SysCustomerMapper sysCustomerMapper;

    /**
     * 系统设置-权限设置-新增一级菜单
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    public BaseResponse addFirstLevel(SysMenuAddParam param, HttpServletRequest request) {
        // 获取当前操作人
        UserVo userVo = UserContext.getUser();
        Integer sort = param.getSort();
        if (sort == null) {
            //根据parentId查询同级菜单数量
            sort = this.getSortId(param.getParentId());
        }
        // 校验菜单名称是否重复
        Long count = sysMenuMapper.selectCount(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getName, param.getName())
                .eq(SysMenu::getType, SysMenuConstant.TYPE_PAGE)
                .eq(SysMenu::getIsDelete, SysMenuConstant.NOT_DELETE));
        if (count > 0) {
            return BaseResponse.failure("菜单名称重复");
        }
        this.saveMenu(param,
                userVo,
                SysMenuConstant.VEHICLE_MANAGEMENT_PARENT_ID,
                SysMenuConstant.TYPE_PAGE,
                sort,
                request);
        return BaseResponse.ok();
    }

    private void saveMenu(SysMenuAddParam param, UserVo userVo, Integer parentId,
            Integer type, Integer sort, HttpServletRequest request) {
        SysMenu insertParam = new SysMenu();
        insertParam.setParentId(parentId);
        insertParam.setName(param.getName());
        insertParam.setCode(param.getCode());
        insertParam.setUrl(param.getUrl());
        insertParam.setType(type);
        insertParam.setSort(sort);
        insertParam.setStatus(param.getStatus());
        insertParam.setCreatePerson(userVo.getName());
        insertParam.setUpdatePerson(userVo.getName());
        sysMenuMapper.insert(insertParam);
        operateLogAsync.pushAddLog(
                insertParam,
                OperateModuleEnum.SYSTEM_MENU_ADD,
                String.valueOf(insertParam.getId()),
                userVo,
                request);
    }

    /**
     * 系统设置-权限设置-新增二级菜单
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    public BaseResponse addSecondLevel(SysMenuAddParam param, HttpServletRequest request) {
        // 获取当前操作人
        UserVo userVo = UserContext.getUser();
        Integer sort = param.getSort();
        // 排序为空则默认当前同级菜单数量+1
        if (sort == null) {
            //根据parentId查询同级菜单数量
            sort = this.getSortId(param.getParentId());
        }
        // 菜单名称重复校验
        Long count = sysMenuMapper.selectCount(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getName, param.getName())
                .eq(SysMenu::getType, SysMenuConstant.TYPE_PAGE)
                .eq(SysMenu::getIsDelete, SysMenuConstant.NOT_DELETE));
        if (count > 0) {
            return BaseResponse.failure("菜单名称重复");
        }
        this.saveMenu(param,
                userVo,
                param.getParentId(),
                param.getType(),
                sort,
                request);
        return BaseResponse.ok();
    }

    /**
     * 系统设置-权限设置-编辑菜单
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    public BaseResponse edit(SysMenuEditParam param, HttpServletRequest request) {
        UserVo userVo = UserContext.getUser();
        SysMenu sysMenu = sysMenuMapper.selectById(param.getId());
        if (sysMenu == null) {
            return BaseResponse.failure("菜单不存在");
        }
        Integer sort = param.getSort();
        // 排序为空则默认当前同级菜单数量+1
        if (sort == null) {
            //根据parentId查询同级菜单数量
            sort = getSortId(param.getParentId());
        }
        // 菜单名称重复校验
        Long count = sysMenuMapper.selectCount(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getName, param.getName())
                .ne(SysMenu::getId, param.getId())
                .eq(SysMenu::getType, SysMenuConstant.TYPE_PAGE)
                .eq(SysMenu::getIsDelete, SysMenuConstant.NOT_DELETE));
        if (count > 0) {
            return BaseResponse.failure("菜单名称重复");
        }
        SysMenu updateParam = new SysMenu();
        updateParam.setId(param.getId());
        updateParam.setParentId(param.getParentId());
        updateParam.setName(param.getName());
        updateParam.setCode(param.getCode());
        updateParam.setUrl(param.getUrl());
        updateParam.setType(param.getType());
        updateParam.setSort(sort);
        updateParam.setStatus(param.getStatus());
        updateParam.setUpdatePerson(userVo.getName());
        Wrapper<SysMenu> updateWrapper = new UpdateWrapper<SysMenu>()
                .eq("id", param.getId())
                .set("parent_id", param.getParentId())
                .set("name", param.getName())
                .set("code", param.getCode())
                .set("url", param.getUrl())
                .set("type", param.getType())
                .set("sort", sort)
                .set("status", param.getStatus())
                .set("update_person", userVo.getName());
        sysMenuMapper.update(updateParam, updateWrapper);
        operateLogAsync.pushEditLog(
                SysMenu.class,
                sysMenu,
                updateParam,
                OperateModuleEnum.SYSTEM_MENU_EDIT,
                String.valueOf(updateParam.getId()),
                userVo,
                request);
        return BaseResponse.ok();
    }

    private Integer getSortId(Integer parentId) {
        Long count = sysMenuMapper.selectCount(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId));
        return Math.toIntExact(count) + 1;
    }

    /**
     * 系统设置-权限设置-删除菜单
     *
     * @param id
     * @param phone
     * @param request
     * @return
     */
    @Override
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        // 获取登录人信息
        UserVo userVo = UserContext.getUser();
        // 验证手机号
        if (!userVo.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号错误，删除操作未执行");
        }
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            return BaseResponse.failure("菜单不存在");
        }
        SysMenu updateParam = new SysMenu();
        updateParam.setId(id);
        sysMenuMapper.deleteById(updateParam);
        // 删除所有parentId为id的菜单
        sysMenuMapper.delete(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, id));
        operateLogAsync.pushDeleteLog(
                OperateModuleEnum.SYSTEM_MENU_DELETE,
                String.valueOf(sysMenu.getId()),
                userVo,
                request,
                sysMenu.getName());
        return BaseResponse.ok();
    }

    /**
     * 系统设置-权限设置-获取菜单树
     *
     * @return
     */
    @Override
    public BaseResponse getTree() {
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getIsDelete, SysMenuConstant.NOT_DELETE));
        Set<SysMenu> parentMenu = new TreeSet<>();
        for (SysMenu sysMenu : sysMenuList) {
            // 角色类型和菜单匹配
            if (sysMenu.getParentId() == SysMenuConstant.VEHICLE_MANAGEMENT_PARENT_ID) {
                parentMenu.add(sysMenu);
            }
        }
        this.processMenuTree(sysMenuList, parentMenu);
        return BaseResponse.ok(parentMenu);
    }

    /**
     * 系统设置-权限设置-获取所有菜单数(包含小程序)
     *
     * @return
     */
    @Override
    public BaseResponse getAllTree() {
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getIsDelete, SysMenuConstant.NOT_DELETE));
        Set<SysMenu> parentMenu = new TreeSet<>();
        for (SysMenu sysMenu : sysMenuList) {
            // 角色类型和菜单匹配
            if (sysMenu.getParentId() == SysMenuConstant.DEFAULT_PARENT_ID) {
                parentMenu.add(sysMenu);
            }
        }
        this.processMenuTree(sysMenuList, parentMenu);
        return BaseResponse.ok(parentMenu);
    }

    /**
     * 获取当前用户菜单树
     *
     * @return
     */
    @Override
    public BaseResponse getCurrentUserMenuTree() {
        // 获取当前登录用户
        UserVo user = UserContext.getUser();
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, user.getUserId()));
        if (sysUserRole == null) {
            return BaseResponse.failure("用户没有权限");
        }
        Set<SysMenu> menuTree = this.getMenuTree(sysUserRole.getRoleId(),
                SysMenuConstant.DEFAULT_PARENT_ID);
        return BaseResponse.ok(menuTree);
    }

    /**
     * 获取菜单树
     *
     * @param roleId
     * @param parentId
     * @return
     */
    public Set<SysMenu> getMenuTree(Integer roleId, Integer parentId) {
        // 获取菜单
        List<SysMenu> sysMenuList = this.getSysMenus(roleId, parentId);
        Set<SysMenu> parentMenuSet = new TreeSet<>();
        for (SysMenu sysMenu : sysMenuList) {
            // 角色类型和菜单匹配
            if (sysMenu.getParentId().equals(parentId)) {
                parentMenuSet.add(sysMenu);
            }
        }
        //生成菜单树
        this.processMenuTree(sysMenuList, parentMenuSet);
        return parentMenuSet;
    }

    /**
     * 系统设置-权限设置-获取菜单详情
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse<SysMenuDetailVO> getDetailMenu(Integer id) {
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            return BaseResponse.failure("菜单不存在");
        }
        SysMenuDetailVO sysMenuDetailVO = this.getSysMenuDetailVO(sysMenu);
        return BaseResponse.ok(sysMenuDetailVO);
    }

    /**
     * 根据客户id获取菜单树(客户新增)
     *
     * @return
     */
    @Override
    public BaseResponse<Set<SysMenu>> getClientMenuTree(Integer clientId) {
        SysCustomer sysCustomer = sysCustomerMapper.selectById(clientId);
        if (sysCustomer == null) {
            return BaseResponse.failure("客户不存在");
        }
        SysCustomer client = sysCustomerMapper.selectById(sysCustomer.getCompanyId());
        Integer adminUserId = client.getAdminUserId();
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, adminUserId));
        Set<SysMenu> menuTree = this.getMenuTree(sysUserRole.getRoleId(),
                SysMenuConstant.DEFAULT_PARENT_ID);
        return BaseResponse.ok(menuTree);
    }

    private SysMenuDetailVO getSysMenuDetailVO(SysMenu sysMenu) {
        // 查询父菜单
        SysMenu parentSysMenu = sysMenuMapper.selectById(sysMenu.getParentId());
        SysMenuDetailVO sysMenuDetailVO = new SysMenuDetailVO();
        sysMenuDetailVO.setId(sysMenu.getId());
        sysMenuDetailVO.setName(sysMenu.getName());
        if (parentSysMenu != null) {
            sysMenuDetailVO.setParentId(parentSysMenu.getId());
            sysMenuDetailVO.setParentName(parentSysMenu.getName());
        }
        sysMenuDetailVO.setCode(sysMenu.getCode());
        sysMenuDetailVO.setUrl(sysMenu.getUrl());
        sysMenuDetailVO.setType(sysMenu.getType());
        sysMenuDetailVO.setSort(sysMenu.getSort());
        sysMenuDetailVO.setStatus(sysMenu.getStatus());
        return sysMenuDetailVO;
    }

    /**
     * 获取菜单
     *
     * @param roleId
     * @param parentId
     * @return
     */
    private List<SysMenu> getSysMenus(Integer roleId, Integer parentId) {
        List<SysMenu> sysMenuList = null;
        // 超级管理员查询所有菜单
        if (roleId.equals(SysRoleConstant.DEFAULT_ADMIN_ROLE_ID)) {
            sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getIsDelete, SysMenuConstant.NOT_DELETE));
        } else {
            // 根据角色查询菜单
            List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(
                    new LambdaQueryWrapper<SysRoleMenu>()
                            .eq(SysRoleMenu::getRoleId, roleId));
            List<Integer> menuIdList = new ArrayList<>();
            for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
                menuIdList.add(sysRoleMenu.getMenuId());
            }
            // 查询角色对应的菜单
            sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .in(SysMenu::getId, menuIdList)
                    .eq(SysMenu::getIsDelete, SysMenuConstant.NOT_DELETE));
        }
        return sysMenuList;
    }

    private void processMenuTree(List<SysMenu> sysMenuList, Set<SysMenu> parentMenu) {
        for (SysMenu parent : parentMenu) {
            Integer parentId = parent.getId();
            // 去除重复子菜单
            LinkedHashSet<SysMenu> sysMenuHashSet = new LinkedHashSet<>();
            // 遍历获取所有菜单中，父id为sysMenuId的菜单
            for (SysMenu child : sysMenuList) {
                if (parentId.equals(child.getParentId())) {
                    sysMenuHashSet.add(child);
                }
            }
            if (!sysMenuHashSet.isEmpty()) {
                parent.getChildren().addAll(sysMenuHashSet);
            }
            if (parent.getChildren().isEmpty()) {
                continue;
            }
            processMenuTree(sysMenuList, parent.getChildren());
        }
    }
}
