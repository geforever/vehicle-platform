package org.platform.vehicle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.entity.DriverEntity;
import org.platform.vehicle.mapper.DriverMapper;
import org.platform.vehicle.service.DriverService;

import lombok.RequiredArgsConstructor;

/**
 * @Author Sunnykid
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DriverServiceImpl extends BaseServiceImpl<DriverEntity> implements DriverService {

	private final DriverMapper driverMapper;

	@Override
	public BaseMapper<DriverEntity> getMapper() {
		return this.driverMapper;
	}

}
