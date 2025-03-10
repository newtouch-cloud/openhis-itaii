package com.openhis.web.doctorstation.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingDto;

/**
 * 医生站-诊断 应用Service
 */
public interface IDoctorStationDiagnosisAppService {

    /**
     * 新增诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    R<?> addDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto);

    /**
     * 编辑诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    R<?> updateDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto);

}
