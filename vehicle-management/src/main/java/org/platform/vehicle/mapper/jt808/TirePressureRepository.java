package org.platform.vehicle.mapper.jt808;

import org.platform.vehicle.entity.JT808.TirePressureDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author gejiawei
 * @Date 2023/11/9 11:50
 */

@Repository
public interface TirePressureRepository extends ElasticsearchRepository<TirePressureDto, Long> {

}
