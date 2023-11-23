package org.platform.vehicle.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.dto.ImportResultInfo;
import org.platform.vehicle.entity.VehicleBrandEntity;
import org.platform.vehicle.mapper.VehicleBrandMapper;
import org.platform.vehicle.service.VehicleBrandService;

import lombok.RequiredArgsConstructor;

/**
 * @Author Sunnykid
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleBrandServiceImpl extends BaseServiceImpl<VehicleBrandEntity> implements VehicleBrandService {

	private final VehicleBrandMapper vehicleBrandMapper;

	@Override
	public BaseMapper<VehicleBrandEntity> getMapper() {
		return this.vehicleBrandMapper;
	}

	@Override
	public ImportResultInfo importData(Integer customerId, File excelFile) throws Exception {
		return null;
	}
}
