package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/11/2 16:05
 */
@Data
public class VehicleTrackVo {

    /**
     * 电源: 0-表示使用自备电源,1-表示接入外接电源
     */
    private Integer powerSupply;

    /**
     * 电压
     */
    private String voltage;

    /**
     * 掉电状态: 0-表示未掉电,1-表示掉电
     */
    private Integer powerOffStatus;

    /**
     * 速度
     */
    private String speed;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deviceTime;

    /**
     * 经度
     */
    private String lngLat;

    /**
     * 纬度
     */
    private String latLat;
}
