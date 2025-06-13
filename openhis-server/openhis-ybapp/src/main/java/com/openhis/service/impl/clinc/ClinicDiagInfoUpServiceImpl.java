package com.openhis.service.impl.clinc;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 门诊就诊信息上传
 *
 * @author SunJQ
 * @date 2025-05-07
 */
@Service
@HandlerInt(ServiceAdrEnum.C2203)
public class ClinicDiagInfoUpServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.C2203.getAddress(),initTransBody(ServiceAdrEnum.C2203.getNum(),inputData, baseParam),baseParam);
    }

}
