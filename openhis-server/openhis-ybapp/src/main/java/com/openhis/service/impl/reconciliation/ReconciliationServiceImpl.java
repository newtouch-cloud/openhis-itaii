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
 * 清算申请状态查询（吉林省）
 *
 * @author SunJQ
 * @date 2025-04-28
 */
@Service
@HandlerInt(ServiceAdrEnum.R3205A)
public class ReconciliationServiceImpl extends HandlerServiceImpl {
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam) {
        return sendDatas(interfaceConfig.getAddress()+ ServiceAdrEnum.R3205A.getAddress(),initTransBody(ServiceAdrEnum.R3205A.getNum(),inputData, baseParam), baseParam);
    }
}
