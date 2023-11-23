package org.platform.vehicle.util;

import org.platform.vehicle.exception.BaseException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/8/28 14:41
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsHelpUtil {

    public static final int LOGIN_COUNT_LIMIT = 5;
    private final RedisTemplate<String, String> redisTemplate;


    /**
     * 校验短信发送次数
     *
     * @param phone
     * @param keyPrefix
     * @param countKey
     * @return
     */
    public Integer checkSmsLimit(String phone, String keyPrefix, String countKey) {
        String timeStr = (String) redisTemplate.opsForHash().get(countKey + phone, phone);
        Integer time = null;
        if (timeStr == null) {
            time = 0;
        } else {
            time = Integer.valueOf(timeStr);
        }
        if (time >= LOGIN_COUNT_LIMIT) {
            //等于20的时候就说明上次加1是第五次操作
            throw new BaseException("999999", "您今天的验证码使用次数已达到上限，请24小时后再登录！");
        }
        String messagePhone = (String) redisTemplate.opsForValue().get(keyPrefix + phone);
        if (StringUtils.isNotBlank(messagePhone)) {
            //短信验证码在有效时间2分钟内重复发送
            throw new BaseException("999999", "短信验证码的有效时间为2分钟，2分钟内不能多次获取验证码短信。");
        }
        return time;
    }

    public void addCount(String keyPrefix, String phone, String smsCode, Integer count) {
        if (smsCode != null) {
            log.info("手机号:{},手机验证码:{}", phone, smsCode);
            if (count == 0) {
                //没有此手机号的缓存，填1
                redisTemplate.opsForHash().put(keyPrefix + phone, phone, "" + 1);
                redisTemplate.expire(keyPrefix + phone, 24L, TimeUnit.HOURS);
            } else {
                count = count + 1;
                redisTemplate.opsForHash().put(keyPrefix + phone, phone, "" + count);
            }
        }
    }
}
