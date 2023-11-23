package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/14 14:56
 */
@Data
public class AssetTirePageExportVo {

    /**
     * 轮胎编号
     */
    @ExcelProperty("轮胎编号")
    private String code;

    /**
     * 客户名称
     */
    @ExcelProperty("客户名称")
    private String clientName;

    /**
     * 车队名称
     */
    @ExcelProperty("车队名称")
    private String fleetName;

    /**
     * 仓库名称
     */
    @ExcelProperty("仓库名称")
    private String warehouseName;

    /**
     * 安装车辆
     */
    @ExcelProperty("安装车辆")
    private String licensePlate;

    /**
     * 轮位
     */
    @ExcelProperty("轮位")
    private String tireSiteName;

    /**
     * 轮位类型
     */
    @ExcelProperty("轮位类型")
    private String tireSiteTypeName;

    /**
     * 轮胎品牌名称
     */
    @ExcelProperty("轮胎品牌名称")
    private String tireBrandName;

    /**
     * 轮胎型号
     */
    @ExcelProperty("轮胎型号")
    private String tireSpecName;

    /**
     * 新旧程度(30,50,70,100)
     */
    @ExcelProperty("新旧程度")
    private String degree;

    /**
     * 当前里程KM
     */
    @ExcelProperty("当前里程KM")
    private String mileage;

    /**
     * 传感器ID
     */
    @ExcelProperty("传感器ID")
    private String sensorId;

    /**
     * 轮胎状态:1-仓库待用,2-使用中,3-已变卖,4-已调拨
     */
    @ExcelProperty("轮胎状态")
    private String tireStatusStr;

    /**
     * 更新人
     */
    @ExcelProperty("修改人")
    private String updatePerson;

    /**
     * 更新时间
     */
    @ExcelProperty("休息时间")
    private String updateTime;

}
