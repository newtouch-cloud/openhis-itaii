package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 【2507】人员意外伤害备案（输入）
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersUnintInj2507Param {
    // 1. 申报来源
    @JSONField(name = "dcla_souc")
    private String dclaSouc;

    // 2. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 3. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 4. 人员参保关系ID
    @JSONField(name = "psn_insu_rlts_id")
    private String psnInsuRltsId;

    // 5. 人员证件类型
    @JSONField(name = "psn_cert_type")
    private String psnCertType;

    // 6. 证件号码
    @JSONField(name = "certno")
    private String certno;

    // 7. 人员姓名
    @JSONField(name = "psn_name")
    private String psnName;

    // 8. 性别
    @JSONField(name = "gend")
    private String gend;

    // 9. 民族
    @JSONField(name = "naty")
    private String naty;

    // 10. 出生日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "brdy")
    private Date brdy;

    // 11. 联系电话
    @JSONField(name = "tel")
    private String tel;

    // 12. 联系地址
    @JSONField(name = "addr")
    private String addr;

    // 13. 参保所属医保区划
    @JSONField(name = "insu_admdvs")
    private String insuAdmdvs;

    // 14. 单位编号
    @JSONField(name = "emp_no")
    private String empNo;

    // 15. 单位名称
    @JSONField(name = "emp_name")
    private String empName;

    // 16. 就医地医保区划
    @JSONField(name = "mdtrtarea_admdvs")
    private String mdtrtareaAdmdvs;

    // 17. 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 18. 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 19. 医院等级
    @JSONField(name = "hosp_lv")
    private String hospLv;

    // 20. 入院时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "adm_time")
    private Date admTime;

    // 21. 受伤时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "trum_time")
    private Date trumTime;

    // 22. 受伤地点
    @JSONField(name = "trum_site")
    private String trumSite;

    // 23. 致伤原因
    @JSONField(name = "trum_rea")
    private String trumRea;

    // 24. 审核支付标志
    @JSONField(name = "chk_pay_flag")
    private String chkPayFlag;

    // 25. 代办人姓名
    @JSONField(name = "agnter_name")
    private String agnterName;

    // 26. 代办人证件类型
    @JSONField(name = "agnter_cert_type")
    private String agnterCertType;

    // 27. 代办人证件号码
    @JSONField(name = "agnter_certno")
    private String agnterCertno;

    // 28. 代办人联系方式
    @JSONField(name = "agnter_tel")
    private String agnterTel;

    // 29. 代办人联系地址
    @JSONField(name = "agnter_addr")
    private String agnterAddr;

    // 30. 代办人关系
    @JSONField(name = "agnter_rlts")
    private String agnterRlts;

    // 31. 开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "begndate")
    private Date begndate;

    // 32. 结束日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "enddate")
    private Date enddate;

    // 33. 备注
    @JSONField(name = "memo")
    private String memo;

    // 34. 待遇申报明细流水号（输出）
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;
}
