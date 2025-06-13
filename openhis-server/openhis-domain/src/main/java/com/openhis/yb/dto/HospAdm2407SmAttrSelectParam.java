package com.openhis.yb.dto;

import java.math.BigDecimal;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2407】就医特殊属性查询（输入）
 *
 * @author yuanzs
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2407SmAttrSelectParam {

    // 1. 分页条数
    @JSONField(name = "page_num")
    private BigDecimal pageNum;

    // 2. 分页大小
    @JSONField(name = "page_size")
    private BigDecimal pageSize;

    // 3. 就医人员特殊标识明细id
    @JSONField(name = "ipt_psn_sp_flag_detl_id")
    private String iptPsnSpFlagDetlId;

    // 4. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 5. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 6. 人员证件类型
    @JSONField(name = "psn_cert_type")
    private String psnCertType;

    // 7. 证件号码
    @JSONField(name = "certno")
    private String certno;

    // 8. 人员姓名
    @JSONField(name = "psn_name")
    private String psnName;

    // 9. 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 10. 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 11. 住院/门诊号
    @JSONField(name = "ipt_otp_no")
    private String iptOtpNo;

    // 12. 就医人员特殊标识类型
    @JSONField(name = "ipt_psn_sp_flag_type")
    private String iptPsnSpFlagType;

    // 13. 就医人员特殊标识
    @JSONField(name = "ipt_psn_sp_flag")
    private String iptPsnSpFlag;

    // 14. 医保区划
    @JSONField(name = "admdvs")
    private String admdvs;

}
