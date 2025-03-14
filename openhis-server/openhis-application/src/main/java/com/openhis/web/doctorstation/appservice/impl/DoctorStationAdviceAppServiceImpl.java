package com.openhis.web.doctorstation.appservice.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.mapper.DoctorStationAdviceAppMapper;

/**
 * 医生站-医嘱/处方 应用实现类
 */
@Service
public class DoctorStationAdviceAppServiceImpl implements IDoctorStationAdviceAppService {

    @Resource
    DoctorStationAdviceAppMapper doctorStationAdviceAppMapper;

    /**
     *
     * @param adviceBaseDto
     * @param searchKey
     * @param locationId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public IPage<AdviceBaseDto> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        Integer pageNo, Integer pageSize) {

        return null;
    }

}
