package org.platform.vehicle.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.platform.vehicle.response.ResponseEnum;

/**
 * <p>
 * 异常基类
 * </p>
 * @author gejiawei
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
	private String code;
	private String message;
	private Object data;

	public BaseException(ResponseEnum status) {
		super(status.getMessage());
		this.code = status.getCode();
		this.message = status.getMessage();
	}

	public BaseException(ResponseEnum status, Object data) {
		this(status);
		this.data = data;
	}

	public BaseException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BaseException(String code, String message, Object data) {
		this(code, message);
		this.data = data;
	}
}
