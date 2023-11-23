package org.platform.vehicle.web.service;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.platform.vehicle.web.param.TireCheckDataParam;

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
