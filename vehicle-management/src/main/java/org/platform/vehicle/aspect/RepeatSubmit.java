package org.platform.vehicle.aspect;

import java.lang.annotation.*;

/**
 * @Author: gejiawei
 * @Version:
 * @Program: yingtu
 * @Date: 2022/6/14 9:28
 * @Description:  功能：自定义注解[重复提交]
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    /**
     * 限制多少分钟后可再次请求同一接口
     *
     * @return
     */
    long lockTime() default 1;

    /**
     * 锁定的字段,默认传参MD5加密
     *
     * @return
     */
    String lockField() default "";

}
