package org.platform.vehicle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.entity.VehicleSpecMaintenaceEntity;
import org.platform.vehicle.mapper.VehicleSpecMaintenaceMapper;
import org.platform.vehicle.service.VehicleSpecMaintenaceService;

import lombok.RequiredArgsConstructor;

/**
 * @Author Sunnykid
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleSpecMaintenaceServiceImpl extends BaseServiceImpl<VehicleSpecMaintenaceEntity> implements VehicleSpecMaintenaceService {

	private final VehicleSpecMaintenaceMapper vehicleSpecMaintenaceMapper;

	@Override
	public BaseMapper<VehicleSpecMaintenaceEntity> getMapper() {
		return this.vehicleSpecMaintenaceMapper;
	}

}
