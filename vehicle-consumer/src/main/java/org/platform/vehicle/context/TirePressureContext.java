package org.platform.vehicle.context;

import org.platform.vehicle.dto.TirePressureDataDto;
import org.platform.vehicle.dto.TirePressureDto;
import org.platform.vehicle.entity.TirePressure;
import org.platform.vehicle.entity.TirePressureData;
import org.platform.vehicle.service.ElasticSearchService;
import org.platform.vehicle.vo.VehicleContextVo;
import org.platform.vehicle.utils.IdWorker;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/8 14:37
 */
@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TirePressureContext {

    private final ElasticSearchService elasticSearchService;
    private final VehicleContext vehicleContext;
    private final TirePressureDataContext tirePressureDataContext;

    private final IdWorker ID_WORKER = new IdWorker();


    @Value("${vehicle.execute_batch_size}")
    public Integer BATCH_SIZE;
    private static final Queue<TirePressure> TIRE_PRESSURE_QUEUE = new ConcurrentLinkedQueue<>();


    @Async("esTaskExecutor")
    public void push(TirePressure tirePressure) {
        TIRE_PRESSURE_QUEUE.add(tirePressure);
        event();
    }

    public void event() {
        log.info("执行车辆轮胎数据入库事件, TIRE_PRESSURE_QUEUE.size:{}",
                TIRE_PRESSURE_QUEUE.size());
        if (TIRE_PRESSURE_QUEUE.size() >= BATCH_SIZE) {
            this.execute();
        }
    }

    public void execute() {
        Date currentDate = new Date();
        List<TirePressure> tirePressureList = new ArrayList<>();
        List<TirePressureDto> tirePressureDtoList = new ArrayList<>();
        List<TirePressureDataDto> tirePressureDataDtoList = new ArrayList<>();
        for (int i = 0; i < BATCH_SIZE; i++) {
            TirePressure tirePressure = TIRE_PRESSURE_QUEUE.poll();
            if (tirePressure != null) {
                tirePressureList.add(tirePressure);
                long code = ID_WORKER.nextId();
                VehicleContextVo context = vehicleContext.getContext(tirePressure.getClientId());
                if (context == null) {
                    continue;
                }
                TirePressureDto tirePressureDto = this.getTirePressureDto(code, context,
                        tirePressure, currentDate);
                tirePressureDtoList.add(tirePressureDto);
                List<TirePressureData> dataList = tirePressure.getDataList();
                for (TirePressureData tirePressureData : dataList) {
                    TirePressureDataDto tirePressureDataDto = this.getTirePressureDataDto(
                            tirePressure, tirePressureData, code, context, tirePressureDto,
                            currentDate);
                    tirePressureDataDtoList.add(tirePressureDataDto);
                }
            }
        }
        try {
            // 保存es
            elasticSearchService.saveTirePressure(tirePressureDtoList);
            elasticSearchService.saveTirePressureData(tirePressureDataDtoList);
        } catch (Exception e) {
            log.error("ES保存轮胎数据失败", e);
        }
        try {
            // 保存mysql
            tirePressureDataContext.saveTireNewestData(tirePressureList);
        } catch (Exception e) {
            log.error("保存轮胎最新数据失败", e);
        }
    }

    private TirePressureDto getTirePressureDto(long code, VehicleContextVo context,
            TirePressure tirePressure, Date currentDate) {
        TirePressureDto tirePressureDto = new TirePressureDto();
        tirePressureDto.setCode(code);
        tirePressureDto.setFleetId(context.getFleetId());
        tirePressureDto.setClientId(tirePressure.getClientId());
        tirePressureDto.setSerialNo(tirePressure.getSerialNo());
        tirePressureDto.setReceiverId(tirePressure.getReceiverId());
        tirePressureDto.setUpTrailerInfo(tirePressure.getUpTrailerInfo());
        tirePressureDto.setTrailerStatus(tirePressure.getTrailerStatus());
        tirePressureDto.setGuaRepeaterId(tirePressure.getGuaRepeaterId());
        tirePressureDto.setFrontAxleRelayInfo(tirePressure.getFrontAxleRelayInfo());
        tirePressureDto.setLongitude(tirePressure.getLongitude());
        tirePressureDto.setLatitude(tirePressure.getLatitude());
        tirePressureDto.setDeviceTime(tirePressure.getDeviceTime());
        tirePressureDto.setCreateTime(currentDate);
        return tirePressureDto;
    }

    private TirePressureDataDto getTirePressureDataDto(TirePressure tirePressure,
            TirePressureData tirePressureData, long code, VehicleContextVo context,
            TirePressureDto tirePressureDto, Date currentDate) {
        TirePressureDataDto tirePressureDataDto = new TirePressureDataDto();
        tirePressureDataDto.setParentCode(code);
        tirePressureDataDto.setFleetId(context.getFleetId());
        tirePressureDataDto.setClientId(tirePressure.getClientId());
        tirePressureDataDto.setType(tirePressureData.getType());
        tirePressureDataDto.setTireSiteId(tirePressureData.getTireSiteId());
        tirePressureDataDto.setTireSensorId(tirePressureData.getTireSensorId());
        tirePressureDataDto.setVoltage(tirePressureData.getVoltage());
        tirePressureDataDto.setTirePressure(tirePressureData.getTirePressure());
        tirePressureDataDto.setTireTemperature(tirePressureData.getTireTemperature());
        if (tirePressureData.getTireWarningData() != null) {
            tirePressureDataDto.setBatteryVoltageStatus(
                    tirePressureData.getTireWarningData()
                            .getBatteryVoltageStatus());
            tirePressureDataDto.setIsTimeout(tirePressureData.getTireWarningData()
                    .getIsTimeout());
            tirePressureDataDto.setScheme(
                    tirePressureData.getTireWarningData().getScheme());
            tirePressureDataDto.setTirePressureStatus(
                    tirePressureData.getTireWarningData()
                            .getTirePressureStatus());
            tirePressureDataDto.setTireTemperatureStatus(
                    tirePressureData.getTireWarningData()
                            .getTireTemperatureStatus());
            tirePressureDataDto.setTireStatus(tirePressureData.getTireWarningData()
                    .getTireStatus());
            tirePressureDataDto.setDeviceTime(tirePressureDto.getDeviceTime());
        }
        tirePressureDataDto.setCreateTime(currentDate);
        return tirePressureDataDto;
    }
}
