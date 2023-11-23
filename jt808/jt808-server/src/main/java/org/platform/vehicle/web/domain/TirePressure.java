package org.platform.vehicle.web.domain;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/24 13:19
 */
@Data
public class TirePressure {

	/**
	 * 流水ID
	 */
	private Long id;

	/**
	 * 唯一编号
	 */
	private Long code;

	/**
	 * 记录创建时间
	 */
	private Date createTime;

	/**
	 * 终端ID号
	 */
	private String clientId;

	/**
	 * 流水号
	 */
	private int serialNo;

	/**
	 * 轮胎详细数据
	 */
	private List<TirePressureData> dataList;

	/**
	 * 接收器ID
	 */
	private String receiverId;

	/**
	 * 上挂信息
	 */
	private String upTrailerInfo;

	/**
	 * 挂车状态:1-上挂,2-下挂
 	 */
	private Integer trailerStatus;

	/**
	 * 挂车中继器ID
	 */
	private String guaRepeaterId;

	/**
	 * 前桥中继器信息
	 */
	private String frontAxleRelayInfo;

	/**
	 * 经度
	 */
	private String longitude;

	/**
	 * 纬度
	 */
	private String latitude;

	/**
	 * 设备时间
	 */
	private Date deviceTime;
}
