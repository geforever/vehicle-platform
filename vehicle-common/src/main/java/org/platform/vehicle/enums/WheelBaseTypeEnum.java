package org.platform.vehicle.enums;


/**
 * @module:后台数据管理
 * @version:V1.0
 *
 * @author Sunnykid
 * @date 2023年9月7日 下午5:48:37
 * @description:车轴类型枚举
 */
public enum WheelBaseTypeEnum {

	DAO_XIANG_ZHOU(1, "导向轴"),
	QU_DONG_ZHOU(2, "驱动轴"),
	GUA_CHE_ZHOU(3, "挂车轴"),
	FU_QIAO_ZHOU(3, "浮桥轴");

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
	 */
	WheelBaseTypeEnum(Integer code, String text) {
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
