package org.platform.vehicle.mapper.jt808;

import org.platform.vehicle.entity.JT808.GeoLocationDto;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author gejiawei
 * @Date 2023/11/9 11:50
 */

@Repository
public interface GeoLocationRepository extends ElasticsearchRepository<GeoLocationDto, Long> {

    List<GeoLocationDto> findByReceiverIdAndDeviceTimeBetweenOrderByDeviceTimeDesc(String gpsId,
            String startDate, String endDate);

    List<GeoLocationDto> findByReceiverIdOrderByDeviceTimeDesc(String gpsId);

}
