package org.platform.vehicle.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.dto.AssetTireFitDto;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.param.AssetTireFitConditionQuery;
import org.springframework.stereotype.Repository;

/**
 * @author Sunnykid
 */
@Repository
public interface AssetTireMapper extends CommonMapper<AssetTire> {

    Page<AssetTireFitDto> fitConditionQuery(Page<AssetTireFitDto> page,
            AssetTireFitConditionQuery param);

}
