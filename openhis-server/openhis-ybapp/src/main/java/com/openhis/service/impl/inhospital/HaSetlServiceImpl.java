package com.openhis.service.impl.inhospital;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 住院结算
 */
@Service
@HandlerInt(ServiceAdrEnum.H2304)
public class HaSetlServiceImpl extends HandlerServiceImpl {

    /**
     *
     * @param  		inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.H2304.getAddress(),initTransBody(ServiceAdrEnum.H2304.getNum(),inputData, baseParam),baseParam);
    }

}
