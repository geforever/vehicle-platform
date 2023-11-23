package org.platform.vehicle.context;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.vo.VehicleSpecContextVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/2 09:14
 */
@Component
public class VehicleSpecContext {

    public static final String VEHICLE_SPEC_CONTEXT_KEY = "vehicle_spec_models";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public VehicleSpecContextVo getContext(Integer vehicleSpecId) {
        Object object = redisTemplate.opsForHash()
                .get(VEHICLE_SPEC_CONTEXT_KEY, vehicleSpecId.toString());
        if (object == null) {
            return null;
        }
        VehicleSpecContextVo vehicleSpecContextVo = JSONObject.parseObject((String) object,
                VehicleSpecContextVo.class);
        return vehicleSpecContextVo;
    }

}
