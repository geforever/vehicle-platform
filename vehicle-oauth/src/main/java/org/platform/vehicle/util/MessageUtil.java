package org.platform.vehicle.util;

import org.platform.vehicle.utils.phone.model.SubmitResp;
import org.platform.vehicle.utils.phone.util.SmsClient;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2021/12/30 3:09 下午
 */

@Component
public class MessageUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final int MESSAGE_CODE_SIZE = 6;

    public String sendMessage(String phone, String message, String key) {
        String msgCode = getMsgCode();
        SubmitResp submitResp = null;
        String msg = message.replaceAll("\\{.*\\}", msgCode);
        SmsClient smsClient = new SmsClient();
        submitResp = smsClient.submit(phone, msg);
        if (submitResp.success()) {
            redisTemplate.opsForValue().set(key + phone, msgCode);
            redisTemplate.expire(key + phone, 2 * 60, TimeUnit.SECONDS);
            return msgCode;
        } else {
            return null;
        }
    }

    public boolean checkCode(String phoneNumber, String smsCode, String key) {
        String redisMsgCode = (String) redisTemplate.opsForValue().get(key + phoneNumber);
        if (StringUtils.isNotBlank(redisMsgCode)) {
            // 验证通过删除验证码
            if (redisMsgCode.equals(smsCode)) {
                redisTemplate.delete(key + phoneNumber);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * @Function: 生成验证码
     */
    private static String getMsgCode() {
        StringBuilder code = new StringBuilder();
        SecureRandom ran = new SecureRandom();
        for (int i = 0; i < MESSAGE_CODE_SIZE; i++) {
            code.append(Integer.valueOf(ran.nextInt(10)).toString());
        }
        return code.toString();
    }
}
