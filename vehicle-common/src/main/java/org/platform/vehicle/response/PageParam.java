package org.platform.vehicle.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class PageParam implements Serializable {

	private static final long serialVersionUID = 5590207085765725933L;

	/**
	 * 页大小
	 */
	protected Integer pageSize;

	/**
	 * 页码
	 */
	protected Integer pageNum;

	public boolean hasPageParam() {
		return this.getPageNum() != null && this.getPageSize() != null && this.getPageNum().intValue() > 0
				&& this.getPageSize().intValue() > 0;
	}

}
