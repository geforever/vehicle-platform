package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.aspect.FieldName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (WarehouseSetting)实体类
 *
 * @author geforever
 * @since 2023-09-13 10:20:17
 */
@Data
@TableName("asset_warehouse_setting")
public class WarehouseSetting implements Serializable {

    private static final long serialVersionUID = -69033270360423450L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 仓库名称
     */
    @TableField("name")
    @FieldName(value = "仓库名称")
    private String name;

    /**
     * 车队ID
     */
    @TableField("fleet_id")
    @FieldName(value = "车队ID")
    private Integer fleetId;

    /**
     * 客户ID
     */
    @TableField("client_id")
    @FieldName(value = "客户ID")
    private Integer clientId;

    /**
     * 库存最小值
     */
    @TableField("stock_min")
    @FieldName(value = "库存下限")
    private Integer stockMin;

    /**
     * 库存最大值
     */
    @TableField("stock_max")
    @FieldName(value = "库存上限")
    private Integer stockMax;

    /**
     * 是否删除:0-未删除,1-已删除
     */
    @TableField("is_delete")
    private Integer isDelete;

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

    /**
     * 更新人
     */
    @TableField("update_person")
    private String updatePerson;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

