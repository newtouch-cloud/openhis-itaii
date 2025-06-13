package com.openhis.listener;

import com.openhis.annotation.HandlerInt;
import com.openhis.context.HandlerServiceContext;
import com.openhis.service.HandlerService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听@HandlerInt注解下的类
 */
@Component
public class HandlerServiceListener implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * 将实例放到容器
	 * @param event
	 */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(HandlerInt.class);
		HandlerServiceContext handlerServiceContext = event.getApplicationContext().getBean(HandlerServiceContext.class);
		beans.forEach((name, bean) -> {
			HandlerInt typeHandler = bean.getClass().getAnnotation(HandlerInt.class);
			handlerServiceContext.putHandlerService(typeHandler.value().toString(), (HandlerService) bean);
		});
    }
}
