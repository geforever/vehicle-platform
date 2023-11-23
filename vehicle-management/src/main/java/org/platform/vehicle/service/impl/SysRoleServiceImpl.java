package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysMenuConstant;
import org.platform.vehicle.constant.SysRoleConstant;
import org.platform.vehicle.constant.SysUserConstant;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.SysMenu;
import org.platform.vehicle.entity.SysRole;
import org.platform.vehicle.entity.SysRoleMenu;
import org.platform.vehicle.entity.SysUserRole;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.SysRoleMapper;
import org.platform.vehicle.mapper.SysRoleMenuMapper;
import org.platform.vehicle.mapper.SysUserRoleMapper;
import org.platform.vehicle.param.SysRoleAddParam;
import org.platform.vehicle.param.SysRoleConditionQueryParam;
import org.platform.vehicle.param.SysRoleEditParam;
import org.platform.vehicle.service.SysMenuService;
import org.platform.vehicle.service.SysRoleMenuBaseService;
import org.platform.vehicle.service.SysRoleService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.vo.SysRoleDetailVo;
import org.platform.vehicle.vo.SysRoleVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author gejiawei
 * @Date 2023/8/25 09:07
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRoleServiceImpl implements SysRoleService {

    private final SysMenuService sysMenuService;
    private final SysRoleMapper sysRoleMapper;
    private final SysCustomerMapper sysCustomerMapper;
    private final SysRoleMenuBaseService sysRoleMenuBaseService;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final OperateLogAsync operateLogAsync;
    private final SysUserRoleMapper sysUserRoleMapper;

    /**
     * 系统设置-角色设置-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse conditionQuery(SysRoleConditionQueryParam param) {
        Page<SysRole> page = new Page<SysRole>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<SysRole> wrapper = this.getSysRoleLambdaQueryWrapper(param);
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(page, wrapper);
        List<SysRole> sysRoleList = sysRolePage.getRecords();
        List<SysCustomer> customerList = this.getCustomerList(sysRoleList);
        List<SysRoleVo> sysRoleVoList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {
            // sysRole转换为sysRoleVo
            SysRoleVo sysRoleVo = this.getSysRoleVo(sysRole, customerList);
            sysRoleVoList.add(sysRoleVo);
        }
        return new BasePageResponse(sysRoleVoList, page);
    }

    /**
     * 系统设置-角色设置-新增
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(SysRoleAddParam param, HttpServletRequest request) {
        UserVo user = UserContext.getUser();
        SysCustomer sysCustomer = sysCustomerMapper.selectById(param.getCustomerId());
        if (sysCustomer == null) {
            return BaseResponse.failure("客户不存在");
        }
        // 角色名称校验
        Long count = sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCustomerId, param.getCustomerId())
                .eq(SysRole::getName, param.getName())
                .eq(SysRole::getIsDelete, SysUserConstant.NOT_DELETED));
        if (count > 0) {
            return BaseResponse.failure("角色名称已存在");
        }
        SysRole sysRole = this.saveSysRole(param, user);
        this.saveRoleMenu(param.getMenuIdList(), sysRole);
        operateLogAsync.pushAddLog(
                sysRole,
                OperateModuleEnum.SYSTEM_ROLE_ADD,
                String.valueOf(sysRole.getId()),
                user,
                request);
        return BaseResponse.ok(sysRole);
    }

    /**
     * 系统设置-角色设置-修改
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(SysRoleEditParam param, HttpServletRequest request) {
        // 当前登录角色
        UserVo user = UserContext.getUser();
        SysRole sysRole = sysRoleMapper.selectById(param.getId());
        if (sysRole == null) {
            return BaseResponse.failure("角色不存在");
        }
        // 校验角色名称重复
        if (!param.getName().equals(sysRole.getName())) {
            // 角色名称校验
            Long count = sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getCustomerId, sysRole.getCustomerId())
                    .eq(SysRole::getName, param.getName())
                    .eq(SysRole::getIsDelete, SysUserConstant.NOT_DELETED));
            if (count > 0) {
                return BaseResponse.failure("角色名称已存在");
            }
        }
        SysRole newSysRole = this.updateRole(param, user);
        // 删除角色菜单
        sysRoleMenuBaseService.remove(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, param.getId()));
        // 保存角色菜单
        this.saveRoleMenu(param.getMenuIdList(), sysRole);
        // 保存日志
        operateLogAsync.pushEditLog(
                SysRole.class,
                sysRole,
                newSysRole,
                OperateModuleEnum.SYSTEM_ROLE_EDIT,
                String.valueOf(sysRole.getId()),
                user,
                request);
        return BaseResponse.ok();
    }

    /**
     * 系统设置-角色设置-删除
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
        // 用户不能删除自己的角色
        if (userVo.getRoleId().equals(id)) {
            return BaseResponse.failure("不能删除自己的角色");
        }
        // 验证手机号
        if (!userVo.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号错误，删除操作未执行");
        }
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (sysRole == null) {
            return BaseResponse.failure("角色不存在");
        }
        // 该角色被使用,无法删除
        Long count = sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, id));
        if (count > 0) {
            return BaseResponse.failure("该角色被使用,无法删除");
        }
        // 删除角色
        SysRole updateParam = new SysRole();
        updateParam.setId(id);
        updateParam.setIsDelete(SysRoleConstant.IS_DELETE);
        updateParam.setUpdatePerson(userVo.getName());
        sysRoleMapper.updateById(updateParam);
        // 保存日志
        operateLogAsync.pushDeleteLog(
                OperateModuleEnum.SYSTEM_ROLE_DELETE,
                String.valueOf(sysRole.getId()),
                userVo,
                request,
                sysRole.getName());
        return BaseResponse.ok();
    }

    /**
     * 系统设置-角色设置-查看角色权限
     *
     * @param roleId
     * @return
     */
    @Override
    public BaseResponse getRoleMenu(Integer roleId) {
        Set<SysMenu> menuTree = sysMenuService.getMenuTree(roleId,
                SysMenuConstant.DEFAULT_PARENT_ID);
        return BaseResponse.ok(menuTree);
    }

    /**
     * 系统设置-角色设置-查看角色详情
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse getRoleDetail(Integer id) {
        SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .eq(SysRole::getIsDelete, SysUserConstant.NOT_DELETED));
        if (sysRole == null) {
            return BaseResponse.failure("角色不存在");
        }
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, id));
        SysRoleDetailVo sysRoleDetailVo = this.getSysRoleDetailVo(sysRole, sysRoleMenuList);
        return BaseResponse.ok(sysRoleDetailVo);
    }

    /**
     * 根据customerId查询角色
     *
     * @param customerId
     * @return
     */
    @Override
    public BaseResponse<List<SysRoleVo>> getRoleByCustomerId(Integer customerId) {
        UserVo user = UserContext.getUser();
        if (!user.getCustomerIds().contains(customerId)) {
            return BaseResponse.failure("客户不正确");
        }
        List<SysRole> sysRoleList = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getCustomerId, customerId)
                        .eq(SysRole::getLevel, SysRoleConstant.CAN_MODIFY_LEVEL)
                        .eq(SysRole::getIsDelete, SysUserConstant.NOT_DELETED));
        List<SysCustomer> customerList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, customerId));
        List<SysRoleVo> sysRoleVoList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {
            // sysRole转换为sysRoleVo
            SysRoleVo sysRoleVo = this.getSysRoleVo(sysRole, customerList);
            sysRoleVoList.add(sysRoleVo);
        }
        return BaseResponse.ok(sysRoleVoList);
    }

    private SysRoleDetailVo getSysRoleDetailVo(SysRole sysRole, List<SysRoleMenu> sysRoleMenuList) {
        List<Integer> menuIdList = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
            menuIdList.add(sysRoleMenu.getMenuId());
        }
        SysRoleDetailVo sysRoleDetailVo = new SysRoleDetailVo();
        sysRoleDetailVo.setId(sysRole.getId());
        sysRoleDetailVo.setCustomerId(sysRole.getCustomerId());
        sysRoleDetailVo.setCustomerName(sysRole.getName());
        sysRoleDetailVo.setName(sysRole.getName());
        sysRoleDetailVo.setNickname(sysRole.getNickname());
        sysRoleDetailVo.setLevel(sysRole.getLevel());
        sysRoleDetailVo.setDescription(sysRole.getDescription());
        sysRoleDetailVo.setStatus(sysRole.getStatus());
        sysRoleDetailVo.setTemp(sysRole.getTemp());
        sysRoleDetailVo.setMenuIdList(menuIdList);
        return sysRoleDetailVo;
    }

    private SysRole updateRole(SysRoleEditParam param, UserVo user) {
        SysRole updateParam = new SysRole();
        updateParam.setId(param.getId());
        updateParam.setName(param.getName());
        updateParam.setNickname(param.getName());
        updateParam.setDescription(param.getDescription());
        updateParam.setStatus(param.getStatus());
        updateParam.setUpdatePerson(user.getName());
        sysRoleMapper.updateById(updateParam);
        return updateParam;
    }

    private void saveRoleMenu(List<Integer> menuIdList, SysRole sysRole) {
        // 保存角色菜单
        List<SysRoleMenu> batchInsertParam = new ArrayList<>();
        if (menuIdList != null && !menuIdList.isEmpty()) {
            for (Integer menuId : menuIdList) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getId());
                sysRoleMenu.setMenuId(menuId);
                batchInsertParam.add(sysRoleMenu);
            }
            sysRoleMenuBaseService.saveBatch(batchInsertParam);
        }
    }

    private SysRole saveSysRole(SysRoleAddParam param, UserVo user) {
        SysRole addParam = new SysRole();
        addParam.setCustomerId(param.getCustomerId());
        addParam.setName(param.getName());
        addParam.setNickname(param.getName());
        addParam.setLevel(param.getLevel());
        addParam.setDescription(param.getDescription());
        addParam.setCreatePerson(user.getName());
        addParam.setUpdatePerson(user.getName());
        sysRoleMapper.insert(addParam);
        return addParam;
    }

    private SysRoleVo getSysRoleVo(SysRole sysRole, List<SysCustomer> customerList) {
        SysCustomer sysCustomer = this.getSysCustomer(sysRole, customerList);
        SysRoleVo sysRoleVo = new SysRoleVo();
        sysRoleVo.setId(sysRole.getId());
        sysRoleVo.setCustomerId(sysRole.getCustomerId());
        sysRoleVo.setCustomerName(sysCustomer.getName());
        sysRoleVo.setName(sysRole.getName());
        sysRoleVo.setNickname(sysRole.getNickname());
        sysRoleVo.setLevel(sysRole.getLevel());
        sysRoleVo.setDescription(sysRole.getDescription());
        sysRoleVo.setStatus(sysRole.getStatus());
        sysRoleVo.setIsDelete(sysRole.getIsDelete());
        sysRoleVo.setCreateTime(sysRole.getCreateTime());
        sysRoleVo.setUpdateTime(sysRole.getUpdateTime());
        sysRoleVo.setCreatePerson(sysRole.getCreatePerson());
        sysRoleVo.setUpdatePerson(sysRole.getUpdatePerson());
        sysRoleVo.setTemp(sysRole.getTemp());
        return sysRoleVo;
    }

    private SysCustomer getSysCustomer(SysRole sysRole, List<SysCustomer> customerList) {
        SysCustomer sysCustomer = null;
        for (SysCustomer temp : customerList) {
            if (sysRole.getCustomerId().equals(temp.getId())) {
                sysCustomer = temp;
                break;
            }
        }
        return sysCustomer;
    }

    private List<SysCustomer> getCustomerList(List<SysRole> sysRoleList) {
        List<Integer> customerIdList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {
            customerIdList.add(sysRole.getCustomerId());
        }
        if (customerIdList.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .in(SysCustomer::getId, customerIdList));
        return sysCustomerList;
    }


    private LambdaQueryWrapper<SysRole> getSysRoleLambdaQueryWrapper(
            SysRoleConditionQueryParam param) {
        UserVo user = UserContext.getUser();
        // 当前用户属于客户,则查询该客户下所有车队
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getName())) {
            wrapper.like(SysRole::getName, param.getName());
        }
        if (param.getCustomerId() != null) {
            wrapper.eq(SysRole::getCustomerId, param.getCustomerId());
        }
        wrapper.in(SysRole::getCustomerId, user.getCustomerIds());
        wrapper.eq(SysRole::getLevel, SysRoleConstant.CAN_MODIFY_LEVEL);
        wrapper.eq(SysRole::getIsDelete, SysUserConstant.NOT_DELETED);
        wrapper.orderByDesc(SysRole::getUpdateTime);
        return wrapper;
    }
}
