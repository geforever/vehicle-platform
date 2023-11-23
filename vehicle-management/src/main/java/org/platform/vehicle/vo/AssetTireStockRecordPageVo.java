package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/15 17:22
 */
@Data
public class AssetTireStockRecordPageVo {

    private Integer id;

    /**
     * 轮胎编号
     */
    private String tireCode;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 车队名称
     */
    private String fleetName;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 库存记录类型:1-入库,2-出库
     */
    private Integer type;

    /**
     * 该记录出入库类型类型: 入库类型:1:采集,2赔付,3:拆卸入库,4-调拨入库 出库类型:1:安装出库,2-调拨出库,3-变卖出库
     */
    private Integer stockType;

    /**
     * 供给方/接收方
     */
    private String target;

    /**
     * 轮胎品牌
     */
    private String tireBrand;

    /**
     * 轮胎型号
     */
    private String tireSpec;

    /**
     * 新旧程度(30,50,70,100)
     */
    private String degree;

    /**
     * 当前里程
     */
    private Integer mileage;

    /**
     * 备注
     */
    private String remark;

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
