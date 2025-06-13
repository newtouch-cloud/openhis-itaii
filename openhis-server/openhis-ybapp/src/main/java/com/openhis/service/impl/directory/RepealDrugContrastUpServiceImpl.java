package com.openhis.service.impl.directory;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 目录对照撤销
 */
@HandlerInt(ServiceAdrEnum.P3302)
@Service
public class RepealDrugContrastUpServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.P3302.getAddress(),initTransBody(ServiceAdrEnum.P3302.getNum(),inputData, baseParam),baseParam);
    }

}
