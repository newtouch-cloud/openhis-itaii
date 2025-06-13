package com.openhis.service.impl.info;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 签退接口
 */
@HandlerInt(ServiceAdrEnum.P9002)
@Service
public class SignOutServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.P9002.getAddress(),initTransBody(ServiceAdrEnum.P9002.getNum(),inputData, baseParam),baseParam);
    }

}
