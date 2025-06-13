/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service.impl.clinc;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

/**
 * 费用明细撤销
 *
 * @author SunJQ
 * @date 2025-05-07
 */
@Service
@HandlerInt(ServiceAdrEnum.C2205)
public class ClinicFeeDetailDelServiceImpl extends HandlerServiceImpl {


    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ ServiceAdrEnum.C2205.getAddress(),initTransBody(ServiceAdrEnum.C2205.getNum(),inputData, baseParam),baseParam);
    }

}
