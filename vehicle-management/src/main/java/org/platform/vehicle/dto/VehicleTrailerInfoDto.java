package org.platform.vehicle.dto;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/27 14:28
 */
@Data
public class VehicleTrailerInfoDto {

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 接收器ID
     */
    private String receiverIdNumber;

    /**
     * 主车车牌
     */
    private String mainLicensePlate;

    /**
     * 副车车牌
     */
    private String minorLicensePlate;

    /**
     * 主车中继器ID
     */
    private String mainRelayId;

    /**
     * 副车中继器ID
     */
    private String minorRelayId;

    /**
     * 主车挂车绑定类型:0-未绑定,1-主车挂车,2-挂车主车
     */
    private Integer type;

//    /**
//     * 告警类型:0-正常,1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
//     */
//    private Integer warningType;

    private List<WarningDetailDto> warningDetailList;
    /**
     * 车型分类:1-主车,2-挂车,3-主挂一体
     */
    private Integer specType;

}
