package org.platform.vehicle.mapper.jt808;

import org.platform.vehicle.entity.JT808.TirePressureDataDto;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author gejiawei
 * @Date 2023/11/9 11:50
 */

@Repository
public interface TirePressureDataRepository extends
        ElasticsearchRepository<TirePressureDataDto, Long> {

    List<TirePressureDataDto> findByClientIdAndTireSiteIdInAndTypeInAndDeviceTimeBetweenOrderByDeviceTime(
            String clientId, List<String> tireSiteIds, List<Integer> types, String startTime,
            String endTime);

    List<TirePressureDataDto> findByClientIdAndDeviceTimeBetweenOrderByDeviceTime(
            String gpsId, String startDate, String endDate);
}
