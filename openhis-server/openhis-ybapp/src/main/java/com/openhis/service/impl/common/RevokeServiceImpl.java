package com.openhis.service.impl.common;

import com.core.common.utils.SecurityUtils;
import com.openhis.common.constant.CommonConstants;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.ElepHandlerInt;
import com.openhis.domain.BaseResponse;
import com.openhis.enums.ElepServiceAdrEnum;
import com.openhis.pojo.RequestData;
import com.openhis.service.impl.ElepHandlerServiceImpl;

/**
 * 电子处方撤销
 */
@ElepHandlerInt(ElepServiceAdrEnum.A0004)
@Service
public class RevokeServiceImpl extends ElepHandlerServiceImpl {

    /**
     * 处理交易
     * @param requestData 请求信息
     * @return
     */
    @Override
    public BaseResponse elepHandle(RequestData requestData, BaseParam baseParam){
        String eleAddress = baseParam.getBaseInfo().getEleAddress();
        return sendDatas(eleAddress+ElepServiceAdrEnum.A0004.getAddress(),initTransBody(ElepServiceAdrEnum.A0004.getNum(),requestData, baseParam), baseParam);
    }

}
