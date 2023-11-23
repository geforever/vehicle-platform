package org.platform.vehicle.util;

import org.platform.vehicle.constant.WarningTypeEnum;
import org.platform.vehicle.context.TrailerVehicleContext;
import org.platform.vehicle.context.VehicleContext;
import org.platform.vehicle.context.VehicleSpecContext;
import org.platform.vehicle.entity.TirePressure;
import org.platform.vehicle.entity.TirePressureData;
import org.platform.vehicle.param.TireCheckDataDetailParam;
import org.platform.vehicle.param.TireCheckDataParam;
import org.platform.vehicle.param.TireCheckWarningDataParam;
import org.platform.vehicle.service.VehicleManagementFeignClientService;
import org.platform.vehicle.vo.VehicleContextVo;
import org.platform.vehicle.vo.VehicleSpecContextVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/14 11:46
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TireDataUtil {

    private final VehicleSpecContext vehicleSpecContext;
    private final VehicleManagementFeignClientService vehicleManagementFeignClientService;
    private final VehicleContext vehicleContext;
    private final TrailerVehicleContext trailerVehicleContext;


    @Async("asyncTaskExecutor")
    public void prepareData(List<TirePressure> tirePressureList) {
        List<TireCheckDataParam> tireCheckDataParamList = new ArrayList<>();
        for (TirePressure tirePressure : tirePressureList) {
            TireCheckDataParam tireCheckDataParam = new TireCheckDataParam();
            tireCheckDataParam.setReceiverId(tirePressure.getClientId());
            tireCheckDataParam.setSerialNo(tirePressure.getSerialNo());
            tireCheckDataParam.setTrailerStatus(tirePressure.getTrailerStatus());
            tireCheckDataParam.setGuaRepeaterId(tirePressure.getGuaRepeaterId());
            tireCheckDataParam.setLongitude(tirePressure.getLongitude());
            tireCheckDataParam.setLatitude(tirePressure.getLatitude());
            List<TireCheckDataDetailParam> tireCheckDataDetailParamList = new ArrayList<>();
            for (TirePressureData tirePressureData : tirePressure.getDataList()) {
                TireCheckDataDetailParam tireCheckDataDetailParam = this.getTireCheckDataDetailParam(
                        tirePressureData);
                tireCheckDataDetailParamList.add(tireCheckDataDetailParam);
            }
            tireCheckDataParam.setTireCheckDataDetailParamList(tireCheckDataDetailParamList);
            tireCheckDataParamList.add(tireCheckDataParam);
        }
        this.checkData(tireCheckDataParamList);
    }

    void checkData(List<TireCheckDataParam> paramList) {
        log.info("开始校验轮胎数据,size:{}", paramList.size());
        // 开始时间
        long startTime = System.currentTimeMillis();
        List<TireCheckDataParam> errTireDataList = new ArrayList<>();
        // 主车
        Map<String, VehicleContextVo> mainVehicleResult = new HashMap<>();
        // 挂车
        Map<String, VehicleContextVo> minorVehicleResult = new HashMap<>();
        for (TireCheckDataParam param : paramList) {
            if (StringUtils.isNotBlank(param.getGuaRepeaterId())) {
                minorVehicleResult.put(param.getGuaRepeaterId(),
                        trailerVehicleContext.getContext(param.getGuaRepeaterId()));
            }
            if (StringUtils.isNotBlank(param.getReceiverId())) {
                mainVehicleResult.put(param.getReceiverId(),
                        vehicleContext.getContext(param.getReceiverId()));
            }
        }
        for (TireCheckDataParam param : paramList) {
            TireCheckDataParam errTireData = new TireCheckDataParam();
            List<TireCheckDataDetailParam> errorTireDetailList = new ArrayList<>();
            if (mainVehicleResult.isEmpty()) {
                return;
            }
            VehicleContextVo mainVehicle = this.getVehicleEntity(param, mainVehicleResult);
            if (mainVehicle == null) {
                continue;
            }
            VehicleSpecContextVo minorVehicleSpec = null;
            if (!minorVehicleResult.isEmpty()) {
                VehicleContextVo minorVehicle = this.getMinorVehicle(param, minorVehicleResult);
                if (minorVehicle != null) {
                    minorVehicleSpec = vehicleSpecContext.getContext(minorVehicle.getSpecId());
                }
            }
            VehicleSpecContextVo mainVehicleSpec = vehicleSpecContext.getContext(
                    mainVehicle.getSpecId());
            if (mainVehicleSpec == null) {
                return;
            }
            for (TireCheckDataDetailParam tireData : param.getTireCheckDataDetailParamList()) {
                // 校验轮胎数据
                if (this.isErrorTireData(tireData, mainVehicleSpec, minorVehicleSpec)) {
                    errorTireDetailList.add(tireData);
                }
            }
            if (!errorTireDetailList.isEmpty()) {
                errTireData.setReceiverId(param.getReceiverId());
                errTireData.setSerialNo(param.getSerialNo());
                errTireData.setTrailerStatus(param.getTrailerStatus());
                errTireData.setGuaRepeaterId(param.getGuaRepeaterId());
                errTireData.setLongitude(param.getLongitude());
                errTireData.setLatitude(param.getLatitude());
                errTireData.setTireCheckDataDetailParamList(errorTireDetailList);
                errTireDataList.add(errTireData);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("校验轮胎数据完成,耗时:{}", endTime - startTime);
        if (!errTireDataList.isEmpty()) {
            vehicleManagementFeignClientService.checkData(errTireDataList);
        }
    }

    private VehicleContextVo getMinorVehicle(TireCheckDataParam param,
            Map<String, VehicleContextVo> minorVehicleResult) {
        VehicleContextVo minorVehicle = null;
        minorVehicle = minorVehicleResult.get(param.getGuaRepeaterId());
        return minorVehicle;
    }

    private VehicleContextVo getVehicleEntity(TireCheckDataParam param,
            Map<String, VehicleContextVo> mainVehicleResult) {
        VehicleContextVo mainVehicle = null;
        mainVehicle = mainVehicleResult.get(param.getReceiverId());
        return mainVehicle;
    }


    private TireCheckDataDetailParam getTireCheckDataDetailParam(
            TirePressureData tirePressureData) {
        TireCheckDataDetailParam tireCheckDataDetailParam = new TireCheckDataDetailParam();
        tireCheckDataDetailParam.setType(tirePressureData.getType());
        tireCheckDataDetailParam.setTireSiteId(tirePressureData.getTireSiteId());
        tireCheckDataDetailParam.setTireSensorId(tirePressureData.getTireSensorId());
        tireCheckDataDetailParam.setVoltage(tirePressureData.getVoltage());
        tireCheckDataDetailParam.setTirePressure(tirePressureData.getTirePressure());
        tireCheckDataDetailParam.setTireTemperature(tirePressureData.getTireTemperature());
        TireCheckWarningDataParam tireCheckWarningDataParam = new TireCheckWarningDataParam();
        tireCheckWarningDataParam.setBatteryVoltageStatus(
                tirePressureData.getTireWarningData().getBatteryVoltageStatus());
        tireCheckWarningDataParam.setIsTimeout(
                tirePressureData.getTireWarningData().getIsTimeout());
        tireCheckWarningDataParam.setScheme(
                tirePressureData.getTireWarningData().getScheme());
        tireCheckWarningDataParam.setTirePressureStatus(
                tirePressureData.getTireWarningData().getTirePressureStatus());
        tireCheckWarningDataParam.setTireTemperatureStatus(
                tirePressureData.getTireWarningData().getTireTemperatureStatus());
        tireCheckWarningDataParam.setTireStatus(
                tirePressureData.getTireWarningData().getTireStatus());
        tireCheckDataDetailParam.setTireWarningData(tireCheckWarningDataParam);
        return tireCheckDataDetailParam;
    }

    private boolean isErrorTireData(TireCheckDataDetailParam tireData,
            VehicleSpecContextVo mainVehicleSpec, VehicleSpecContextVo minorVehicleSpec) {
        List<WarningTypeEnum> getTireWarningType = new ArrayList<>();
        if (tireData.getType() == 0) {
            if (mainVehicleSpec != null) {
                getTireWarningType = WarningHelper.getTireWarningType(tireData, mainVehicleSpec);
            }
        } else {
            if (minorVehicleSpec != null) {
                getTireWarningType = WarningHelper.getTireWarningType(tireData, minorVehicleSpec);
            }
        }
        return !getTireWarningType.isEmpty();
    }
}
