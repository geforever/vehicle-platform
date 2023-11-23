package org.platform.vehicle.web.model.entity;

import java.util.Date;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/6 16:33
 */
@Data
public class NewestGeoLocationEntity {

    private Integer id;

    /**
     * 接收器ID
     */
    private String receiverId;


    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 速度
     */
    private String speed;

    /**
     * 海拔高度
     */
    private String altitude;

    /**
     * 报警标志
     */
    private Integer warnBit;

    /**
     * 状态
     */
    private Integer statusBit;

    /**
     * 设备时间
     */
    private Date deviceTime;
}
