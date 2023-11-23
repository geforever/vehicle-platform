package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/25 12:11
 */
@Data
public class WarningRemarkTemplateVo {

    private Integer id;

    /**
     * 客户ID:0-默认模版
     */
    private Integer clientId;

    /**
     * 告警类型:1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
     */
    private Integer type;

    /**
     * 故障原因
     */
    private String reason;
}
