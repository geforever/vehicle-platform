package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysUserConstant;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.SysCustomerUser;
import org.platform.vehicle.entity.SysRole;
import org.platform.vehicle.entity.SysUser;
import org.platform.vehicle.entity.SysUserRole;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.SysCustomerUserMapper;
import org.platform.vehicle.mapper.SysRoleMapper;
import org.platform.vehicle.mapper.SysUserMapper;
import org.platform.vehicle.mapper.SysUserRoleMapper;
import org.platform.vehicle.param.SysUserAddParam;
import org.platform.vehicle.param.SysUserConditionQueryParam;
import org.platform.vehicle.param.SysUserEditParam;
import org.platform.vehicle.service.SysUserService;
import org.platform.vehicle.util.OauthUtil;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.vo.SysUserDetailVo;
import org.platform.vehicle.vo.SysUserVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.Md5Utils;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author gejiawei
 * @Date 2023/8/23 17:20
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysCustomerMapper sysCustomerMapper;
    private final SysCustomerUserMapper sysCustomerUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final OperateLogAsync operateLogAsync;
    private final OauthUtil oauthUtil;

    /**
     * 系统设置-用户设置-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse conditionQuery(SysUserConditionQueryParam param) {
        Page<SysUser> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<SysUser> wrapper = this.getSysUserQueryWrapper(param);
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(page, wrapper);
        List<SysUser> sysUserList = sysUserPage.getRecords();
        List<Integer> sysUserIdList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            sysUserIdList.add(sysUser.getId());
        }
        if (sysUserIdList.isEmpty()) {
            return BasePageResponse.ok(new ArrayList<>(), page);
        }
        List<SysCustomerUser> sysCustomerUserList = sysCustomerUserMapper.selectList(
                new LambdaQueryWrapper<SysCustomerUser>()
                        .in(SysCustomerUser::getUserId, sysUserIdList));
        List<Integer> sysCustomerIdList = new ArrayList<>();
        for (SysCustomerUser sysCustomerUser : sysCustomerUserList) {
            sysCustomerIdList.add(sysCustomerUser.getCustomerId());
        }
        List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .in(SysCustomer::getId, sysCustomerIdList));
        // 查询用户角色
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .in(SysUserRole::getUserId, sysUserIdList));
        List<Integer> sysRoleIdList = new ArrayList<>();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            sysRoleIdList.add(sysUserRole.getRoleId());
        }
        List<SysRole> sysRoleList = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .in(SysRole::getId, sysRoleIdList));
        List<SysUserVo> sysUserVoList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            // 获取客户信息
            SysCustomer customer = this.getCustomer(sysUser, sysCustomerUserList, sysCustomerList);
            // 获取角色信息
            SysRole sysRole = this.getSysRole(sysUser, sysUserRoleList, sysRoleList);
            SysUserVo sysUserVo = this.getSysUserVo(sysUser, customer, sysRole);
            sysUserVoList.add(sysUserVo);
        }
        return BasePageResponse.ok(sysUserVoList, page);
    }

    private SysRole getSysRole(SysUser sysUser, List<SysUserRole> sysUserRoleList,
            List<SysRole> sysRoleList) {
        SysRole sysRole = null;
        for (SysUserRole sysUserRole : sysUserRoleList) {
            if (sysUserRole.getUserId().equals(sysUser.getId())) {
                for (SysRole role : sysRoleList) {
                    if (sysUserRole.getRoleId().equals(role.getId())) {
                        sysRole = role;
                        break;
                    }
                }
                break;
            }
        }
        return sysRole;
    }

    private SysCustomer getCustomer(SysUser sysUser, List<SysCustomerUser> sysCustomerUserList,
            List<SysCustomer> sysCustomerList) {
        SysCustomer sysCustomer = null;
        for (SysCustomerUser sysCustomerUser : sysCustomerUserList) {
            if (sysCustomerUser.getUserId().equals(sysUser.getId())) {
                for (SysCustomer customer : sysCustomerList) {
                    if (sysCustomerUser.getCustomerId().equals(customer.getId())) {
                        sysCustomer = customer;
                        break;
                    }
                }
                break;
            }
        }
        return sysCustomer;
    }

    /**
     * 系统设置-用户设置-新增账号
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(SysUserAddParam param, HttpServletRequest request) {
        // 当前操作人
        UserVo user = UserContext.getUser();
        // 账号重复校验
        Long checkAccountRepeat = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getAccount, param.getAccount())
                .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
        if (checkAccountRepeat > 0) {
            return BaseResponse.failure("账号重复");
        }
        // 手机号重复校验
        if (StringUtils.isNotBlank(param.getPhone())) {
            Long checkPhoneRepeat = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getPhone, param.getPhone())
                    .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
            if (checkPhoneRepeat > 0) {
                return BaseResponse.failure("手机号重复");
            }
        }
        // 角色校验
        SysRole sysRole = sysRoleMapper.selectById(param.getRoleId());
        if (sysRole == null) {
            return BaseResponse.failure("角色不存在");
        }
        Long count = sysUserMapper.selectCount(new LambdaQueryWrapper<>());
        // 校验customerID
        SysCustomer sysCustomer = sysCustomerMapper.selectById(param.getCustomerId());
        if (sysCustomer == null) {
            return BaseResponse.failure("客户不存在");
        }
        // 密码加密
        String password = param.getPassword();
        if (StringUtils.isBlank(param.getPassword())) {
            password = SysUserConstant.DEFAULT_PASSWORD;
        }
        String passwordMd5 = Md5Utils.getMd5(password);
        // 保存用户信息
        String code = "";
        // 面心科技默认customerId = 1
        if (param.getCustomerId() == 1) {
            code = SysUserConstant.DEFAULT_USER_CODE;
        } else {
            code = SysUserConstant.CUSTOMER_DEFAULT_USER_CODE;
        }
        code = code + count;
        SysUser sysUser = this.saveSysUser(code, param, passwordMd5, user);
        // 保存用户客户关联信息
        this.saveCustomerUser(sysCustomer, sysUser);
        operateLogAsync.pushAddLog(
                sysUser,
                OperateModuleEnum.SYSTEM_USER_ADD,
                String.valueOf(sysUser.getId()),
                user,
                request);
        return BaseResponse.ok(sysUser);
    }

    /**
     * 系统设置-用户设置-修改账号
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(SysUserEditParam param, HttpServletRequest request) {
        UserVo user = UserContext.getUser();
        SysCustomer sysCustomer = sysCustomerMapper.selectById(param.getCustomerId());
        if (sysCustomer == null) {
            return BaseResponse.failure("客户不存在");
        }
        // 查询用户信息
        SysUser sysUser = sysUserMapper.selectById(param.getId());
        if (sysUser == null) {
            return BaseResponse.failure("用户不存在");
        }
        // 账号重复校验
        if (!param.getAccount().equals(sysUser.getAccount())) {
            Long checkAccountRepeat = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getAccount, param.getAccount())
                    .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
            if (checkAccountRepeat > 0) {
                return BaseResponse.failure("账号重复");
            }
        }
        if (StringUtils.isNotBlank(param.getPhone()) &&
                !param.getPhone().equals(sysUser.getPhone())) {
            // 手机号重复校验
            Long checkPhoneRepeat = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getPhone, param.getPhone())
                    .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
            if (checkPhoneRepeat > 0) {
                return BaseResponse.failure("手机号重复");
            }
        }
        SysUser newSysUser = this.editSysUser(param, sysUser, user);
        this.editSysCustomerUser(sysUser, sysCustomer);
        this.editRole(param, sysUser);
        operateLogAsync.pushEditLog(
                SysUser.class,
                sysUser,
                newSysUser,
                OperateModuleEnum.SYSTEM_USER_EDIT,
                String.valueOf(param.getId()),
                user,
                request);
        return BaseResponse.ok();
    }

    private void editSysCustomerUser(SysUser sysUser, SysCustomer sysCustomer) {
        // 解除原先的客户用户绑定关系
        sysCustomerUserMapper.delete(new LambdaQueryWrapper<SysCustomerUser>()
                .eq(SysCustomerUser::getUserId, sysUser.getId()));
        // 保存用户客户关联信息
        this.saveCustomerUser(sysCustomer, sysUser);
    }

    private void editRole(SysUserEditParam param, SysUser sysUser) {
        // 删除原用户角色
        this.deleteUserRole(sysUser);
        // 保存用户角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getId());
        sysUserRole.setRoleId(param.getRoleId());
        sysUserRoleMapper.insert(sysUserRole);
    }

    private SysUser editSysUser(SysUserEditParam param, SysUser sysUser, UserVo user) {
        SysUser updateParam = new SysUser();
        updateParam.setId(sysUser.getId());
        updateParam.setAccount(param.getAccount());
        updateParam.setPhone(StringUtils.isNotBlank(param.getPhone()) ? param.getPhone() : "");
        updateParam.setName(param.getName());
        updateParam.setStatus(param.getStatus());
        updateParam.setUpdatePerson(user.getName());
        sysUserMapper.updateById(updateParam);
        return updateParam;
    }

    /**
     * 系统设置-用户设置-删除账号
     *
     * @param userId
     * @param phone
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(Integer userId, String phone, HttpServletRequest request) {
        // 获取登录人信息
        UserVo userVo = UserContext.getUser();
        // 判断删除账号是否是操作账号
        if (userVo.getUserId().equals(userId)) {
            return BaseResponse.failure("不能删除自己的账号");
        }
        // 验证手机号
        if (!userVo.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号错误，删除操作未执行");
        }
        // 查询用户信息
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            return BaseResponse.failure("用户不存在");
        }
        // 删除用户角色管理表
        this.deleteUserRole(sysUser);
        // 删除用户
        this.deleteUser(sysUser, userVo);
        // 删除redis用户缓存
        oauthUtil.deleteUserCache(sysUser.getId());
        // 保存日志
        operateLogAsync.pushDeleteLog(
                OperateModuleEnum.SYSTEM_USER_DELETE,
                String.valueOf(sysUser.getId()),
                userVo,
                request,
                sysUser.getAccount());
        return BaseResponse.ok();
    }

    private void deleteUserRole(SysUser sysUser) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, sysUser.getId()));
    }

    private void deleteUser(SysUser sysUser, UserVo userVo) {
        SysUser deleteParam = new SysUser();
        deleteParam.setId(sysUser.getId());
        deleteParam.setIsDelete(SysUserConstant.IS_DELETED);
        deleteParam.setUpdatePerson(userVo.getName());
        sysUserMapper.updateById(deleteParam);
    }

    /**
     * 系统设置-用户设置-删除账号
     *
     * @param account
     * @param request
     * @return
     */
    @Override
    public int delete(String account, HttpServletRequest request) {
        // 查询用户信息
        SysUser sysUser = this.sysUserMapper.selectOne(
                new QueryWrapper<SysUser>().eq("account", account));
        if (sysUser == null) {
            return 0;
        }
        // 删除用户
        SysUser deleteParam = new SysUser();
        deleteParam.setId(sysUser.getId());
        deleteParam.setIsDelete(SysUserConstant.IS_DELETED);
        deleteParam.setUpdatePerson(UserContext.getUser().getName());
        int x = this.sysUserMapper.updateById(deleteParam);
        // 保存日志
        this.operateLogAsync.pushDeleteLog(
                OperateModuleEnum.SYSTEM_USER_DELETE,
                String.valueOf(sysUser.getId()),
                UserContext.getUser(),
                request,
                sysUser.getAccount());
        return x;
    }

    /**
     * 系统设置-用户设置-详情
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse getSysUserDetail(Integer id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            return BaseResponse.failure("用户不存在");
        }
        SysCustomer sysCustomer = null;
        SysRole sysRole = null;
        // 查询客户
        SysCustomerUser sysCustomerUser = sysCustomerUserMapper.selectOne(
                new LambdaQueryWrapper<SysCustomerUser>()
                        .eq(SysCustomerUser::getUserId, sysUser.getId()));
        if (sysCustomerUser != null) {
            sysCustomer = sysCustomerMapper.selectById(sysCustomerUser.getCustomerId());
        }
        // 查询角色
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, sysUser.getId()));
        if (sysUserRole != null) {
            sysRole = sysRoleMapper.selectById(sysUserRole.getRoleId());
        }
        SysUserDetailVo sysUserDetailVo = this.getSysUserDetailVo(sysUser, sysCustomer,
                sysRole);
        return BaseResponse.ok(sysUserDetailVo);
    }

    /**
     * 系统设置-用户设置-重置密码
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse resetPassword(Integer id, HttpServletRequest request) {
        // 获取登录人信息
        UserVo userVo = UserContext.getUser();
        // 查询用户信息
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            return BaseResponse.failure("用户不存在");
        }
        // 密码加密
        String passwordMd5 = Md5Utils.getMd5(SysUserConstant.DEFAULT_PASSWORD);
        // 重置密码
        SysUser updateParam = new SysUser();
        updateParam.setId(sysUser.getId());
        updateParam.setPassword(passwordMd5);
        updateParam.setUpdatePerson(userVo.getName());
        sysUserMapper.updateById(updateParam);
        // 保存日志
        operateLogAsync.pushEditLog(
                SysUser.class,
                sysUser,
                updateParam,
                OperateModuleEnum.SYSTEM_USER_RESET_PASSWORD,
                String.valueOf(sysUser.getId()),
                userVo,
                request);
        return BaseResponse.ok();
    }

    private SysUserDetailVo getSysUserDetailVo(SysUser sysUser, SysCustomer sysCustomer,
            SysRole sysRole) {
        SysUserDetailVo sysUserDetailVo = new SysUserDetailVo();
        sysUserDetailVo.setId(sysUser.getId());
        if (sysCustomer != null) {
            sysUserDetailVo.setCustomerId(sysCustomer.getId());
            sysUserDetailVo.setCustomerName(sysCustomer.getName());
        }
        sysUserDetailVo.setCode(sysUser.getCode());
        sysUserDetailVo.setAccount(sysUser.getAccount());
        sysUserDetailVo.setPhone(sysUser.getPhone());
        sysUserDetailVo.setName(sysUser.getName());
        sysUserDetailVo.setStatus(sysUser.getStatus());
        sysUserDetailVo.setType(sysUser.getType());
        if (sysRole != null) {
            sysUserDetailVo.setRoleId(sysRole.getId());
            sysUserDetailVo.setRoleName(sysRole.getName());
        }
        return sysUserDetailVo;
    }

    private void saveCustomerUser(SysCustomer sysCustomer, SysUser sysUser) {
        SysCustomerUser addParam = new SysCustomerUser();
        addParam.setCustomerId(sysCustomer.getId());
        addParam.setUserId(sysUser.getId());
        sysCustomerUserMapper.insert(addParam);
    }

    private SysUser saveSysUser(String code, SysUserAddParam param, String passwordMd5,
            UserVo user) {
        SysUser addParam = new SysUser();
        addParam.setCode(code);
        addParam.setAccount(param.getAccount());
        addParam.setPassword(passwordMd5);
        addParam.setPhone(param.getPhone());
        addParam.setName(param.getName());
        addParam.setType(param.getType());
        addParam.setCreatePerson(user.getName());
        addParam.setUpdatePerson(user.getName());
        sysUserMapper.insert(addParam);
        // 保存用户角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(addParam.getId());
        sysUserRole.setRoleId(param.getRoleId());
        sysUserRoleMapper.insert(sysUserRole);
        return addParam;
    }

    private LambdaQueryWrapper<SysUser> getSysUserQueryWrapper(SysUserConditionQueryParam param) {
        UserVo user = UserContext.getUser();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getAccount())) {
            wrapper.like(SysUser::getAccount, param.getAccount());
        }
        if (StringUtils.isNotBlank(param.getName())) {
            wrapper.like(SysUser::getName, param.getName());
        }
        List<Integer> customerIdList = new ArrayList<>();
        if (param.getCustomerIdList() != null && !param.getCustomerIdList().isEmpty()) {
            customerIdList = param.getCustomerIdList();
        } else {
            customerIdList = user.getCustomerIds();
        }
        // 根据customerId查询所有userId
        List<SysCustomerUser> sysCustomerUserList = sysCustomerUserMapper.selectList(
                new LambdaQueryWrapper<SysCustomerUser>()
                        .in(SysCustomerUser::getCustomerId, customerIdList));
        List<Integer> userIdList = new ArrayList<>();
        for (SysCustomerUser sysCustomerUser : sysCustomerUserList) {
            userIdList.add(sysCustomerUser.getUserId());
        }
        // userIdList为空时，查询不到数据
        if (userIdList.isEmpty()) {
            userIdList.add(-1);
        }
        // 根据customerId查询所有
        wrapper.in(SysUser::getId, userIdList);
        wrapper.ne(SysUser::getId, SysUserConstant.ADMINISTRATOR_ID);
        wrapper.eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED);
        wrapper.orderByDesc(SysUser::getUpdateTime);
        return wrapper;
    }

    private SysUserVo getSysUserVo(SysUser sysUser, SysCustomer customer, SysRole sysRole) {
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setId(sysUser.getId());
        sysUserVo.setCode(sysUser.getCode());
        sysUserVo.setAccount(sysUser.getAccount());
        sysUserVo.setPhone(sysUser.getPhone());
        sysUserVo.setName(sysUser.getName());
        sysUserVo.setCustomerName(customer == null ? "" : customer.getName());
        sysUserVo.setRoleName(sysRole == null ? "" : sysRole.getName());
        sysUserVo.setStatus(sysUser.getStatus());
        sysUserVo.setType(sysUser.getType());
        sysUserVo.setUpdatePerson(sysUser.getUpdatePerson());
        sysUserVo.setLastLoginTime(sysUser.getLastLoginTime());
        sysUserVo.setUpdateTime(sysUser.getUpdateTime());
        return sysUserVo;
    }

    @Override
    public SysUser getByAccount(String account) {
        return this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("account", account));
    }

    @Override
    public int updateById(SysUser sysUser) {
        return this.sysUserMapper.updateById(sysUser);
    }
}
