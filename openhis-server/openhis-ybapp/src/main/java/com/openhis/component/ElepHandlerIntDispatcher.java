package com.openhis.component;

import com.openhis.context.ElepHandlerServiceContext;
import com.openhis.domain.BaseResponse;
import com.openhis.pojo.RequestData;
import com.openhis.service.IElepHandlerService;
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
public class ElepHandlerIntDispatcher {

    /** 接口服务容器 */
    @Autowired
    private ElepHandlerServiceContext  elepHandlerServiceContext;

    /**
     * dispatcher 处理器
     * @param intType
     * @param requestData
     * @return
     */
    public BaseResponse dispatcher(String intType, RequestData requestData, BaseParam baseParam) {
        // 根据接口类型获取对应服务
        IElepHandlerService elephandlerService = elepHandlerServiceContext.getElepHandlerService(intType);
        // 调用对应服务接口
        return elephandlerService.elepHandle(requestData,baseParam);
    }
}
