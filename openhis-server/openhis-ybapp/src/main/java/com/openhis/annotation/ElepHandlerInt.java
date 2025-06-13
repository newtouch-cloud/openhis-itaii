package com.openhis.annotation;

import java.lang.annotation.*;

import com.openhis.enums.ElepServiceAdrEnum;
import com.openhis.enums.ServiceAdrEnum;

/**
 * 接口处理器注解
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElepHandlerInt {

	ElepServiceAdrEnum value();

}
