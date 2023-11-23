package org.platform.vehicle.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_vehicle_image")
public class VehicleImageEntity implements Serializable {

	private static final long serialVersionUID = 6379697367106102477L;

	/**
	 * 流水ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 车辆ID
	 */
	@TableField("vehicle_id")
	private Integer vehicleId;

	/**
	 * 车辆图片文件
	 */
	@TableField(value="image_file", updateStrategy = FieldStrategy.IGNORED)
	private String imageFile;

	public VehicleImageEntity() {
		super();
	}

	public VehicleImageEntity(Integer id, Integer vehicleId, String imageFile) {
		super();
		this.id = id;
		this.vehicleId = vehicleId;
		this.imageFile = imageFile;
	}

	public VehicleImageEntity(Integer vehicleId, String imageFile) {
		super();
		this.vehicleId = vehicleId;
		this.imageFile = imageFile;
	}

}
