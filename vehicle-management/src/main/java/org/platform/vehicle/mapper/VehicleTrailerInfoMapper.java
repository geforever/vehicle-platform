package org.platform.vehicle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.dto.VehicleTrailerInfoDto;
import org.platform.vehicle.entity.VehicleTrailerInfo;
import org.platform.vehicle.param.WarmPressingConditionQuery;
import org.springframework.stereotype.Repository;

/**
 * @author gejiawei
 */
@Repository
public interface VehicleTrailerInfoMapper extends BaseMapper<VehicleTrailerInfo> {

    Page<VehicleTrailerInfoDto> conditionQuery(Page<VehicleTrailerInfoDto> page,
            WarmPressingConditionQuery param);

}
