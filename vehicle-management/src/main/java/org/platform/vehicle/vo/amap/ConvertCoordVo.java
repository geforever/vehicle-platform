package org.platform.vehicle.vo.amap;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/10/11 16:32
 */
@Getter
@Setter
public class ConvertCoordVo extends AmapBaseVo {

    /**
     * 经度和纬度用","分割，经度在前，纬度在后，经纬度小数点后不得超过6位。多个坐标对之间用”|”进行分隔最多支持40对坐标。
     */
    private String locations;

}
