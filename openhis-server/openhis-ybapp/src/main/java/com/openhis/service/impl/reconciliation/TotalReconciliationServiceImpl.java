package com.openhis.service.impl.reconciliation;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 对总账
 */
@Service
@HandlerInt(ServiceAdrEnum.R3201)
public class TotalReconciliationServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.R3201.getAddress(),initTransBody(ServiceAdrEnum.R3201.getNum(),inputData, baseParam), baseParam);
    }

}
