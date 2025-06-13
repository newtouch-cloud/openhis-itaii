package com.openhis.service.impl.info;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;


/**
 * 人员信息获取接口
 */
@Service
@HandlerInt(ServiceAdrEnum.P1101)
public class GetPersonInfoServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.P1101.getAddress(),initTransBody(ServiceAdrEnum.P1101.getNum(),inputData, baseParam),baseParam);
    }

}
