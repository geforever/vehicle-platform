package org.platform.vehicle.param;

import java.util.ArrayList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.platform.vehicle.entity.DriverEntity;
import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Sunnykid
 */
@Getter
@Setter
public class DriverQueryCondition extends PageParam {

	private static final long serialVersionUID = -7902332086911958683L;

	/**
	 * 司机姓名
	 */
	private String driverName;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 车队ID
	 */
	private Integer customerId;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted = Boolean.FALSE;

	public QueryWrapper<DriverEntity> wrapper() {
		QueryWrapper<DriverEntity> q = new QueryWrapper<DriverEntity>();
		if(StringUtils.isNotBlank(this.driverName)) {
			q.like("driver_name", this.driverName);
		}
		if(StringUtils.isNotBlank(this.mobile)) {
			q.like("mobile", this.mobile);
		}
		if(this.customerId != null) {
			q.eq("customer_id", this.customerId);
		}
		if(this.isEnabled != null) {
			q.eq("is_enabled", this.isEnabled);
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
