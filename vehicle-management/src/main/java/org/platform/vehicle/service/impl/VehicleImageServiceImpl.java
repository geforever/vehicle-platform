package org.platform.vehicle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.entity.VehicleImageEntity;
import org.platform.vehicle.mapper.VehicleImageMapper;
import org.platform.vehicle.service.VehicleImageService;

import lombok.RequiredArgsConstructor;

/**
 * @Author Sunnykid
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleImageServiceImpl extends BaseServiceImpl<VehicleImageEntity> implements VehicleImageService {

	private final VehicleImageMapper vehicleImageMapper;

	@Override
	public BaseMapper<VehicleImageEntity> getMapper() {
		return this.vehicleImageMapper;
	}


}
