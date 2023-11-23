package org.platform.vehicle.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;

/**
 * @author gejiawei
 */
public class BaseResponse<T> extends BaseBean implements Serializable {

	private static final long serialVersionUID = -784221938078468816L;

	/**
	 * 返回码
	 */
	protected String code;

	/**
	 * 返回信息
	 */
	protected String message;

	/**
	 * 返回结果
	 */
	protected T data;

	/**
	 * 额外返回结果
	 */
	protected Object extraData;

	public BaseResponse() {
	}

	public BaseResponse(ResponseEnum taskErrorEnum, T data) {
		super();
		this.data = data;
		this.code = taskErrorEnum.getCode();
		this.message = taskErrorEnum.getMessage();
	}

	public BaseResponse(ResponseEnum taskErrorEnum) {
		super();
		this.code = taskErrorEnum.getCode();
		this.message = taskErrorEnum.getMessage();
	}

	public BaseResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public BaseResponse(String code, String message, T data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getCode() {
		return this.code;
	}

	public BaseResponse<T> setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public BaseResponse<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return this.data;
	}

	public BaseResponse<T> setData(T data) {
		this.data = data;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public static BaseResponse ok() {
		return new BaseResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
	}

	public static BaseResponse<ResponseEnum> success() {
		return new BaseResponse<>(ResponseEnum.SUCCESS);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final BaseResponse ok(Object data) {
		return new BaseResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage()).setData(data);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static BasePageResponse ok(Object data, Page pageVo) {
		return new BasePageResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage()).setData(data)
				.setPageVo(pageVo);
	}

	@SuppressWarnings("rawtypes")
	public static BaseResponse failure(String message) {
		return new BaseResponse(ResponseCode.ERROR, message);
	}

	@SuppressWarnings("rawtypes")
	public static BaseResponse failure(String code, String message) {
		return new BaseResponse(code, message);
	}

	@SuppressWarnings("rawtypes")
	public static BaseResponse failure() {
		return new BaseResponse(ResponseEnum.NOT_KNOWN_EXCEPTION.getCode(),
				ResponseEnum.NOT_KNOWN_EXCEPTION.getMessage());
	}

	@SuppressWarnings("rawtypes")
	public static BaseResponse failure(ResponseEnum error) {
		return new BaseResponse(error.getCode(), error.getMessage());
	}

	public Object getExtraData() {
		return this.extraData;
	}

	public BaseResponse<T> setExtraData(Object extraData) {
		this.extraData = extraData;
		return this;
	}

	public boolean isOk() {
		return ResponseEnum.SUCCESS.getCode().equals(this.getCode());
	}

}
