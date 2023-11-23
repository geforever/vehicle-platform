package org.platform.vehicle.controller;

import org.platform.vehicle.context.GeoLocationContext;
import org.platform.vehicle.context.TirePressureContext;
import org.platform.vehicle.context.TirePressureDataContext;
import org.platform.vehicle.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author gejiawei
 * @Date 2023/11/8 13:25
 */
@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TestController {

    private final GeoLocationContext geoLocationContext;
    private final TirePressureContext tirePressureContext;
    private final TirePressureDataContext tirePressureDataContext;

    /**
     * 手动执行ES入库任务
     */
    @PostMapping("/executeEsTask")
    public BaseResponse executeEsTask() {
        log.info("手动执行ES入库任务");
        // 开始时间
        long startTime = System.currentTimeMillis();
        geoLocationContext.execute();
        tirePressureContext.execute();
        log.info("手动执行ES入库任务结束,耗时:{}", System.currentTimeMillis() - startTime);
        return BaseResponse.success();
    }
}
