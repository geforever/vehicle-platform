package org.platform.vehicle.conf;

import static org.platform.vehicle.constant.DataInitConstant.VEHICLE_SPEC_CONTEXT_KEY;

import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.vo.context.VehicleSpecContextVo;
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
public class VehicleSpecContext {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void init(Map<String, VehicleSpecContextVo> vehicleModels) {
        clean();
        try {
            // 使用 JSON 序列化将对象转换为 JSON 字符串
            Map<String, String> serializedModels = new HashMap<>(vehicleModels.size());
            for (Map.Entry<String, VehicleSpecContextVo> entry : vehicleModels.entrySet()) {
                String key = entry.getKey();
                String value = JSONObject.toJSONString(entry.getValue());
                serializedModels.put(key, value);
            }
            // 将 JSON 字符串存储在 Redis 哈希表中
            redisTemplate.opsForHash().putAll(VEHICLE_SPEC_CONTEXT_KEY, serializedModels);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

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

    /**
     * 获取所有的车型上下文
     *
     * @return
     */
    public Map<Integer, VehicleSpecContextVo> getAllContexts() {
        Map<Object, Object> all = redisTemplate.opsForHash().entries(VEHICLE_SPEC_CONTEXT_KEY);
        Map<Integer, VehicleSpecContextVo> contexts = new HashMap<>();
        all.forEach((key, value) -> contexts.put(Integer.parseInt((String) key),
                JSONObject.parseObject((String) value, VehicleSpecContextVo.class)));
        return contexts;
    }

    public void add(VehicleSpecEntity entity) {
        VehicleSpecContextVo vehicleSpecContextVo = this.getVehicleSpecContextVo(entity);
        this.remove(vehicleSpecContextVo.getSpecId());
        redisTemplate.opsForHash().put(VEHICLE_SPEC_CONTEXT_KEY,
                vehicleSpecContextVo.getSpecId().toString(),
                JSONObject.toJSONString(vehicleSpecContextVo));
    }

    private VehicleSpecContextVo getVehicleSpecContextVo(VehicleSpecEntity entity) {
        VehicleSpecContextVo vehicleSpecContextVo = new VehicleSpecContextVo();
        vehicleSpecContextVo.setSpecId(entity.getId());
        vehicleSpecContextVo.setSpecType(entity.getSpecType());
        vehicleSpecContextVo.setWheelCount(entity.getWheelCount());
        vehicleSpecContextVo.setWheelbaseType(entity.getWheelbaseType());
        vehicleSpecContextVo.setWheelArrange(entity.getWheelArrange());
        vehicleSpecContextVo.setLowPressureAlarmLevel1(entity.getLowPressureAlarmLevel1());
        vehicleSpecContextVo.setHighPressureAlarmLevel1(entity.getHighPressureAlarmLevel1());
        vehicleSpecContextVo.setHighTemperatureAlarmLevel1(entity.getHighTemperatureAlarmLevel1());
        vehicleSpecContextVo.setLowPressureAlarmLevel2(entity.getLowPressureAlarmLevel2());
        vehicleSpecContextVo.setHighPressureAlarmLevel2(entity.getHighPressureAlarmLevel2());
        vehicleSpecContextVo.setHighTemperatureAlarmLevel2(entity.getHighTemperatureAlarmLevel2());
        vehicleSpecContextVo.setEnableLev2Alarm(entity.getEnableLev2Alarm());
        return vehicleSpecContextVo;
    }

    public void edit(VehicleSpecEntity entity) {
        VehicleSpecContextVo vehicleSpecContextVo = this.getVehicleSpecContextVo(entity);
        this.remove(vehicleSpecContextVo.getSpecId());
        redisTemplate.opsForHash().put(VEHICLE_SPEC_CONTEXT_KEY,
                vehicleSpecContextVo.getSpecId().toString(),
                JSONObject.toJSONString(vehicleSpecContextVo));
    }

    public void remove(Integer id) {
        if (id != null) {
            redisTemplate.opsForHash().delete(VEHICLE_SPEC_CONTEXT_KEY, String.valueOf(id));
        }
    }

    public void clean() {
        redisTemplate.delete(VEHICLE_SPEC_CONTEXT_KEY);
    }

}
