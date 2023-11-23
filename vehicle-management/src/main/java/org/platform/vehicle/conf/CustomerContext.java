package org.platform.vehicle.conf;

import static org.platform.vehicle.constant.DataInitConstant.CUSTOMER_CONTEXT_KEY;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.vo.context.CustomerContextVo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/2 09:14
 */
@Component
public class CustomerContext {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void init(Map<String, CustomerContextVo> customerModels) {
        clean();
        try {
            // 使用 JSON 序列化将对象转换为 JSON 字符串
            Map<String, String> serializedModels = new HashMap<>(customerModels.size());
            for (Map.Entry<String, CustomerContextVo> entry : customerModels.entrySet()) {
                String key = entry.getKey();
                String value = JSONObject.toJSONString(entry.getValue());
                serializedModels.put(key, value);
            }
            // 将 JSON 字符串存储在 Redis 哈希表中
            redisTemplate.opsForHash().putAll(CUSTOMER_CONTEXT_KEY, serializedModels);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    public CustomerContextVo getContext(Integer id) {
        Object object = redisTemplate.opsForHash()
                .get(CUSTOMER_CONTEXT_KEY, id.toString());
        if (object == null) {
            return null;
        }
        CustomerContextVo customerContextVo = JSONObject.parseObject((String) object,
                CustomerContextVo.class);
        return customerContextVo;
    }

    public void add(CustomerContextVo customerContextVo) {
        this.remove(customerContextVo.getId());
        redisTemplate.opsForHash().put(CUSTOMER_CONTEXT_KEY,
                customerContextVo.getId().toString(),
                JSONObject.toJSONString(customerContextVo));
    }

    public void edit(CustomerContextVo customerContextVo) {
        this.remove(customerContextVo.getId());
        redisTemplate.opsForHash().put(CUSTOMER_CONTEXT_KEY,
                customerContextVo.getId().toString(),
                JSONObject.toJSONString(customerContextVo));
    }

    public void remove(Integer id) {
        redisTemplate.opsForHash().delete(CUSTOMER_CONTEXT_KEY, String.valueOf(id));
    }

    public void clean() {
        redisTemplate.delete(CUSTOMER_CONTEXT_KEY);
    }

}
