package org.platform.vehicle.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

/**
 * @Author gejiawei
 * @Date 2023/11/6 14:21
 */
@FeignClient(name = "vehicle-jt808")
public interface Jt808FeignClient {


    /**
     * 挂车上挂
     *
     * @param clientId
     * @return
     */
    @PostMapping("/mx/shanggua")
    void trailerHangOn(@RequestParam String clientId, String chuanganqiId);

    /**
     * 挂车下挂
     *
     * @param clientId
     * @return
     */
    @PostMapping("/mx/xiagua")
    void trailerHangUnder(@RequestParam String clientId);

    /**
     * 同步阈值
     *
     * @param clientId
     * @param thresholdJson
     * @return
     */
    @PostMapping("/mx/yuzhi")
    void syncInterval(@RequestParam String clientId, @RequestParam String thresholdJson);

    /**
     * 下发中继器绑定指令
     *
     * @param cheType     车辆类型（1表示主车；2表示挂车）
     * @param clientId    GPS ID（12位的数字或字母）
     * @param zhongjiqiId 中继器ID（6位的数字或字母）
     **/
    @PostMapping("/mx/zhongjiqi")
    void bindRepeater(@RequestParam Integer cheType, @RequestParam String clientId,
            @RequestParam String zhongjiqiId);

    /**
     * 下发中继器绑定指令
     *
     * @param cheType      车辆类型（1表示主车；2表示挂车）
     * @param luntaiNum    轮胎序号（ 01～20 有效，1-32）
     * @param clientId     GPS ID（12位的数字或字母）
     * @param chuanganqiId 传感器ID（6位的数字或字母）
     **/
    @PostMapping("/mx/lunwei")
    void syncWheel(@RequestParam Integer cheType, @RequestParam Integer luntaiNum,
            @RequestParam String clientId, @RequestParam String chuanganqiId);

}
