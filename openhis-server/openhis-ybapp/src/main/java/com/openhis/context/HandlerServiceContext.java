package com.openhis.context;

import com.openhis.service.HandlerService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 工厂模式
 */
@Component
public class HandlerServiceContext {
	
	/** 接口处理器容器 */
    private final Map<String, HandlerService> handlerMap = new HashMap<>();

    /**
     * 根据类型从容器中获取处理器
     * @param type
     * @return
     */
    public HandlerService getHandlerService(String type) {
        return handlerMap.get(type);
    }

    /**
     * 装载处理器
     * @param type
     * @param service
     */
    public void putHandlerService(String type, HandlerService service) {
        handlerMap.put(type, service);
    }

}
