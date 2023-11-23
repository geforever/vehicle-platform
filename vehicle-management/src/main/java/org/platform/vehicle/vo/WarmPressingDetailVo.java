package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/12 13:44
 */
@Data
public class WarmPressingDetailVo {

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 挂车车牌号
     */
    private String guaLicensePlate;

    /**
     * 车辆状态: 1-行驶中,2-已停车,3-已断电
     */
    private Integer vehicleStatus;

    /**
     * GPS_ID
     */
    private String gpsId;

    /**
     * 主车中继器ID
     */
    private String mainRepeaterId;

    /**
     * 挂车中继器ID
     */
    private String guaRepeaterId;

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 车辆线路
     */
    private String vehicleLine;

    /**
     * 当前位置
     */
    private String currentLocation;

    /**
     * 当地温度
     */
    private String localTemperature;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 车辆类型: 1-主车,2-挂车
     */
    private Integer specType;

    /**
     * 主车轮胎列表
     */
    private List<WarmPressingTireVo> mainTireList;

    /**
     * 挂车轮胎列表
     */
    private List<WarmPressingTireVo> guaTireList;

}
