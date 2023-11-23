package org.platform.vehicle.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.base.BaseServiceImpl;
import org.platform.vehicle.dto.ImportResultInfo;
import org.platform.vehicle.entity.TireSpecEntity;
import org.platform.vehicle.mapper.TireSpecMapper;
import org.platform.vehicle.service.TireSpecService;

import lombok.RequiredArgsConstructor;

/**
 * @Author Sunnykid
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TireSpecServiceImpl extends BaseServiceImpl<TireSpecEntity> implements TireSpecService {

	private final TireSpecMapper tireSpecMapper;

	@Override
	public BaseMapper<TireSpecEntity> getMapper() {
		return this.tireSpecMapper;
	}

	@Override
	public ImportResultInfo importData(Integer customerId, File excelFile) throws Exception {
		return null;
	}
}
