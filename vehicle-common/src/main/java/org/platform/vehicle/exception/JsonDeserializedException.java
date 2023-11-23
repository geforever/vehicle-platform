package org.platform.vehicle.exception;


/**
 * 自定义JSON异常类
 * @author zengxinyan
 *
 */
public class JsonDeserializedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JsonDeserializedException(String message) {
		super(message);
	}

	public JsonDeserializedException(Throwable cause) {
		super(cause);
	}

	public JsonDeserializedException(String message, Throwable cause) {
		super(message, cause);
	}

}
