/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service.impl.inventory;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

/**
 * 商品销售退货
 *
 * @author SunJQ
 * @date 2025-04-28
 */
@Service
@HandlerInt(ServiceAdrEnum.I3506)
public class UnPurchasingServiceImpl extends HandlerServiceImpl {
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam) {
        return sendDatas(interfaceConfig.getAddress()+ ServiceAdrEnum.I3506.getAddress(),initTransBody(ServiceAdrEnum.I3506.getNum(),inputData, baseParam),baseParam);
    }
}
