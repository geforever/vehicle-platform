package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/28 10:37
 */
@Data
public class WarningTraceDetailRecordVo {

    private Integer id;

    /**
     * 流水号
     */
    private String traceNo;

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 轮位
     */
    private Integer tireSite;

    /**
     * 轮位名称
     */
    private String tireSiteName;

    /**
     * 告警类型
     */
    private Integer warningType;

    /**
     * 压力
     */
    private String pressure;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 压力阈值
     */
    private String pressureThreshold;

    /**
     * 温度阈值
     */
    private String temperatureThreshold;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
