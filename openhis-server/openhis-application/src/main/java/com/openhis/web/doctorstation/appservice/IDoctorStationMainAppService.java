package com.openhis.web.doctorstation.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.PatientInfoDto;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 医生站-主页面 应用Service
 */
public interface IDoctorStationMainAppService {

    /**
     * 查询就诊患者信息
     *
     * @param patientInfoDto 查询条件 (前端传 statusEnum 区分就诊状态tab)
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param pricingFlag 划价标记
     * @return 就诊患者信息
     */
    IPage<PatientInfoDto> getPatientInfo(PatientInfoDto patientInfoDto, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request, Integer pricingFlag);

    /**
     * 医生接诊
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    R<?> receiveEncounter(Long encounterId);

    /**
     * 患者暂离
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    R<?> leaveEncounter(Long encounterId);

    /**
     * 就诊完成
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    R<?> completeEncounter(Long encounterId);

}
