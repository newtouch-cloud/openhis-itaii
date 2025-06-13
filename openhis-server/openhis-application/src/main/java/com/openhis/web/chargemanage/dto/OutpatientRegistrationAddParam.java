package com.openhis.web.chargemanage.dto;

import javax.validation.Valid;

import com.openhis.web.paymentmanage.dto.PaymentDetailDto;
import com.openhis.yb.dto.Info5301SpecialConditionResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
     * 费用项管理-表单数据  //todo:挂号费会绑定诊查费，该字段会变成List，挂号前先把这个收费项开个前台，后台没有保存
     */
    @Valid
    private ChargeItemFormData chargeItemFormData;

    /**
     * 密钥 医保挂号时使用
     */
    private String busiCardInfo;//2025/05/16 前后端一致，前端大写，后端保持大写

    /**
     * 类型 医保挂号时使用
     */
    private String ybMdtrtCertType;
    /**
     * 医保个人结算方式 参照枚举 按项目结算 01 按定额结算 02
     */
    private String YbPsnSetlWay;

    public OutpatientRegistrationAddParam(){
        this.YbPsnSetlWay = "01";
    }

    /**
     * 特慢病列表
     */
    private List<Info5301SpecialConditionResult> feedetail;

}
