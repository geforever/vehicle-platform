package org.platform.vehicle.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_vehicle_spec_maintenace")
public class VehicleSpecMaintenaceEntity implements Serializable {

	private static final long serialVersionUID = -5322255173841741866L;

	/**
	 * 流水ID
	 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

	/**
	 * 车型id
	 */
	@TableField("spec_id")
	private Integer specId;

	/**
	 * 保养项目
	 */
	@TableField("maintenace_item")
	private Integer maintenaceItem;

	/**
	 * 保养年限周期
	 */
	@TableField("year_life")
	private Integer yearLife;

	/**
	 * 保养里程周期
	 */
	@TableField("mileage_life")
	private BigDecimal mileageLife;

}
