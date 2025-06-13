/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service.impl.down;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

/**
 * TODO:目录下载服务
 *
 * @author SunJQ
 * @date 2025-03-25
 */
@Service
@HandlerInt(ServiceAdrEnum.D1301)
public class DownloadServiceImpl extends HandlerServiceImpl {
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam) {
        ServiceAdrEnum anEnum = ServiceAdrEnum.getEnum(inputData.getData().toString());
        return sendDatas(interfaceConfig.getAddress()+anEnum.getAddress(),initTransBody(anEnum.getNum(),inputData, baseParam),baseParam);
    }
}
