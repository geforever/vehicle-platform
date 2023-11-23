package org.platform.vehicle.param;

import java.util.ArrayList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.response.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Sunnykid
 */
@Getter
@Setter
public class VehicleSpecQueryCondition extends PageParam {

	private static final long serialVersionUID = -8598720674335178701L;

	/**
	 * 品牌名称
	 */
	private Integer brandId;

	/**
	 * 规格名称
	 */
	private String specName;
	/**
	 * 车队ID
	 */
	private Integer customerId;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted = Boolean.FALSE;

	public QueryWrapper<VehicleSpecEntity> wrapper() {
		QueryWrapper<VehicleSpecEntity> q = new QueryWrapper<VehicleSpecEntity>();
		if(this.brandId != null) {
			q.eq("brand_id", this.brandId);
		}
		if(StringUtils.isNotBlank(this.specName)) {
			q.like("spec_name", this.specName);
		}
		if(this.customerId != null) {
			q.eq("customer_id", this.customerId);
		}
		if(this.isDeleted != null) {
			q.eq("is_deleted", this.isDeleted);
		}
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
