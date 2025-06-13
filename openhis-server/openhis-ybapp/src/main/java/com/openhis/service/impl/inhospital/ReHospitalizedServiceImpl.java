package com.openhis.service.impl.inhospital;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 撤销住院
 */
@Service
@HandlerInt(ServiceAdrEnum.H2404)
public class ReHospitalizedServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.H2404.getAddress(),initTransBody(ServiceAdrEnum.H2404.getNum(),inputData, baseParam),baseParam);
    }

}
