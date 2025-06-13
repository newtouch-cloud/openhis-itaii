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
 * 电子处方医保电子签名
 */
@ElepHandlerInt(ElepServiceAdrEnum.A0002)
@Service
public class SignatureServiceImpl extends ElepHandlerServiceImpl {

    /**
     * 处理交易
     * @param requestData 请求信息
     * @return
     */
    @Override
    public BaseResponse elepHandle(RequestData requestData, BaseParam baseParam){
        String eleAddress = baseParam.getBaseInfo().getEleAddress();
        return sendDatas(eleAddress+ElepServiceAdrEnum.A0002.getAddress(),initTransBody(ElepServiceAdrEnum.A0002.getNum(),requestData, baseParam), baseParam);
    }

}
