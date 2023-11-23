package org.platform.vehicle.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class GeoLocation implements Serializable {

	private static final long serialVersionUID = -4148258521394719387L;

	/**
	 * 流水ID
	 */
	private Long id;

	/**
	 * 记录创建时间
	 */
	private Date createTime;

	/**
	 * 接收器ID
	 */
	private String receiverId;

	/**
	 * 消息ID
	 */
	private String msgId;

	/**
	 * 经度
	 */
	private String lng;

	/**
	 * 纬度
	 */
	private String lat;

	/**
	 * 海拔高度
	 */
	private String altitude;

	/**
	 * 速度
	 */
	private String speed;

	/**
	 * 报警标志
	 */
	private Integer warnBit;

	/**
	 * 状态
	 */
	private Integer statusBit;

	/**
	 * 设备时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deviceTime;

}
