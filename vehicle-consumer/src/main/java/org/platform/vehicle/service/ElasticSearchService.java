package org.platform.vehicle.service;

import org.platform.vehicle.dto.GeoLocationDto;
import org.platform.vehicle.dto.TirePressureDataDto;
import org.platform.vehicle.dto.TirePressureDto;
import java.util.List;

/**
 * @Author gejiawei
 * @Date 2023/11/9 10:40
 */
public interface ElasticSearchService {


    /**
     * 保存车辆位置信息
     *
     * @param geoLocationDtoList
     */
    void saveGeoLocation(List<GeoLocationDto> geoLocationDtoList);

    void saveTirePressure(List<TirePressureDto> tirePressureDtoList);

    void saveTirePressureData(List<TirePressureDataDto> tirePressureDataDtoList);
}
