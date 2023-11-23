package org.platform.vehicle.param;

import java.util.ArrayList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Sunnykid
 */
@Getter
@Setter
public class VehicleQueryCondition extends PageParam {

	private static final long serialVersionUID = -1190667251881821226L;

	/**
	 * 车牌号
	 */
	private String licensePlate;

	/**
	 * 接收器ID
	 */
	private String receiverIdNumber;

	/**
	 * 车队ID
	 */
	private Integer fleetId;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted = Boolean.FALSE;

	private Integer maintanceStatus;

	public QueryWrapper<VehicleEntity> wrapper() {
		QueryWrapper<VehicleEntity> q = new QueryWrapper<VehicleEntity>();
		if (StringUtils.isNotBlank(this.licensePlate)) {
			q.like("license_plate", this.licensePlate);
		}
		if(StringUtils.isNotBlank(this.receiverIdNumber)) {
			q.like("receiver_id_number", this.receiverIdNumber);
		}
		if(this.fleetId != null) {
			q.eq("fleet_id", this.fleetId);
		}
		if(this.isEnabled != null) {
			q.eq("is_enabled", this.isEnabled);
		}
		if(this.isDeleted != null) {
			q.eq("is_deleted", this.isDeleted);
		}
		// TODO: 处理maintanceStatus
		q.orderByDesc(new ArrayList<String>() {
			private static final long serialVersionUID = -1176952011182782372L;
			{
				this.add("update_time");
				this.add("create_time");
			}
		});
		return q;
	}

}
