package com.openhis.service.impl.info;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 签到接口
 */
@HandlerInt(ServiceAdrEnum.P9001)
@Service
public class SignInServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.P9001.getAddress(),initTransBody(ServiceAdrEnum.P9001.getNum(),inputData, baseParam),baseParam);
    }

}
