package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【25003】人员生育备案
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersBirthRecord25003Param {

    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 联系电话
    @JSONField(name = "tel")
    private String tel;

    // 3. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 4. 参保机构医保区划
    @JSONField(name = "insu_admdvs")
    private String insuAdmdvs;

    // 5. 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 6. 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 7. 孕周数
    @JSONField(name = "esso_val")
    private Integer essoVal;

    // 8. 生育待遇申报人类别
    @JSONField(name = "matn_trt_dclaer_type")
    private String matnTrtDclaerType;

    // 9. 末次月经日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "last_mena_date")
    private Date lastMenaDate;

    // 10. 预计生育日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "plan_matn_date")
    private Date planMatnDate;

    // 11. 申报日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "dcla_date")
    private Date dclaDate;

    // 12. 配偶姓名
    @JSONField(name = "spus_name")
    private String spusName;

    // 13. 配偶证件类型
    @JSONField(name = "spus_cert_type")
    private String spusCertType;

    // 14. 配偶证件号码
    @JSONField(name = "spus_certno")
    private String spusCertno;

    // 15. 生育类别
    @JSONField(name = "matn_type")
    private String matnType;

    // 16. 胎次
    @JSONField(name = "fetts")
    private BigDecimal fetts;

    // 17. 计划生育服务证号
    @JSONField(name = "fpsc_no")
    private String fpscNo;

    // 18. 开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "begndate")
    private Date begndate;

    // 19. 结束日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "enddate")
    private Date enddate;

    // 20. 联系地址
    @JSONField(name = "addr")
    private String addr;

    // --- 输出参数 ---

    // 1. 待遇申报明细流水号
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String outputPsnNo;

}
