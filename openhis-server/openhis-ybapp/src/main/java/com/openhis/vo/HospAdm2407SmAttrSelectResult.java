package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2407】就医特殊属性查询（输出）
 *
 * @author yuanzs
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2407SmAttrSelectResult {

    // 1. 就医人员特殊标识明细id
    @JSONField(name = "ipt_psn_sp_flag_detl_id")
    private String iptPsnSpFlagDetlId;

    // 2. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 3. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 4. 人员证件类型
    @JSONField(name = "psn_cert_type")
    private String psnCertType;

    // 5. 证件号码
    @JSONField(name = "certno")
    private String certno;

    // 6. 人员姓名
    @JSONField(name = "psn_name")
    private String psnName;

    // 7. 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 8. 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 9. 住院/门诊号
    @JSONField(name = "ipt_otp_no")
    private String iptOtpNo;

    // 10. 就医人员特殊标识类型
    @JSONField(name = "ipt_psn_sp_flag_type")
    private String iptPsnSpFlagType;

    // 11. 就医人员特殊标识
    @JSONField(name = "ipt_psn_sp_flag")
    private String iptPsnSpFlag;

    // 12. 医保区划
    @JSONField(name = "admdvs")
    private String admdvs;

}
