package org.platform.vehicle.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.dto.ImportResultInfo;
import org.platform.vehicle.entity.TireBrandEntity;
import org.platform.vehicle.mapper.TireBrandMapper;
import org.platform.vehicle.service.TireBrandService;

import lombok.RequiredArgsConstructor;

/**
 * @Author Sunnykid
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TireBrandServiceImpl extends BaseServiceImpl<TireBrandEntity> implements TireBrandService {

	private final TireBrandMapper tireBrandMapper;

	@Override
	public BaseMapper<TireBrandEntity> getMapper() {
		return this.tireBrandMapper;
	}

	@Override
	public ImportResultInfo importData(Integer customerId, File excelFile) throws Exception {
		return null;
	}
}
