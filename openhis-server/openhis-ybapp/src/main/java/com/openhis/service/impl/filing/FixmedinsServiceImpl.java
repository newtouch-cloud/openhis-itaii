package com.openhis.service.impl.filing;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 人员定点备案撤销
 */
@Service
@HandlerInt(ServiceAdrEnum.F2505)
public class FixmedinsServiceImpl extends HandlerServiceImpl {

    /**
     *
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.F2505.getAddress(),initTransBody(ServiceAdrEnum.F2505.getNum(),inputData, baseParam),baseParam);
    }

}
