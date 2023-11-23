package org.platform.vehicle.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.response.PageParam;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/28 10:35
 */
@Getter
@Setter
public class WarningDetailRecordConditionQueryParam extends PageParam {

    /**
     * 跟进记录流水号
     *
     * @required
     */
    private String traceNo;

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

}
