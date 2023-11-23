package org.platform.vehicle.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.response.PageParam;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/1 10:09
 */
@Getter
@Setter
public class OperateLogConditionQueryParam extends PageParam {

    /**
     * 操作日志类别:1-新增,2-修改,3-删除
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
}
