package org.platform.vehicle.service.impl;

import static org.platform.vehicle.constant.SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE;
import static org.platform.vehicle.constant.SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.conf.TrailerVehicleContext;
import org.platform.vehicle.conf.VehicleContext;
import org.platform.vehicle.dto.ImportResultInfo;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.service.VehicleService;
import org.platform.vehicle.utils.UserContext;
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
public class VehicleServiceImpl extends BaseServiceImpl<VehicleEntity> implements VehicleService {

    private final VehicleMapper vehicleMapper;
    private final VehicleSpecMapper vehicleSpecMapper;
    private final SysCustomerMapper sysCustomerMapper;
    private final VehicleContext vehicleContext;
    private final TrailerVehicleContext trailerVehicleContext;

    @Override
    public BaseMapper<VehicleEntity> getMapper() {
        return this.vehicleMapper;
    }

    @Override
    public ImportResultInfo importData(Integer customerId, File excelFile) throws Exception {
        return null;
    }
}
