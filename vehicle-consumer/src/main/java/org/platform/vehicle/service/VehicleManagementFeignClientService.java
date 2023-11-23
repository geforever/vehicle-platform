package org.platform.vehicle.service;

import org.platform.vehicle.param.TireCheckDataParam;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author gejiawei
 * @Date 2023/11/6 15:43
 */
public interface VehicleManagementFeignClientService {

    /**
     * 轮胎数据校验
     *
     * @param paramList
     */
    void checkData(@RequestBody List<TireCheckDataParam> paramList);

}
