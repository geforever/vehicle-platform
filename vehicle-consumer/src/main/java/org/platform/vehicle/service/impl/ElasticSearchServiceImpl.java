package org.platform.vehicle.service.impl;

import com.alibaba.fastjson.JSON;
import org.platform.vehicle.dto.GeoLocationDto;
import org.platform.vehicle.dto.TirePressureDataDto;
import org.platform.vehicle.dto.TirePressureDto;
import org.platform.vehicle.service.ElasticSearchService;
import org.platform.vehicle.util.EsHelper;
import org.platform.vehicle.utils.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/11/9 10:39
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final RestHighLevelClient restHighLevelClient;
    private final EsHelper esHelper;

    @Override
    public void saveGeoLocation(List<GeoLocationDto> geoLocationDtoList) {
        if (geoLocationDtoList.isEmpty()) {
            return;
        }
        log.info("ES-开始批量插入车辆位置信息,size:{}", geoLocationDtoList.size());
        // 开始时间
        long start = System.currentTimeMillis();
        this.batchInsertLocation(geoLocationDtoList);
        log.info("ES-结束批量插入车辆位置信息, 耗时:{}", System.currentTimeMillis() - start);
    }

    private void batchInsertLocation(List<GeoLocationDto> list) {
        BulkProcessor bulkRequest = null;
        try {
            bulkRequest = esHelper.bulkProcessor();
            for (GeoLocationDto geoLocationDto : list) {
                esHelper.createIndex("location_fleet_" + geoLocationDto.getFleetId());
                // 将日期字段格式化为指定格式的字符串
                String formattedDate = DateUtil.dateToStr(geoLocationDto.getDeviceTime(),
                        DateUtil.STANDARD_DATE_TIME_PATTERN);
                // 设置日期字段
                geoLocationDto.setDeviceTime(
                        DateUtil.parseDate(formattedDate, DateUtil.STANDARD_DATE_TIME_PATTERN));
                bulkRequest.add(
                        new IndexRequest("location_fleet_" + geoLocationDto.getFleetId(), "_doc")
                                .source(JSON.toJSONString(geoLocationDto), XContentType.JSON));
            }
            bulkRequest.flush();
        } catch (Exception e) {
            log.error("批量插入ES失败", e);
        } finally {
            if (bulkRequest != null) {
                bulkRequest.close();
            }
        }
    }


    @Override
    public void saveTirePressure(List<TirePressureDto> tirePressureDtoList) {
        if (tirePressureDtoList.isEmpty()) {
            return;
        }
        log.info("ES-开始批量插入胎压信息,size:{}", tirePressureDtoList.size());
        // 开始时间
        long start = System.currentTimeMillis();
        this.batchInsertTirePressure(tirePressureDtoList);
        log.info("ES-结束批量插入胎压信息, 耗时:{}", System.currentTimeMillis() - start);
    }

    private void batchInsertTirePressure(List<TirePressureDto> list) {
        BulkProcessor bulkRequest = null;
        try {
            bulkRequest = esHelper.bulkProcessor();
            for (TirePressureDto tirePressureDto : list) {
                esHelper.createIndex("tire_fleet_" + tirePressureDto.getFleetId());
                bulkRequest.add(
                        new IndexRequest("tire_fleet_" + tirePressureDto.getFleetId(), "_doc")
                                .source(JSON.toJSONString(tirePressureDto), XContentType.JSON));
            }
            bulkRequest.flush();
        } catch (Exception e) {
            log.error("ES-批量插入胎压信息失败", e);
        } finally {
            if (bulkRequest != null) {
                bulkRequest.close();
            }
        }
    }

    @Override
    public void saveTirePressureData(List<TirePressureDataDto> tirePressureDataDtoList) {
        if (tirePressureDataDtoList.isEmpty()) {
            return;
        }
        log.info("ES-开始批量插入胎压明细数据信息,size:{}", tirePressureDataDtoList.size());
        // 开始时间
        long start = System.currentTimeMillis();
        this.batchInsertTirePressureData(tirePressureDataDtoList);
        log.info("ES-结束批量插入胎压明细数据信息, 耗时:{}", System.currentTimeMillis() - start);
    }

    private void batchInsertTirePressureData(List<TirePressureDataDto> list) {
        BulkProcessor bulkRequest = null;
        try {
            bulkRequest = esHelper.bulkProcessor();
            for (TirePressureDataDto tirePressureDataDto : list) {
                esHelper.createIndex("tire_data_fleet_" + tirePressureDataDto.getFleetId());
                bulkRequest.add(
                        new IndexRequest("tire_data_fleet_" + tirePressureDataDto.getFleetId(),
                                "_doc")
                                .source(JSON.toJSONString(tirePressureDataDto), XContentType.JSON));
            }
            bulkRequest.flush();
        } catch (Exception e) {
            log.error("ES-批量插入胎压明细数据信息失败", e);
        } finally {
            if (bulkRequest != null) {
                bulkRequest.close();
            }
        }
    }
}
