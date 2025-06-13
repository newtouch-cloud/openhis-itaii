package com.openhis.service.impl.drugstore;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 药店结算
 */
@Service
@HandlerInt(ServiceAdrEnum.D2102)
public class DrugSetlServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.D2102.getAddress(),initTransBody(ServiceAdrEnum.D2102.getNum(),inputData, baseParam),baseParam);
    }

}
