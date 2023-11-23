package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/9/15 15:54
 */
@Getter
@Setter
public class AssetTireDeviceBindRecordConditionQueryParam extends PageParam {

    /**
     * ID
     */
    private String code;

    /**
     * 设备
     */
    private String deviceType;

    /**
     * 车牌号
     */
    private String licensePlate;

}
