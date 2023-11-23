package org.platform.vehicle.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class BasePageResponse<T> extends BaseResponse<T> {

	private static final long serialVersionUID = -3442281809385014599L;

	private Page<T> pageVo;

	public Page<T> getPageVo() {
		return this.pageVo;
	}

	public BasePageResponse<T> setPageVo(Page<T> pageVo) {
		this.pageVo = pageVo;
		return this;
	}

	public BasePageResponse() {
	}

	public BasePageResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public BasePageResponse(String code, String message, T data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public BasePageResponse(String code, String message, T data, Page<T> Page) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
		this.pageVo = Page;
	}

	public BasePageResponse(T data, Page<T> pageVo) {
		this.setCode(ResponseEnum.SUCCESS.getCode());
		this.setMessage(ResponseEnum.SUCCESS.getMessage());
		this.pageVo = pageVo;
		this.data = data;
	}

	public BasePageResponse(ResponseEnum taskErrorEnum, T data, Page<T> Page) {
		super();
		this.code = taskErrorEnum.getCode();
		this.message = taskErrorEnum.getMessage();
		this.data = data;
		this.pageVo = Page;
	}

	@Override
	public BasePageResponse<T> setData(T data) {
		this.data = data;
		return this;
	}

	public static BasePageResponse ok() {
		return new BasePageResponse<Void>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static BasePageResponse ok(Page pageVo) {
		return new BasePageResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage())
				.setPageVo(pageVo);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static BasePageResponse ok(Object data, Page pageVo) {
		return new BasePageResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage()).setData(data)
				.setPageVo(pageVo);
	}

	@SuppressWarnings("rawtypes")
	public static BasePageResponse failure(String code, String message) {
		return new BasePageResponse(code, message);
	}

	public static BasePageResponse failure() {
		return new BasePageResponse<Void>(ResponseEnum.NOT_KNOWN_EXCEPTION.getCode(),
				ResponseEnum.NOT_KNOWN_EXCEPTION.getMessage());
	}

	public static BasePageResponse failure(ResponseEnum error) {
		return new BasePageResponse<Void>(error.getCode(), error.getMessage());
	}

	public static BasePageResponse failure(String message) {
		return new BasePageResponse<Void>(ResponseCode.ERROR, message);
	}
}
