package com.openhis.listener;

import java.util.Map;

import com.openhis.annotation.ElepHandlerInt;
import com.openhis.context.ElepHandlerServiceContext;
import com.openhis.service.IElepHandlerService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.openhis.annotation.HandlerInt;
import com.openhis.context.HandlerServiceContext;
import com.openhis.service.HandlerService;

/**
 * 监听@ElepHandlerInt注解下的类
 */
@Component
public class ElepHandlerServiceListener implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * 将实例放到容器
	 * @param event
	 */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(ElepHandlerInt.class);
		ElepHandlerServiceContext handlerServiceContext = event.getApplicationContext().getBean(ElepHandlerServiceContext.class);
		beans.forEach((name, bean) -> {
			ElepHandlerInt typeHandler = bean.getClass().getAnnotation(ElepHandlerInt.class);
			handlerServiceContext.putElepHandlerService(typeHandler.value().toString(), (IElepHandlerService) bean);
		});
    }
}
