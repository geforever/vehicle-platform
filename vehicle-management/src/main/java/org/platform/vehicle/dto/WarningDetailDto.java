package org.platform.vehicle.dto;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/31 13:25
 */
@Data
public class WarningDetailDto {

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 轮位
     */
    private Integer tireSite;

    /**
     * 告警类型:1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
     */
    private Integer warningType;
}
