package org.platform.vehicle.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.platform.vehicle.constant.SysCustomerConstant;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.constants.LoginConstant;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/9/18 15:24
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SysCustomerMapper sysCustomerMapper;

    /**
     * 刷新当前登录人信息
     */
    public void refresh() {
        UserVo user = UserContext.getUser();
        SysCustomer sysCustomer = sysCustomerMapper.selectOne(new LambdaQueryWrapper<SysCustomer>()
                .eq(SysCustomer::getId, user.getCustomerId())
                .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        List<Integer> customerIds = this.getCustomerIds(sysCustomer);
        user.setCustomerIds(customerIds);
        this.saveUserInfoToRedis(user);
    }

    public void deleteUserCache(Integer userId) {
        String key = LoginConstant.LOGIN_REDIS_PREFIX + userId;
        redisTemplate.delete(key);
    }

    private List<Integer> getCustomerIds(SysCustomer sysCustomer) {
        List<Integer> customerIds = new ArrayList<>();
        // 面心用户查询所有客户级车队
        if (sysCustomer.getType() == SysCustomerConstant.PARENT_TYPE) {
            List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            for (SysCustomer customer : sysCustomerList) {
                customerIds.add(customer.getId());
            }
        } else {
            // 先添加所属ID
            customerIds.add(sysCustomer.getId());
            // 查询客户下的所有子客户
            List<SysCustomer> customerList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getCompanyId, sysCustomer.getCompanyId())
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            // 添加下级ID
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

    /**
     * 保存用户信息到redis
     *
     * @param userVo
     */
    private void saveUserInfoToRedis(UserVo userVo) {
        String json = JSONObject.toJSONString(userVo);
        String key = LoginConstant.LOGIN_REDIS_PREFIX + userVo.getUserId();
        // 查询用户是否登录，在登录状态删除登录记录
        String token = String.valueOf(redisTemplate.opsForValue().get(key));
        if (StringUtils.isBlank(token)) {
            return;
        }
        redisTemplate.opsForValue().set(token, json, 24, TimeUnit.HOURS);
    }

}
