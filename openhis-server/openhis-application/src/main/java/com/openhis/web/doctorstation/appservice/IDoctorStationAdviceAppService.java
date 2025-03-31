package com.openhis.web.doctorstation.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceSaveDto;
import com.openhis.web.doctorstation.dto.AdviceSaveParam;

/**
 * 医生站-医嘱/处方 应用Service
 */
public interface IDoctorStationAdviceAppService {

    /**
     * 查询医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 医嘱信息
     */
    IPage<AdviceBaseDto> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        Integer pageNo, Integer pageSize);

    /**
     * 门诊保存医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @return 结果
     */
    R<?> saveAdvice(AdviceSaveParam adviceSaveParam);

}
