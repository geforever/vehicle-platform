package org.platform.vehicle.repository;

import org.platform.vehicle.dto.TirePressureDataDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author gejiawei
 * @Date 2023/11/9 11:50
 */

@Repository
public interface TirePressureDataRepository extends
        ElasticsearchRepository<TirePressureDataDto, Long> {

}
