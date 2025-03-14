package com.openhis.web.doctorstation.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;

/**
 * 医生站-医嘱/处方 应用Service
 */
public interface IDoctorStationAdviceAppService {

    IPage<AdviceBaseDto> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        Integer pageNo, Integer pageSize);

}
