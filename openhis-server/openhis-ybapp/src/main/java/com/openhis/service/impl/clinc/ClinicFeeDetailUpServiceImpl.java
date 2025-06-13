package com.openhis.service.impl.clinc;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;


/**
 * 费用明细上传
 */
@Service
@HandlerInt(ServiceAdrEnum.C2204)
public class ClinicFeeDetailUpServiceImpl extends HandlerServiceImpl {

    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.C2204.getAddress(),initTransBody(ServiceAdrEnum.C2204.getNum(),inputData, baseParam),baseParam);
    }

}
