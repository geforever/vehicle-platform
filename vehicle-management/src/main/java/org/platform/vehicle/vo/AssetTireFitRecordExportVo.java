package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/16 17:09
 */
@Data
public class AssetTireFitRecordExportVo {

    /**
     * 所属客户
     */
    @ExcelProperty("所属客户")
    private String clientName;

    /**
     * 车队
     */
    @ExcelProperty("车队")
    private String fleetName;

    /**
     * 车牌号
     */
    @ExcelProperty("车牌号")
    private String licensePlate;

    /**
     * 轮胎号
     */
    @ExcelProperty("轮胎号")
    private String tireCode;

    /**
     * 轮位
     */
    @ExcelProperty("轮位")
    private String tireSiteName;

    /**
     * 品牌
     */
    @ExcelProperty("品牌")
    private String brandName;

    /**
     * 类型:1-安装,2-拆卸
     */
    @ExcelProperty("类型")
    private String typeStr;

    /**
     * 创建人
     */
    @ExcelProperty("创建人")
    private String createPerson;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private String createTime;
}
