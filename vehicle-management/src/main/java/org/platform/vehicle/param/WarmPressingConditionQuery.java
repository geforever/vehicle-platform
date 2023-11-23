package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/9/18 11:52
 */
@Setter
@Getter
public class WarmPressingConditionQuery extends PageParam {

    /**
     * 车辆归属
     */
    private Integer customerId;

    /**
     * 车辆归属批量查询
     */
    private List<Integer> customerIds;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 车辆状态:正常不传,传中文:一级高压报警,一级高温报警,一级低压报警,二级高压报警,二级高温报警,二级低压报警,低电压报警,急漏气报警,慢漏气报警,无信号报警
     */
    private String statusStr;

    /**
     * 告警类型:1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
     */
    private Integer status;
}
