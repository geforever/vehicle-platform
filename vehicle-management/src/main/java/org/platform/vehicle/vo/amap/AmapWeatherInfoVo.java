package org.platform.vehicle.vo.amap;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 高德天气信息
 *
 * @Author gejiawei
 * @Date 2023/10/11 16:04
 */
@Getter
@Setter
public class AmapWeatherInfoVo extends AmapBaseVo {


    /**
     * 返回结果总数目
     */
    private String count;

    /**
     * 实时天气
     */
    private List<LiveVo> lives;
}
