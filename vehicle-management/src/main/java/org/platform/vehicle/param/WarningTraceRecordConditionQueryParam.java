package org.platform.vehicle.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.response.PageParam;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/28 09:28
 */
@Getter
@Setter
public class WarningTraceRecordConditionQueryParam extends PageParam {

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 车队
     */
    private String fleetName;

    /**
     * 类型:1-紧急类,2-常规类
     *
     * @required
     */
    private Integer type;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 报警类型:高压报警、高温报警、低压报警、电压低报警、无信号报警、二级高温报警、二级高压报警、二级低压报警、急漏气报警
     */
    private String warningType;

    /**
     * 是否跟进:0-未跟进，1-已跟进
     */
    private Integer isFollow;
}
