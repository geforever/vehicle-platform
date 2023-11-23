package org.platform.vehicle.context;

import org.platform.vehicle.entity.TireNewestDataEntity;
import org.platform.vehicle.entity.TirePressure;
import org.platform.vehicle.entity.TirePressureData;
import org.platform.vehicle.mapper.jdbc.TireNewestDataJdbc;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/8 14:37
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TirePressureDataContext {

    private final TireNewestDataJdbc tireNewestDataJdbc;

    public void saveTireNewestData(List<TirePressure> tirePressureDtoList) {
        log.info("mysql车辆轮胎数据入库, size:{}", tirePressureDtoList.size());
        // 开始时间
        long startTime = System.currentTimeMillis();
        if (tirePressureDtoList.isEmpty()) {
            return;
        }
        Set<TireNewestDataEntity> insertOrUpdateSet = new HashSet<>();
        for (TirePressure tirePressure : tirePressureDtoList) {
            for (TirePressureData tirePressureData : tirePressure.getDataList()) {
                TireNewestDataEntity tireNewestDataParam = this.getTireNewestDataEntity(
                        tirePressure, tirePressureData);
                this.addInsertOrUpdateSet(insertOrUpdateSet, tireNewestDataParam);
            }
        }
        try {
            tireNewestDataJdbc.insertOnDuplicateKeyUpdate(insertOrUpdateSet);
        } catch (SQLException e) {
            log.error("mysql批量更新轮胎最新数据失败", e);
        }
        log.info("mysql车辆轮胎数据入库完成, 耗时:{}ms", System.currentTimeMillis() - startTime);
    }

    private void addInsertOrUpdateSet(Set<TireNewestDataEntity> insertOrUpdateSet,
            TireNewestDataEntity entity) {
        if (insertOrUpdateSet.contains(entity)) {
            insertOrUpdateSet.remove(entity);
            insertOrUpdateSet.add(entity);
        } else {
            insertOrUpdateSet.add(entity);
        }
    }

    private TireNewestDataEntity getTireNewestDataEntity(TirePressure tirePressure,
            TirePressureData tirePressureData) {
        TireNewestDataEntity tireNewestDataParam = new TireNewestDataEntity();
        tireNewestDataParam.setClientId(tirePressure.getClientId());
        tireNewestDataParam.setSerialNo(String.valueOf(tirePressure.getSerialNo()));
        tireNewestDataParam.setTrailerStatus(tirePressure.getTrailerStatus());
        tireNewestDataParam.setGuaRepeaterId(tirePressure.getGuaRepeaterId());
        tireNewestDataParam.setLongitude(tirePressure.getLongitude());
        tireNewestDataParam.setLatitude(tirePressure.getLatitude());
        tireNewestDataParam.setTireType(tirePressureData.getType());
        tireNewestDataParam.setTireSiteId(tirePressureData.getTireSiteId());
        tireNewestDataParam.setTireSensorId(tirePressureData.getTireSensorId());
        tireNewestDataParam.setVoltage(tirePressureData.getVoltage());
        tireNewestDataParam.setTirePressure(tirePressureData.getTirePressure());
        tireNewestDataParam.setTireTemperature(tirePressureData.getTireTemperature());
        tireNewestDataParam.setBatteryVoltageStatus(tirePressureData.getTireWarningData()
                .getBatteryVoltageStatus());
        tireNewestDataParam.setIsTimeout(tirePressureData.getTireWarningData()
                .getIsTimeout());
        tireNewestDataParam.setScheme(tirePressureData.getTireWarningData().getScheme());
        tireNewestDataParam.setTirePressureStatus(tirePressureData.getTireWarningData()
                .getTirePressureStatus());
        tireNewestDataParam.setTireTemperatureStatus(tirePressureData.getTireWarningData()
                .getTireTemperatureStatus());
        tireNewestDataParam.setTireStatus(tirePressureData.getTireWarningData()
                .getTireStatus());
        tireNewestDataParam.setDeviceTime(tirePressure.getDeviceTime());
        return tireNewestDataParam;
    }

}
