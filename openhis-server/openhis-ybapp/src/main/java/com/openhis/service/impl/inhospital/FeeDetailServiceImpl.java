package com.openhis.service.impl.inhospital;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 住院费用明细上传
 */
@Service
@HandlerInt(ServiceAdrEnum.H2301)
public class FeeDetailServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.H2301.getAddress(),initTransBody(ServiceAdrEnum.H2301.getNum(),inputData, baseParam),baseParam);
    }

}
