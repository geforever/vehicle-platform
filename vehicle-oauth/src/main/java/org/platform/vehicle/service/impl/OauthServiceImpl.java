package org.platform.vehicle.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.platform.vehicle.constant.SysCustomerConstant;
import org.platform.vehicle.constant.SysRoleConstant;
import org.platform.vehicle.constant.SysUserConstant;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.SysCustomerUser;
import org.platform.vehicle.entity.SysMenu;
import org.platform.vehicle.entity.SysRole;
import org.platform.vehicle.entity.SysRoleMenu;
import org.platform.vehicle.entity.SysUser;
import org.platform.vehicle.entity.SysUserRole;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.SysCustomerUserMapper;
import org.platform.vehicle.mapper.SysMenuMapper;
import org.platform.vehicle.mapper.SysRoleMapper;
import org.platform.vehicle.mapper.SysRoleMenuMapper;
import org.platform.vehicle.mapper.SysUserMapper;
import org.platform.vehicle.mapper.SysUserRoleMapper;
import org.platform.vehicle.param.LoginParam;
import org.platform.vehicle.param.ResetPasswordParam;
import org.platform.vehicle.service.OauthService;
import org.platform.vehicle.util.MessageUtil;
import org.platform.vehicle.util.TokenUtil;
import org.platform.vehicle.constants.LoginConstant;
import org.platform.vehicle.constants.SmsCodeConstant;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.Md5Utils;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.utils.sms.SmsContext;
import org.platform.vehicle.utils.sms.strategy.SmsStrategy;
import org.platform.vehicle.vo.LoginVo;
import org.platform.vehicle.vo.MenuVo;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/8/23 11:24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthServiceImpl implements OauthService {

    private final SysUserMapper sysUserMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MessageUtil messageUtil;
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysCustomerMapper sysCustomerMapper;
    private final SysCustomerUserMapper sysCustomerUserMapper;

    @Resource(name = "loginSmsStrategy")
    private SmsStrategy loginSmsStrategy;

    @Resource(name = "resetPasswordSmsStrategy")
    private SmsStrategy resetPasswordSmsStrategy;

    /**
     * 登录
     *
     * @param loginParam
     * @return
     */
    @Override
    public BaseResponse login(LoginParam loginParam) {
        Integer source = loginParam.getSource();
        SysUser sysUser = this.checkLogin(loginParam);
        Integer roleId = this.getSysUserRoleId(sysUser);
        Set<MenuVo> menuTree = this.getMenuTree(roleId, source);
        SysCustomer sysCustomer = this.getSysCustomer(sysUser);
        // 查询公司信息
        SysCustomer client = sysCustomerMapper.selectById(sysCustomer.getCompanyId());
        // 客户/车队包含下级
        List<Integer> customerIds = this.getCustomerIds(sysCustomer);
        // 保存登录时间
        this.saveLastLoginTime(sysUser);
        String token = TokenUtil.getToken();
        LoginVo loginVo = this.getLoginVo(sysUser, sysCustomer, client, customerIds, roleId, token,
                menuTree, source);
        // 用户信息存放redis
        this.saveUserInfoToRedis(loginVo);
        return BaseResponse.ok(loginVo);
    }

    private void saveLastLoginTime(SysUser sysUser) {
        SysUser userUpdateParam = new SysUser();
        userUpdateParam.setId(sysUser.getId());
        userUpdateParam.setLastLoginTime(new Date());
        sysUserMapper.updateById(userUpdateParam);
    }

    private LoginVo getLoginVo(SysUser sysUser, SysCustomer sysCustomer, SysCustomer client,
            List<Integer> customerIds,
            Integer roleId, String token, Set<MenuVo> menuTree, Integer source) {
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(sysUser.getId());
        loginVo.setAccount(sysUser.getAccount());
        loginVo.setName(sysUser.getName());
        loginVo.setPhone(sysUser.getPhone());
        loginVo.setCustomerId(sysCustomer.getId());
        loginVo.setCompanyId(sysCustomer.getCompanyId());
        loginVo.setCompanyName(client.getName());
        loginVo.setCustomerName(sysCustomer.getName());
        loginVo.setCustomerType(sysCustomer.getType());
        loginVo.setCustomerIds(customerIds);
        loginVo.setRoleId(roleId);
        loginVo.setToken(token);
        loginVo.setMenuTree(menuTree);
        loginVo.setSource(source);
        return loginVo;
    }

    private SysCustomer getSysCustomer(SysUser sysUser) {
        // 查询customer信息
        Integer customerId = sysCustomerUserMapper.selectOne(
                new LambdaQueryWrapper<SysCustomerUser>()
                        .eq(SysCustomerUser::getUserId, sysUser.getId())).getCustomerId();
        SysCustomer sysCustomer = sysCustomerMapper.selectOne(new LambdaQueryWrapper<SysCustomer>()
                .eq(SysCustomer::getId, customerId)
                .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysCustomer == null) {
            throw new BaseException("99999", "该客户不存在");
        }
        if (sysCustomer.getStatus() == SysCustomerConstant.DISABLE) {
            throw new BaseException("99999", "当前客户已停用");
        }
        return sysCustomer;
    }

    private List<Integer> getCustomerIds(SysCustomer sysCustomer) {
        List<Integer> customerIds = new ArrayList<>();
        // 面心用户查询所有客户级车队
        if (sysCustomer.getType() == SysCustomerConstant.PARENT_COMPANY_TYPE) {
            List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            for (SysCustomer customer : sysCustomerList) {
                customerIds.add(customer.getId());
            }
        } else if (sysCustomer.getType() == SysCustomerConstant.CLIENT_TYPE) {
            // 用户所属客户则查询所有车队
            List<SysCustomer> customerList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getCompanyId, sysCustomer.getCompanyId())
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            for (SysCustomer customer : customerList) {
                customerIds.add(customer.getId());
            }
        } else {
            // 用户所属车队则查询所有下级车队
            customerIds.add(sysCustomer.getId());
            List<SysCustomer> customerList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getCompanyId, sysCustomer.getCompanyId())
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            this.getChildCustomer(sysCustomer, customerList, customerIds);
        }
        return customerIds;
    }

    private void getChildCustomer(SysCustomer sysCustomer, List<SysCustomer> customerList,
            List<Integer> customerIds) {
        for (SysCustomer customer : customerList) {
            if (sysCustomer.getId().equals(customer.getParentId())) {
                customerIds.add(customer.getId());
                this.getChildCustomer(customer, customerList, customerIds);
            }
        }
    }

    private Integer getSysUserRoleId(SysUser sysUser) {
        // 用户角色是一对一的
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, sysUser.getId()));
        if (sysUserRole == null) {
            throw new BaseException("99999", "该用户没有权限登录");
        }
        // 查询角色
        SysRole sysRole = sysRoleMapper.selectById(sysUserRole.getRoleId());
        if (sysRole == null) {
            throw new BaseException("99999", "该用户没有权限登录");
        }
        if (sysRole.getStatus() == SysRoleConstant.DISABLE) {
            throw new BaseException("99999", "该角色已被禁用");
        }
        return sysUserRole.getRoleId();
    }

    private Set<MenuVo> getMenuTree(Integer roleId, Integer source) {
        // 获取菜单
        List<SysMenu> sysMenuList = this.getSysMenus(roleId, source);
        List<MenuVo> sysMenuVoList = this.getSysMenuVo(sysMenuList);
        Set<MenuVo> parentMenuVo = new TreeSet<>();
        for (MenuVo sysMenu : sysMenuVoList) {
            // 角色类型和菜单匹配
            if (sysMenu.getParentId().equals(source)) {
                parentMenuVo.add(sysMenu);
            }
        }
        //生成菜单树
        this.processMenuTree(sysMenuVoList, parentMenuVo);
        return parentMenuVo;
    }

    private void processMenuTree(List<MenuVo> sysMenuList, Set<MenuVo> parentMenu) {
        for (MenuVo parent : parentMenu) {
            Integer parentId = parent.getId();
            // 去除重复子菜单
            LinkedHashSet<MenuVo> sysMenuHashSet = new LinkedHashSet<>();
            // 遍历获取所有菜单中，父id为sysMenuId的菜单
            for (MenuVo child : sysMenuList) {
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

    /**
     * 菜单转换
     *
     * @param sysMenuList
     * @return
     */
    private List<MenuVo> getSysMenuVo(List<SysMenu> sysMenuList) {
        List<MenuVo> sysMenuVoList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            MenuVo sysMenuVo = new MenuVo();
            sysMenuVo.setId(sysMenu.getId());
            sysMenuVo.setParentId(sysMenu.getParentId());
            sysMenuVo.setName(sysMenu.getName());
            sysMenuVo.setCode(sysMenu.getCode());
            sysMenuVo.setUrl(sysMenu.getUrl());
            sysMenuVo.setType(sysMenu.getType());
            sysMenuVo.setSort(sysMenu.getSort());
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }

    /**
     * 获取菜单
     *
     * @param roleId
     * @param source
     * @return
     */
    private List<SysMenu> getSysMenus(Integer roleId, Integer source) {
        List<SysMenu> sysMenuList = null;
        // 超级管理员查询所有菜单
        if (roleId.equals(SysRoleConstant.DEFAULT_ADMIN_ROLE_ID)) {
            sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus, SysUserConstant.STATUS_ENABLE)
                    .eq(SysMenu::getIsDelete, SysUserConstant.NOT_DELETED));
        } else {
            // 根据角色查询菜单
            List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(
                    new LambdaQueryWrapper<SysRoleMenu>()
                            .eq(SysRoleMenu::getRoleId, roleId));
            List<Integer> menuIdList = new ArrayList<>();
            for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
                menuIdList.add(sysRoleMenu.getMenuId());
            }
            // 校验登录权限中是否包含当前登录平台
            if (!menuIdList.contains(source)) {
                throw new BaseException("99999", "该用户没有权限登录");
            }
            // 查询角色对应的菜单
            sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .in(SysMenu::getId, menuIdList)
                    .eq(SysMenu::getStatus, SysUserConstant.STATUS_ENABLE)
                    .eq(SysMenu::getIsDelete, SysUserConstant.NOT_DELETED));
        }
        return sysMenuList;
    }

    private SysUser checkLogin(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String phone = loginParam.getPhone();
        Integer type = loginParam.getType();
        String smsCode = loginParam.getSmsCode();
        SysUser sysUser = null;
        // 查询用户信息
        if (type == LoginConstant.LOGIN_TYPE_PASSWORD) {
            sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getAccount, account)
                    .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
            if (sysUser == null) {
                // 用户名不存在，查询手机号
                sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getPhone, account)
                        .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
            }
            if (sysUser == null) {
                throw new BaseException("99999", "用户名或密码错误");
            }
            String passwordMd5 = Md5Utils.getMd5(password);
            if (!sysUser.getPassword().equals(passwordMd5)) {
                throw new BaseException("99999", "用户名或密码错误");
            }
        } else if (type == LoginConstant.LOGIN_TYPE_SMS) {
            sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getPhone, phone)
                    .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
            if (sysUser == null) {
                throw new BaseException("99999", "用户名或密码错误");
            }
            // 校验验证码
            SmsContext smsContext = new SmsContext(loginSmsStrategy);
            boolean flag = smsContext.checkSmsCode(phone, smsCode,
                    SmsCodeConstant.LOGIN_PHONE_SMS_CODE);
            if (!flag) {
                throw new BaseException("99999", "验证码错误");
            }
        } else {
            throw new BaseException("99999", "登录类型错误");
        }
        if (sysUser.getStatus() == LoginConstant.ACCOUNT_DISABLE) {
            throw new BaseException("99999", "该账号已被禁用");
        }
        return sysUser;
    }

    /**
     * 获取验证码
     *
     * @return
     */
    @Override
    public BaseResponse getVerifyCode() {
        return null;
    }

    /**
     * 发送验证码
     *
     * @return
     */
    @Override
    public BaseResponse sendLoginSmsCode(String phone) {
        this.checkSendSmsCodeAuthorizationOnLogin(phone);
        SmsContext smsContext = new SmsContext(loginSmsStrategy);
        String msgCode = smsContext.exec(phone);
        return BaseResponse.ok(msgCode);
    }

    private void checkSendSmsCodeAuthorizationOnLogin(String phone) {
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone)
                .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
        // 校验用户信息
        if (sysUser == null) {
            throw new BaseException("99999", "该用户不存在");
        }
        if (sysUser.getStatus() == LoginConstant.ACCOUNT_DISABLE) {
            throw new BaseException("99999", "该账号已被禁用");
        }
        this.getSysUserRoleId(sysUser);
    }

    /**
     * 开发环境-登录
     *
     * @param loginParam
     * @return
     */
    @Override
    public BaseResponse devLogin(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        // 查询用户信息
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getAccount, account));
        if (sysUser == null) {
            return BaseResponse.failure("用户名或密码错误");
        }
        String passwordMd5 = Md5Utils.getMd5(password);
        if (!sysUser.getPassword().equals(passwordMd5)) {
            return BaseResponse.failure("用户名或密码错误");
        }
        // 保存登录时间
        this.saveLastLoginTime(sysUser);
        String token = TokenUtil.getToken();
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(sysUser.getId());
        loginVo.setAccount(sysUser.getAccount());
        loginVo.setName(sysUser.getName());
        loginVo.setPhone(sysUser.getPhone());
        loginVo.setRoleId(SysRoleConstant.DEFAULT_ADMIN_ROLE_ID);
        loginVo.setToken(token);
        // 用户信息存放redis
        this.saveUserInfoToRedis(loginVo);
        return BaseResponse.ok(loginVo);
    }

    /**
     * 登出
     *
     * @return
     */
    @Override
    public BaseResponse logout() {
        UserVo user = UserContext.getUser();
        this.deleteRedisData(user);
        return BaseResponse.ok();
    }

    /**
     * 修改密码
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse resetPassword(ResetPasswordParam param) {
        String phone = param.getPhone();
        String newPassword = param.getNewPassword();
        String confirmPassword = param.getConfirmPassword();
        String smsCode = param.getSmsCode();
        // 校验验证码
        SmsContext smsContext = new SmsContext(resetPasswordSmsStrategy);
        boolean flag = smsContext.checkSmsCode(phone, smsCode,
                SmsCodeConstant.RESET_PASSWORD_PHONE_SMS_CODE);
        if (!flag) {
            throw new BaseException("99999", "验证码错误");
        }
        // 校验密码
        if (!newPassword.equals(confirmPassword)) {
            throw new BaseException("99999", "两次密码不一致");
        }
        // 密码md5加密
        String passwordMd5 = Md5Utils.getMd5(newPassword);
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone)
                .eq(SysUser::getIsDelete, SysUserConstant.NOT_DELETED));
        if (sysUser == null) {
            throw new BaseException("99999", "该用户不存在");
        }
        // 修改密码
        SysUser userUpdateParam = new SysUser();
        userUpdateParam.setId(sysUser.getId());
        userUpdateParam.setPassword(passwordMd5);
        sysUserMapper.updateById(userUpdateParam);
        // 删除redis数据
        this.deleteRedisData(UserContext.getUser());
        return BaseResponse.ok();
    }


    /**
     * 修改密码-发送验证码
     *
     * @return
     */
    @Override
    public BaseResponse sendResetPasswordSmsCode(String phone) {
        SmsContext smsContext = new SmsContext(resetPasswordSmsStrategy);
        String msgCode = smsContext.exec(phone);
        return BaseResponse.ok(msgCode);
    }

    private void deleteRedisData(UserVo user) {
        Integer userId = user.getUserId();
        String key = LoginConstant.LOGIN_REDIS_PREFIX + userId;
        // 查询用户是否登录，在登录状态删除登录记录
        Object tokenObj = redisTemplate.opsForValue().get(key);
        if (tokenObj == null) {
            return;
        }
        String token = String.valueOf(tokenObj);
        Object userInfoObj = redisTemplate.opsForValue().get(token);
        if (userInfoObj == null) {
            redisTemplate.delete(key);
            return;
        }
        redisTemplate.delete(key);
        redisTemplate.delete(token);
        UserContext.clean();
    }

    /**
     * 保存用户信息到redis
     *
     * @param loginVo
     */
    private void saveUserInfoToRedis(LoginVo loginVo) {
        UserVo userVo = this.getUserVo(loginVo);
        String json = JSONObject.toJSONString(userVo);
        String key = LoginConstant.LOGIN_REDIS_PREFIX + loginVo.getUserId();
        // 查询用户是否登录，在登录状态删除登录记录
        String oldToken = String.valueOf(redisTemplate.opsForValue().get(key));
        if (!StringUtils.isBlank(oldToken)) {
            redisTemplate.delete(oldToken);
            redisTemplate.delete(key);
        }
        redisTemplate.opsForValue().set(key, loginVo.getToken(), 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(loginVo.getToken(), json, 24, TimeUnit.HOURS);
    }

    private UserVo getUserVo(LoginVo loginVo) {
        UserVo userVo = new UserVo();
        userVo.setUserId(loginVo.getUserId());
        userVo.setAccount(loginVo.getAccount());
        userVo.setName(loginVo.getName());
        userVo.setPhone(loginVo.getPhone());
        userVo.setCustomerId(loginVo.getCustomerId());
        userVo.setCompanyId(loginVo.getCompanyId());
        userVo.setCompanyName(loginVo.getCompanyName());
        userVo.setCustomerName(loginVo.getCustomerName());
        userVo.setCustomerType(loginVo.getCustomerType());
        userVo.setCustomerIds(loginVo.getCustomerIds());
        userVo.setRoleId(loginVo.getRoleId());
        userVo.setToken(loginVo.getToken());
        userVo.setMenuTree(loginVo.getMenuTree());
        userVo.setSource(loginVo.getSource());
        return userVo;
    }
}
