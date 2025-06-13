package com.openhis.service.impl.reconciliation;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 清算申请
 */
@Service
@HandlerInt(ServiceAdrEnum.R3203A)
public class MonthSetlApplyAServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ ServiceAdrEnum.R3203A.getAddress(),initTransBody(ServiceAdrEnum.R3203A.getNum(),inputData, baseParam), baseParam);
    }

}
