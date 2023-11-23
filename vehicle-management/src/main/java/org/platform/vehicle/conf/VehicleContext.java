package org.platform.vehicle.conf;

import static org.platform.vehicle.constant.DataInitConstant.VEHICLE_CONTEXT_KEY;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.vo.context.VehicleContextVo;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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

    public void init(Map<String, VehicleContextVo> vehicleModels) {
        try {
            // 使用 JSON 序列化将对象转换为 JSON 字符串
            Map<String, String> serializedModels = new HashMap<>(vehicleModels.size());
            for (Map.Entry<String, VehicleContextVo> entry : vehicleModels.entrySet()) {
                String key = entry.getKey();
                String value = JSONObject.toJSONString(entry.getValue());
                serializedModels.put(key, value);
            }
            // 将 JSON 字符串存储在 Redis 哈希表中
            redisTemplate.opsForHash().putAll(VEHICLE_CONTEXT_KEY, serializedModels);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

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

    public void add(VehicleEntity entity) {
        if (StringUtils.isBlank(entity.getReceiverIdNumber())) {
            return;
        }
        if (entity.getFleetId() == null) {
            return;
        }
        VehicleContextVo vehicleContextVo = this.getVehicleSpecContextVo(entity);
        this.remove(entity.getReceiverIdNumber());
        redisTemplate.opsForHash().put(VEHICLE_CONTEXT_KEY,
                entity.getReceiverIdNumber(),
                JSONObject.toJSONString(vehicleContextVo));
    }

    private VehicleContextVo getVehicleSpecContextVo(VehicleEntity entity) {
        VehicleContextVo vehicleContextVo = new VehicleContextVo();
        vehicleContextVo.setLicensePlate(entity.getLicensePlate());
        vehicleContextVo.setFleetId(entity.getFleetId());
        vehicleContextVo.setSpecId(entity.getSpecId());
        return vehicleContextVo;
    }

    public void edit(VehicleEntity history, VehicleEntity entity) {
        // 接收器为空,则删除缓存
        this.remove(history.getReceiverIdNumber());
        if (StringUtils.isBlank(entity.getReceiverIdNumber())) {
            return;
        }
        if (entity.getFleetId() == null) {
            return;
        }
        VehicleContextVo vehicleContextVo = this.getVehicleSpecContextVo(entity);
        redisTemplate.opsForHash().put(VEHICLE_CONTEXT_KEY,
                entity.getReceiverIdNumber(),
                JSONObject.toJSONString(vehicleContextVo));
    }

    public void remove(String key) {
        if (StringUtils.isNotBlank(key)) {
            redisTemplate.opsForHash().delete(VEHICLE_CONTEXT_KEY, key);
        }
    }

    public void clean() {
        redisTemplate.delete(VEHICLE_CONTEXT_KEY);
    }

}
