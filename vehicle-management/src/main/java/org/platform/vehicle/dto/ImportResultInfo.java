package org.platform.vehicle.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @company:上海面心科技
 * @project:路安车辆维管系统
 * @module:后台数据管理
 * @version:V1.0
 *
 * @author Sunnykid
 * @date 2023年9月22日 下午4:55:51
 * @description: 数据导入结果
 */
@Data
public class ImportResultInfo implements Serializable {

	/**
	 * 类序列编号
	 */
	private static final long serialVersionUID = -6248522393306184845L;

	/**
	 * 数据总条数
	 */
	private int totalCount = 0x0;

	/**
	 * 导入成功的数据条数
	 */
	private int successCount = 0x0;

	/**
	 * 导入失败的数据条数
	 */
	private int errorCount = 0x0;

	/**
	 * 导入失败的明细信息
	 */
	private List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();

	public ImportResultInfo(int totalCount, int successCount, int errorCount, List<Map<String, Object>> errorList) {
		super();
		this.totalCount = totalCount;
		this.successCount = successCount;
		this.errorCount = errorCount;
		this.errorList = errorList;
	}

	public ImportResultInfo() {
		super();
	}

	public ImportResultInfo(int totalCount, int successCount, int errorCount) {
		super();
		this.totalCount = totalCount;
		this.successCount = successCount;
		this.errorCount = errorCount;
	}

}
