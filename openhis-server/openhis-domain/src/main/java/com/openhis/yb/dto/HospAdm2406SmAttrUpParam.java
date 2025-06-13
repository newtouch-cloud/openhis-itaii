package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2401】就医特殊属性上传
 *
 * @author yuanzs
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2406SmAttrUpParam {

    // 1. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 人员证件类型
    @JSONField(name = "psn_cert_type")
    private String psnCertType;

    // 4. 证件号码
    @JSONField(name = "certno")
    private String certno;

    // 5. 人员姓名
    @JSONField(name = "psn_name")
    private String psnName;

    // 6. 住院/门诊号
    @JSONField(name = "ipt_otp_no")
    private String iptOtpNo;

    // 7. 就医人员特殊标识类型
    @JSONField(name = "ipt_psn_sp_flag_type")
    private String iptPsnSpFlagType;

    // 8. 就医人员特殊标识
    @JSONField(name = "ipt_psn_sp_flag")
    private String iptPsnSpFlag;

    // 9. 备注
    @JSONField(name = "memo")
    private String memo;

    // 10. 医保区划
    @JSONField(name = "admdvs")
    private String admdvs;

    // 11. 就医人员特殊标识明细id（输出）
    @JSONField(name = "ipt_psn_sp_flag_detl_id")
    private String iptPsnSpFlagDetlId;
}
