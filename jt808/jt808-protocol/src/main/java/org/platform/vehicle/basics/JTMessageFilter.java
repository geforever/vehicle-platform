package org.platform.vehicle.basics;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public interface JTMessageFilter<T extends JTMessage> {

    boolean doFilter(T message);
}
