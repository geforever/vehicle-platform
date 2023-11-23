package org.platform.vehicle.service;

import java.io.File;

import org.platform.vehicle.base.BaseService;
import org.platform.vehicle.dto.ImportResultInfo;
import org.platform.vehicle.entity.VehicleBrandEntity;

/**
 * @author Sunnykid
 */
public interface VehicleBrandService extends BaseService<VehicleBrandEntity> {

	public ImportResultInfo importData(Integer customerId, File excelFile) throws Exception;

}
