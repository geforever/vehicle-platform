package org.platform.vehicle.service;

import java.io.File;

import org.platform.vehicle.base.BaseService;
import org.platform.vehicle.dto.ImportResultInfo;
import org.platform.vehicle.entity.VehicleEntity;

/**
 * @author Sunnykid
 */
public interface VehicleService extends BaseService<VehicleEntity> {

	public ImportResultInfo importData(Integer customerId, File excelFile) throws Exception;

}
