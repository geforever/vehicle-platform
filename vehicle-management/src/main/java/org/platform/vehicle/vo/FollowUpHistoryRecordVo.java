package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/10/9 10:53
 */
@Data
public class FollowUpHistoryRecordVo {

    /**
     * 车牌号
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
     * 司机名称
     */
    private String driverName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 是否运营中:0-否,1-是
     */
    private Integer isRunning;

    /**
     * 运营开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runningStartTime;

    /**
     * 运营结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runningEndTime;

    /**
     * 跟进历史记录
     */
    private List<FollowUpHistoryRecordDetailVo> followUpHistoryRecordDetailVoList;

}
