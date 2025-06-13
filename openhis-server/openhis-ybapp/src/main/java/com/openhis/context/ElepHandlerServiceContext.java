package com.openhis.context;

import java.util.HashMap;
import java.util.Map;

import com.openhis.service.IElepHandlerService;
import org.springframework.stereotype.Component;

import com.openhis.service.HandlerService;

/**
 * 工厂模式
 */
@Component
public class ElepHandlerServiceContext {
	
	/** 接口处理器容器 */
    private final Map<String, IElepHandlerService> elepHandlerMap = new HashMap<>();

    /**
     * 根据类型从容器中获取处理器
     * @param type
     * @return
     */
    public IElepHandlerService getElepHandlerService(String type) {
        return elepHandlerMap.get(type);
    }

    /**
     * 装载处理器
     * @param type
     * @param service
     */
    public void putElepHandlerService(String type, IElepHandlerService service) {
        elepHandlerMap.put(type, service);
    }

}
