package com.openhis.service.impl.clinc;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 撤销挂号接口
 */
@Service
@HandlerInt(ServiceAdrEnum.C2202)
public class ClinicReRegServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.C2202.getAddress(),initTransBody(ServiceAdrEnum.C2202.getNum(),inputData, baseParam),baseParam);
    }

}
