package org.platform.vehicle.enums;


/**
 * @module:后台数据管理
 * @version:V1.0
 *
 * @author Sunnykid
 * @date 2023年9月7日 下午5:58:51
 * @description:车辆保养项目枚举
 */
public enum VehicheMaintenanceItemEnum {

	JIYOU_JILV(1, "机油机滤"),
	HUO_HUA_SAI(2, "火花塞"),
	KONG_QI_LV(3, "空气滤清器"),
	ZHI_DONG_YE(4, "制动液"),
	RAN_YOU_LV(5, "燃油滤清器"),
	LUN_TAI(6, "轮胎");

	/**
	 * 枚举元素唯一编码
	 */
	Integer code;

	/**
	 * 枚举元素文本
	 */
	String text;

	/**
	 * 带参数的构造函数
	 *
	 * @param code Integer 枚举元素唯一编码
	 * @param text String 枚举元素文本
	 * @param description String 枚举元素描述文字
	 */
	VehicheMaintenanceItemEnum(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	/**
	 * 获取枚举元素唯一编码
	 *
	 * @return Integer 返回枚举元素唯一编码
	 */
	public Integer getCode() {
		return this.code;
	}

	/**
	 * 获取枚举元素描述文字
	 *
	 * @return String 返回枚举元素描述文字
	 */
	public String getText() {
		return this.text;
	}
}
