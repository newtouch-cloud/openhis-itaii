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
 * 商品采购退货
 *
 * @author SunJQ
 * @date 2025-04-28
 */
@Service
@HandlerInt(ServiceAdrEnum.I3504)
public class StockUnReplenishmentServiceImpl extends HandlerServiceImpl {
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam) {
        return sendDatas(interfaceConfig.getAddress()+ ServiceAdrEnum.I3504.getAddress(),initTransBody(ServiceAdrEnum.I3504.getNum(),inputData, baseParam),baseParam);
    }
}
