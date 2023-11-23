package org.platform.vehicle.task;

import org.platform.vehicle.context.GeoLocationContext;
import org.platform.vehicle.context.TirePressureContext;
import org.platform.vehicle.context.TirePressureDataContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author gejiawei
 * @Date 2023/11/9 13:46
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EsDataTask {

    private final GeoLocationContext geoLocationContext;
    private final TirePressureContext tirePressureContext;
    private final TirePressureDataContext tirePressureDataContext;

    // 一分钟执行一次
    @Scheduled(cron = "0 */1 * * * ?")
    // 5秒执行一次
//    @Scheduled(cron = "*/5 * * * * ?")
    public void sendGeoLocationData() {
        long startTime = System.currentTimeMillis();
        log.info("定时任务:车辆位置信息入库开始执行");
        geoLocationContext.execute();
        tirePressureContext.execute();
        log.info("定时任务:车辆位置信息入库执行结束,耗时:{}",
                System.currentTimeMillis() - startTime);
    }
}

