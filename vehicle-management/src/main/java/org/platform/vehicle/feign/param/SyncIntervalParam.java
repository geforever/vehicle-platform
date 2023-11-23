package org.platform.vehicle.feign.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/20 11:29
 */
@Data
public class SyncIntervalParam {

    /**
     * 当前设置车辆轴总数
     */
    private Integer zhoushu;

    /**
     * 高温阈值
     */
    private Integer gaowen;

    /**
     * 压力阈值
     */
    private List<TirePressureIntervalParam> taiya;
}
