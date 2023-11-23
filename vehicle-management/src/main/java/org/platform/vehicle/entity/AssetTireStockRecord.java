package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (AssetTireStockRecord)实体类
 *
 * @author geforever
 * @since 2023-09-15 10:55:52
 */
@Data
@TableName("asset_tire_stock_record")
public class AssetTireStockRecord implements Serializable {

    private static final long serialVersionUID = 763158355863833139L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 轮胎编号
     */
    @TableField("tire_code")
    private String tireCode;

    /**
     * 车队ID
     */
    @TableField("fleet_id")
    private Integer fleetId;

    /**
     * 客户名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 车队名称
     */
    @TableField("fleet_name")
    private String fleetName;

    /**
     * 仓库ID
     */
    @TableField("warehouse_id")
    private Integer warehouseId;

    /**
     * 仓库名称
     */
    @TableField("warehouse_name")
    private String warehouseName;

    /**
     * 库存记录类型:1-入库,2-出库
     */
    @TableField("type")
    private Integer type;

    /**
     * 该记录出入库类型类型: 入库类型:1:采集,2赔付,3:拆卸入库,4-调拨入库 出库类型:1:安装出库,2-调拨出库,3-变卖出库
     */
    @TableField("stock_type")
    private Integer stockType;

    /**
     * 供给方
     */
    @TableField("target")
    private String target;

    /**
     * 轮胎品牌
     */
    @TableField("tire_brand")
    private String tireBrand;

    /**
     * 轮胎型号
     */
    @TableField("tire_spec")
    private String tireSpec;

    /**
     * 新旧程度(30,50,70,100)
     */
    @TableField("degree")
    private String degree;

    /**
     * 当前里程
     */
    @TableField("mileage")
    private Integer mileage;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人
     */
    @TableField("create_person")
    private String createPerson;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}

