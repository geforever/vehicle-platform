package org.platform.vehicle.service.impl;

import org.platform.vehicle.feign.Jt808FeignClient;
import org.platform.vehicle.service.Jt808FeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @Author gejiawei
 * @Date 2023/11/6 14:36
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Jt808FeignServiceImpl implements Jt808FeignService {

    private final Jt808FeignClient jt808FeignClient;

    /**
     * 挂车上挂
     *
     * @param clientId
     * @return
     */
    @Override
    public Mono<String> trailerHangOn(String clientId, String chuanganqiId) {
        return jt808FeignClient.trailerHangOn(clientId, chuanganqiId);
    }

    /**
     * 挂车下挂
     *
     * @param clientId
     * @return
     */
    @Override
    public Mono<String> trailerHangUnder(String clientId) {
        return jt808FeignClient.trailerHangUnder(clientId);
    }

    /**
     * 同步阈值
     *
     * @param clientId
     * @param thresholdJson
     * @return
     */
    @Override
    public Mono<String> syncInterval(String clientId, String thresholdJson) {
        return jt808FeignClient.syncInterval(clientId, thresholdJson);
    }

    /**
     * 绑定中继器
     *
     * @param vehicleType 车辆类型（1表示主车；2表示挂车）
     * @param clientId
     * @param repeaterId
     * @return
     */
    @Override
    public Mono<String> bindRepeater(Integer vehicleType, String clientId, String repeaterId) {
        return jt808FeignClient.bindRepeater(vehicleType, clientId, repeaterId);
    }

    /**
     * 同步轮位
     *
     * @param cheType
     * @param wheelNum
     * @param clientId
     * @param sensorId
     * @return
     */
    @Override
    public void syncWheel(Integer cheType, Integer wheelNum, String clientId,
            String sensorId) {
        jt808FeignClient.syncWheel(cheType, wheelNum, clientId, sensorId);
    }
}
