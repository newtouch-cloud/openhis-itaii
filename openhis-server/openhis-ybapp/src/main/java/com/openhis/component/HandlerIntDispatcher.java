package com.openhis.component;

import com.openhis.vo.BaseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openhis.context.HandlerServiceContext;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.HandlerService;

/**
 * handler分发
 */
@Component
public class HandlerIntDispatcher {

    /** 接口服务容器 */
    @Autowired
    private HandlerServiceContext handlerServiceContext;

    /**
     * dispatcher 处理器
     * @param intType
     * @param inputData
     * @return
     */
    public ResultBody dispatcher(String intType, InputData inputData, BaseParam baseParam) {
        // 根据接口类型获取对应服务
        HandlerService handlerService = handlerServiceContext.getHandlerService(intType);
        // 调用对应服务接口
        return handlerService.handle(inputData,baseParam);
    }
}
