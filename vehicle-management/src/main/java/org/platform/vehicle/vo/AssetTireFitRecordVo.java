package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/20 14:39
 */
@Data
public class AssetTireFitRecordVo {

    private Integer id;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 轮位名称
     */
    private String tireSiteName;

    /**
     * 品牌
     */
    private String brandName;

    /**
     * 类型:1-安装,2-拆卸
     */
    private Integer type;

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
