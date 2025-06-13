package com.openhis.service.impl.inhospital;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 办理出院
 */
@Service
@HandlerInt(ServiceAdrEnum.H2405)
public class ReDischargedServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.H2405.getAddress(),initTransBody(ServiceAdrEnum.H2405.getNum(),inputData, baseParam),baseParam);
    }

}
