package com.openhis.service.impl.filing;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 人员慢特病备案撤销
 */
@Service
@HandlerInt(ServiceAdrEnum.F2504)
public class ReSlowdiseaseServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.F2504.getAddress(),initTransBody(ServiceAdrEnum.F2504.getNum(),inputData, baseParam),baseParam);
    }

}
