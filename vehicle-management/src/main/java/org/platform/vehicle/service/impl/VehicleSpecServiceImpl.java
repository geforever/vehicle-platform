package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.conf.VehicleSpecContext;
import org.platform.vehicle.dto.ImportResultInfo;
import org.platform.vehicle.entity.VehicleBrandEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.mapper.VehicleBrandMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.service.VehicleSpecService;
import org.platform.vehicle.vo.context.VehicleSpecContextVo;
import org.platform.vehicle.enums.VehicleSpeciesTypeEnum;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Sunnykid
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleSpecServiceImpl extends BaseServiceImpl<VehicleSpecEntity> implements
        VehicleSpecService {

    private final VehicleSpecMapper vehicleSpecMapper;
    private final VehicleBrandMapper vehicleBrandMapper;
    private final VehicleSpecContext vehicleSpecContext;

    @Override
    public BaseMapper<VehicleSpecEntity> getMapper() {
        return this.vehicleSpecMapper;
    }

    @Override
    public ImportResultInfo importData(Integer customerId, File excelFile) throws Exception {
        return null;
    }

    private void updateSpecContext(VehicleSpecEntity entity) {
        vehicleSpecContext.add(entity);
    }

}
