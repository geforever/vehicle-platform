package org.platform.vehicle.service;

import reactor.core.publisher.Mono;

/**
 * @Author gejiawei
 * @Date 2023/11/6 14:36
 */
public interface Jt808FeignService {

    /**
     * 挂车上挂
     *
     * @param clientId
     */
    Mono<String> trailerHangOn(String clientId, String chuanganqiId);

    /**
     * 挂车下挂
     *
     * @param clientId
     * @return
     */
    Mono<String> trailerHangUnder(String clientId);

    /**
     * 同步阈值
     *
     * @param clientId
     * @param thresholdJson
     * @return
     */
    Mono<String> syncInterval(String clientId, String thresholdJson);

    /**
     * 绑定中继器
     *
     * @param vehicleType 车辆类型（1表示主车；2表示挂车）
     * @param clientId
     * @param repeaterId
     * @return
     */
    Mono<String> bindRepeater(Integer vehicleType, String clientId, String repeaterId);

    /**
     * 同步轮位
     *
     * @param cheType
     * @param wheelNum
     * @param clientId
     * @param sensorId
     * @return
     */
    void syncWheel(Integer cheType, Integer wheelNum, String clientId, String sensorId);
}
