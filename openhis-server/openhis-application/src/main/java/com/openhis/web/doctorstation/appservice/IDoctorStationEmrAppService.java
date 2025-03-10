package com.openhis.web.doctorstation.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.EmrTemplateDto;
import com.openhis.web.doctorstation.dto.PatientEmrDto;

/**
 * 医生站-电子病历 应用Service
 */
public interface IDoctorStationEmrAppService {
    /**
     * 添加病人病历信息
     *
     * @param patientEmrDto 电子病历信息dto
     * @return 操作结果
     */
    R<?> addPatientEmr(PatientEmrDto patientEmrDto);

    /**
     * 获取患者历史病历
     *
     * @param patientEmrDto 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 分页数据列表
     */
    R<?> getPatientEmrHistory(PatientEmrDto patientEmrDto, Integer pageNo, Integer pageSize);

    /**
     * 保存病历模板
     *
     * @param emrTemplateDto 病历模板信息
     * @return 操作结果
     */
    R<?> addEmrTemplate(EmrTemplateDto emrTemplateDto);

    /**
     * 获取电子病历模板列表
     *
     * @param emrTemplateDto 查询参数
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    R<?> getEmrTemplate(EmrTemplateDto emrTemplateDto, Integer pageNo, Integer pageSize);

    /**
     * 删除病历模板
     *
     * @param id 模板id
     * @return 操作结果
     */
    R<?> deleteEmrTemplate(Long id);

}
