package org.platform.vehicle.enums;


/**
 * @module:后台数据管理
 * @version:V1.0
 *
 * @author Sunnykid
 * @date 2023年9月7日 下午5:48:37
 * @description:性别枚举
 */
public enum SexEnum {

	MALE(1, "男"),
	FEMALE(2, "女");

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
	SexEnum(Integer code, String text) {
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
