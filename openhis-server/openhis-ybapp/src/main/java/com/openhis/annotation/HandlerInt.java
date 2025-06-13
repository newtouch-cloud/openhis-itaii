package com.openhis.annotation;

import com.openhis.enums.ServiceAdrEnum;

import java.lang.annotation.*;

/**
 * 接口处理器注解
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerInt {

	ServiceAdrEnum value();

}
