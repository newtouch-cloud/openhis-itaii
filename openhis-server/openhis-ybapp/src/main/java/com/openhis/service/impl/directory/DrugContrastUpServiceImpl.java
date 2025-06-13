package com.openhis.service.impl.directory;

import com.openhis.service.impl.HandlerServiceImpl;
import com.openhis.vo.BaseParam;
import org.springframework.stereotype.Service;

import com.openhis.annotation.HandlerInt;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;

/**
 * 药品目录对照上传
 */
@HandlerInt(ServiceAdrEnum.P3301)
@Service
public class DrugContrastUpServiceImpl extends HandlerServiceImpl {

    /**
     *
     * @param inputData
     * @return
     */
    @Override
    public ResultBody handle(InputData inputData, BaseParam baseParam){
        return sendDatas(interfaceConfig.getAddress()+ServiceAdrEnum.P3301.getAddress(),initTransBody(ServiceAdrEnum.P3301.getNum(),inputData, baseParam),baseParam);
    }

}
