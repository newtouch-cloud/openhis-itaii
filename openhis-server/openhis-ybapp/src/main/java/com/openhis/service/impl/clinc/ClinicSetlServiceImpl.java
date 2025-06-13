package com.openhis.service.impl.clinc;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 门诊结算
 */
@Service
@HandlerInt(ServiceAdrEnum.C2207)
public class ClinicSetlServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param  		inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.C2207.getAddress(),initTransBody(ServiceAdrEnum.C2207.getNum(),inputData, baseParam),baseParam);
    }

}
