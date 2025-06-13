/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service.impl.reconciliation;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

/**
 * 日对账
 *
 * @author SunJQ
 * @date 2025-04-28
 */
@Service
@HandlerInt(ServiceAdrEnum.R13203)
public class DayReconciliationServiceImpl extends HandlerServiceImpl {
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam) {
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.R13203.getAddress(),initTransBody(ServiceAdrEnum.R13203.getNum(),inputData,baseParam),baseParam);
    }
}
