package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/28 09:28
 */
@Data
public class WarningTraceRecordVo {

    private Integer id;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 车队ID
     */
    private Integer fleetId;

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 客户ID
     */
    private Integer clientId;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 轮位名称
     */
    private String tireSiteName;

    /**
     * 报警类型:逗号分割
     */
    private String warningType;

    /**
     * 压力
     */
    private String pressure;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 电压
     */
    private String voltage;

    /**
     * 当前地址
     */
    private String location;

    /**
     * 跟进类型:1-紧急类,2-常规类
     */
    private Integer type;

    /**
     * 报警时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否跟进:0-否,1-是
     */
    private Integer isFollow;

    /**
     * 首次跟进时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date followTime;

    /**
     * 原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 跟进人
     */
    private String followPerson;

}
