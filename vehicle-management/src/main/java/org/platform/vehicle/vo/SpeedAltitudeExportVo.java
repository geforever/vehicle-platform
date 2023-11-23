package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/3 12:16
 */
@Data
public class SpeedAltitudeExportVo {

    /**
     * 车牌号
     */
    @ExcelProperty("车牌号")
    private String licensePlate;

    /**
     * 时间
     */
    @ExcelProperty("时间")
    private String deviceTime;

    /**
     * 速度
     */
    @ExcelProperty("速度")
    private String speed;

    /**
     * 海拔
     */
    @ExcelProperty("海拔")
    private String altitude;

    /**
     * 位置
     */
    @ExcelProperty("位置")
    private String location;
}
