package org.platform.vehicle.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.response.PageParam;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/20 14:31
 */
@Getter
@Setter
public class AssetTireFitRecordConditionQueryParam extends PageParam {

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 类型:1-安装,2-拆卸
     */
    private Integer type;

    /**
     * 开始时间(yyyy-MM-dd HH:mm:ss)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createStartTime;

    /**
     * 结束时间(yyyy-MM-dd HH:mm:ss)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createEndTime;
}
