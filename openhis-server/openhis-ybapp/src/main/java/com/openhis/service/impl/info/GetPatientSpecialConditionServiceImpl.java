package com.openhis.service.impl.info;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

@Service
@HandlerInt(ServiceAdrEnum.G5301)
public class GetPatientSpecialConditionServiceImpl extends HandlerServiceImpl {

    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam) {
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.G5301.getAddress(),initTransBody(ServiceAdrEnum.G5301.getNum(),inputData, baseParam),baseParam);
    }
}
