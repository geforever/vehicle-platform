package org.platform.vehicle.repository;

import org.platform.vehicle.dto.GeoLocationDto;
import org.platform.vehicle.dto.TirePressureDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author gejiawei
 * @Date 2023/11/9 11:50
 */

@Repository
public interface TirePressureRepository extends ElasticsearchRepository<TirePressureDto, Long> {

}
