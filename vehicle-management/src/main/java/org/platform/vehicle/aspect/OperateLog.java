package org.platform.vehicle.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author gejiawei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    //160

    /**
     * 一级模块
     */
    String topModule() default "";

    /**
     * 操作
     */
    String operation() default "";

    /**
     * 功能模块
     */
    String module() default "";

    /**
     * 操作内容：新增，删除
     */
    String content() default "";

    int type() default 0;

}
