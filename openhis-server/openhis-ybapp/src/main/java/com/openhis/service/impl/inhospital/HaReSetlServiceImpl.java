package com.openhis.service.impl.inhospital;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 住院结算撤销
 */
@Service
@HandlerInt(ServiceAdrEnum.H2305)
public class HaReSetlServiceImpl extends HandlerServiceImpl {

    /**
     *
     * @param  		inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.H2305.getAddress(),initTransBody(ServiceAdrEnum.H2305.getNum(),inputData, baseParam),baseParam);
    }

}
