package org.platform.vehicle.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.platform.vehicle.web.feign.VehicleManagementFeignClient;
import org.platform.vehicle.web.param.TireCheckDataParam;
import org.platform.vehicle.web.service.VehicleManagementFeignClientService;

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
