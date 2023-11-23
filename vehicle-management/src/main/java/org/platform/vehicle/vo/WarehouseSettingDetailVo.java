package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/13 14:29
 */
@Data
public class WarehouseSettingDetailVo {

    private Integer id;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 车队ID
     */
    private Integer customerId;

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 库存最小值
     */
    private Integer stockMin;

    /**
     * 库存最大值
     */
    private Integer stockMax;


    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
