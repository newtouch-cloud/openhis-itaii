package com.openhis.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    String dictCode();          // 字典类型字段
    String dictText() default ""; // 回显字段，默认为空
    String dictTable() default ""; // 表名，默认为空
}
