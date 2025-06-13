package com.openhis.service.impl.clinc;

import com.alibaba.fastjson2.JSON;
import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 挂号接口
 */
@Service
@HandlerInt(ServiceAdrEnum.C2201)
public class ClinicRegServiceImpl extends HandlerServiceImpl {

    Logger logger = LoggerFactory.getLogger(ClinicRegServiceImpl.class);

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        logger.info(ServiceAdrEnum.C2201.getDescription()+" : "+ JSON.toJSONString(inputData));
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.C2201.getAddress(),initTransBody(ServiceAdrEnum.C2201.getNum(),inputData, baseParam),baseParam);
    }

}
