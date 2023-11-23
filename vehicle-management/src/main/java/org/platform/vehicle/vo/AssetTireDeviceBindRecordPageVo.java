package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/15 15:53
 */
@Data
public class AssetTireDeviceBindRecordPageVo {

    private Integer id;

    /**
     * 各个配件唯一编号
     */
    private String code;

    /**
     * 设备
     */
    private String deviceType;

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 轮位号
     */
    private String tireSiteName;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
