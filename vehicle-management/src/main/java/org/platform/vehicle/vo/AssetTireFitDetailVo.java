package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/19 09:33
 */
@Data
public class AssetTireFitDetailVo {

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * GPS_ID(接收器ID)
     */
    private String receiverIdNumber;

    /**
     * 中继器ID
     */
    private String repeaterIdNumber;

    /**
     * 车辆id
     */
    private Integer vehicleId;

    /**
     * 轮胎明细
     */
    private List<VehicleTireDetailVo> vehicleTireDetailVoList;
}
