package org.platform.vehicle.web.feign;

import java.util.List;
import org.platform.vehicle.web.param.TireCheckDataParam;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author gejiawei
 * @Date 2023/11/6 14:21
 */
@FeignClient(name = "vehicle-management")
public interface VehicleManagementFeignClient {

    @PostMapping("/warningTraceRecord/checkData")
    void checkData(@RequestBody List<TireCheckDataParam> paramList);
}
