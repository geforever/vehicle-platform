package org.platform.vehicle.enums;


/**
 * @module:后台数据管理
 * @version:V1.0
 *
 * @author Sunnykid
 * @date 2023年9月7日 下午5:58:51
 * @description:准驾车型枚举
 */
public enum ApprovedDrivingTypeEnum {

	A1(1, "A1"),
	A2(2, "A2"),
	A3(3, "A3"),
	B1(4, "B1"),
	B2(5, "B2"),
	B3(6, "B3"),
	C1(7, "C1"),
	C2(8, "C2"),
	C3(9, "C3");

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
	ApprovedDrivingTypeEnum(Integer code, String text) {
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
