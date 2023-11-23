package org.platform.vehicle.service.impl;

import org.platform.vehicle.feign.VehicleManagementFeignClient;
import org.platform.vehicle.param.TireCheckDataParam;
import org.platform.vehicle.service.VehicleManagementFeignClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/11/6 15:43
 */
@Service
public class VehicleManagementFeignClientServiceImpl implements
        VehicleManagementFeignClientService {

    @Autowired
    private VehicleManagementFeignClient vehicleManagementFeignClient;

    @Override
    public void checkData(List<TireCheckDataParam> paramList) {
        vehicleManagementFeignClient.checkData(paramList);
    }
}
