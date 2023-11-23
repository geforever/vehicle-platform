package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/11/2 15:15
 */
@Data
public class TirePressureTrendVo {

    /**
     * 轮胎ID
     */
    private Integer tireSiteId;

    /**
     * 轮胎名称
     */
    private String tireSiteName;

    /**
     * 轮胎压力
     */
    private String pressure;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
