package com.openhis.service.impl.info;

import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.impl.HandlerServiceImpl;

/**
 * 医保目录信息查询接口
 */
@Service
@HandlerInt(ServiceAdrEnum.D1312)
public class CatalogueInfoServiceImpl extends HandlerServiceImpl {

    /**
     * 处理交易
     * 
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam) {
        return sendDatas(interfaceConfig.getAddress() + ServiceAdrEnum.D1312.getAddress(),
            initTransBody(ServiceAdrEnum.D1312.getNum(), inputData, baseParam), baseParam);
    }

}
