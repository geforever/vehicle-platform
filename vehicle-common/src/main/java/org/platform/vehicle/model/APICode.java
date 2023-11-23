package org.platform.vehicle.model;

/**
 * 响应状态枚举类接口
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public interface APICode {

    /** 状态码 */
    int getCode();

    /** 状态信息 */
    String getMessage();
}
