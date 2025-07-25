package com.openhis.web.chargemanage.dto;

import javax.validation.Valid;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊挂号 新增参数
 */
@Data
@Accessors(chain = true)
public class OutpatientRegistrationAddParam {

    /**
     * 就诊管理-表单数据
     */
    @Valid
    private EncounterFormData encounterFormData;

    // /**
    // * 就诊诊断管理-表单数据
    // */
    // private EncounterDiagnosisFormData encounterDiagnosisFormData;

    /**
     * 就诊位置管理-表单数据
     */
    @Valid
    private EncounterLocationFormData encounterLocationFormData;

    /**
     * 就诊参数者管理-表单数据
     */
    @Valid
    private EncounterParticipantFormData encounterParticipantFormData;

    /**
     * 就诊账户管理-表单数据
     */
    @Valid
    private AccountFormData accountFormData;
    /**
     * 费用项管理-表单数据
     */
    @Valid
    private ChargeItemFormData chargeItemFormData;

}
