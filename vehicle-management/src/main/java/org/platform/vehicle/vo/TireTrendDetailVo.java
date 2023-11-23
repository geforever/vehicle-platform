package org.platform.vehicle.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/17 13:35
 */
@Data
public class TireTrendDetailVo {

    /**
     * 轮位名称
     */
    private String tireSiteName;

    /**
     * 轮胎压力温度趋势数据
     */
    private List<TireTrendDetailDataVo> tireTrendDetailDataList = new ArrayList<>();

}
