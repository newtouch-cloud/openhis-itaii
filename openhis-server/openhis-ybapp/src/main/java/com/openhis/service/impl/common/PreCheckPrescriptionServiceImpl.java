package com.openhis.service.impl.common;

import com.core.common.utils.SecurityUtils;
import com.openhis.annotation.ElepHandlerInt;
import com.openhis.common.constant.CommonConstants;
import com.openhis.domain.BaseResponse;
import com.openhis.enums.ElepServiceAdrEnum;
import com.openhis.pojo.RequestData;
import com.openhis.service.impl.ElepHandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.impl.HandlerServiceImpl;

/**
 * 电子处方上传预核验接口
 */
@ElepHandlerInt(ElepServiceAdrEnum.A0001)
@Service
public class PreCheckPrescriptionServiceImpl extends ElepHandlerServiceImpl {

    /**
     * 处理交易
     * @param requestData 请求信息
     * @return
     */
    @Override
    public BaseResponse elepHandle(RequestData requestData, BaseParam baseParam){
        String eleAddress = baseParam.getBaseInfo().getEleAddress();
        return sendDatas(eleAddress+ ElepServiceAdrEnum.A0001.getAddress(),initTransBody(ElepServiceAdrEnum.A0001.getNum(),requestData, baseParam), baseParam);
    }

}
