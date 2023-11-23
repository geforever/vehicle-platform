package org.platform.vehicle.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author gejiawei
 * @Date 2022/1/25 2:21 下午
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FieldName {

    /**
     * 字段名称
     *
     * @return
     */
    String value() default "";

}
