package org.platform.vehicle.context;

import org.platform.vehicle.dto.GeoLocationDto;
import org.platform.vehicle.entity.NewestGeoLocationEntity;
import org.platform.vehicle.mapper.jdbc.NewestGeoLocationJdbc;
import org.platform.vehicle.service.ElasticSearchService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
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
public class GeoLocationContext {

    private final ElasticSearchService elasticSearchService;
    private final NewestGeoLocationJdbc newestGeoLocationJdbc;

    @Value("${vehicle.execute_batch_size}")
    public Integer BATCH_SIZE;
    private static final Queue<GeoLocationDto> GEO_LOCATION_QUEUE = new ConcurrentLinkedQueue<>();


    @Async("esTaskExecutor")
    public void push(GeoLocationDto geoLocationDto) {
        GEO_LOCATION_QUEUE.add(geoLocationDto);
        event();
    }

    public void event() {
        log.info("执行车辆位置信息入库事件, GEO_LOCATION_QUEUE.size:{}", GEO_LOCATION_QUEUE.size());
        if (GEO_LOCATION_QUEUE.size() >= BATCH_SIZE) {
            this.execute();
        }
    }

    public void execute() {
        List<GeoLocationDto> geoLocationDtoList = new ArrayList<>();
        for (int i = 0; i < BATCH_SIZE; i++) {
            GeoLocationDto geoLocationDto = GEO_LOCATION_QUEUE.poll();
            if (geoLocationDto != null) {
                geoLocationDtoList.add(geoLocationDto);
            }
        }
        try {
            elasticSearchService.saveGeoLocation(geoLocationDtoList);
        } catch (Exception e) {
            log.error("ES保存车辆位置信息失败", e);
        }
        try {
            this.saveNewestData(geoLocationDtoList);
        } catch (Exception e) {
            log.error("mysql保存车辆最新位置信息失败", e);
        }
    }

    private void saveNewestData(List<GeoLocationDto> geoLocationDtoList) {
        if (geoLocationDtoList.isEmpty()) {
            return;
        }
        Set<NewestGeoLocationEntity> insertOrUpdateSet = new HashSet<>();
        for (GeoLocationDto geoLocationDto : geoLocationDtoList) {
            NewestGeoLocationEntity newestGeoLocation = this.getNewestGeoLocationEntity(
                    geoLocationDto);
            this.addInsertOrUpdateSet(insertOrUpdateSet, newestGeoLocation);
        }
        try {
            newestGeoLocationJdbc.insertOnDuplicateKeyUpdate(insertOrUpdateSet);
        } catch (SQLException e) {
            log.error("批量插入或更新车辆最新位置信息失败", e);
        }
    }

    private void addInsertOrUpdateSet(Set<NewestGeoLocationEntity> insertOrUpdateSet,
            NewestGeoLocationEntity entity) {
        if (insertOrUpdateSet.contains(entity)) {
            insertOrUpdateSet.remove(entity);
            insertOrUpdateSet.add(entity);
        } else {
            insertOrUpdateSet.add(entity);
        }
    }

    private NewestGeoLocationEntity getNewestGeoLocationEntity(
            GeoLocationDto geoLocationDto) {
        NewestGeoLocationEntity newestGeoLocation = new NewestGeoLocationEntity();
        newestGeoLocation.setReceiverId(geoLocationDto.getReceiverId());
        newestGeoLocation.setMsgId(geoLocationDto.getMsgId());
        newestGeoLocation.setLng(geoLocationDto.getLng());
        newestGeoLocation.setLat(geoLocationDto.getLat());
        newestGeoLocation.setSpeed(geoLocationDto.getSpeed());
        newestGeoLocation.setAltitude(geoLocationDto.getAltitude());
        newestGeoLocation.setWarnBit(geoLocationDto.getWarnBit());
        newestGeoLocation.setStatusBit(geoLocationDto.getStatusBit());
        newestGeoLocation.setDeviceTime(geoLocationDto.getDeviceTime());
        return newestGeoLocation;
    }
}
