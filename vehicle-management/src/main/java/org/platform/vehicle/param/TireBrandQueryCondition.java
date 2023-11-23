package org.platform.vehicle.param;

import java.util.ArrayList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.platform.vehicle.entity.TireBrandEntity;
import org.platform.vehicle.response.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Sunnykid
 */
@Getter
@Setter
public class TireBrandQueryCondition extends PageParam {

	private static final long serialVersionUID = -3357507879295510133L;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 车队ID
	 */
	private Integer customerId;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted = Boolean.FALSE;

	public QueryWrapper<TireBrandEntity> wrapper() {
		QueryWrapper<TireBrandEntity> q = new QueryWrapper<TireBrandEntity>();
		if(StringUtils.isNotBlank(this.brandName)) {
			q.like("brand_name", this.brandName);
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
