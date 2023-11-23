package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/19 10:31
 */
@Data
public class VehicleTireDetailVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 轮位(主车备胎轮位为90和91，备胎轮位为 92,93)
     */
    private Integer tireSite;

    /**
     * 轮位名称
     */
    private String tireSiteName;

    /**
     * 轮位分类(轴位)
     */
    private Integer tireSiteType;

    /**
     * 颜色:1-绿色(轮胎已绑传感器), 3-蓝色(轮胎未绑传感器), 0-白色(轮位未安装车轮)
     */
    private Integer color;

    /**
     * 轮胎品牌
     */
    private String tireBrandName;

    /**
     * 传感器ID
     */
    private String sensorId;

    /**
     * 安装时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
