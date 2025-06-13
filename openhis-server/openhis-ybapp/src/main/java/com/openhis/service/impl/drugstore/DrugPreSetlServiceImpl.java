package com.openhis.service.impl.drugstore;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 药店预结算
 */
@Service
@HandlerInt(ServiceAdrEnum.D2101)
public class DrugPreSetlServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.D2101.getAddress(),initTransBody(ServiceAdrEnum.D2101.getNum(),inputData, baseParam),baseParam);
    }

}
