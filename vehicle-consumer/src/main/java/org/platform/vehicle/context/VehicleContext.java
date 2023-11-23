package org.platform.vehicle.context;

import static org.platform.vehicle.constant.DataInitConstant.VEHICLE_CONTEXT_KEY;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.vo.VehicleContextVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/2 09:14
 */
@Component
public class VehicleContext {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public VehicleContextVo getContext(String receiverNumber) {
        Object object = redisTemplate.opsForHash()
                .get(VEHICLE_CONTEXT_KEY, receiverNumber);
        if (object == null) {
            return null;
        }
        VehicleContextVo vehicleContextVo = JSONObject.parseObject((String) object,
                VehicleContextVo.class);
        return vehicleContextVo;
    }
}
