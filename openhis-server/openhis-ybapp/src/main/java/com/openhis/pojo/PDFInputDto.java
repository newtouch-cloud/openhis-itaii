package com.openhis.web.ybmanage.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * PDF模板替换内容实体类
 *
 * @author liuhr
 * @date 2025/4/30
 */
@Data
@Accessors(chain = true)
public class PDFInputDto {

    /** 医保电子处方追溯码 */
    private String rxTraceCode;

    /** 机构名 */
    private String orgName;

    /** 门诊/住院病历号 */
    private String iptOtpNo;

    /** 院内处方编号 */
    private String prescriptionNo;

    /** 科别/病区和床位 */
    private String locationName;

    /** 姓名 */
    private String patnName;

    /** 性别 */
    private String gender;

    /** 年龄 */
    private BigDecimal patnAge;

    /** 费别 */
    private String hiFeesetlType;

    /** 开具日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date prscTime;

    /** 临床诊断 */
    private String diagName;

    /** 处方有效天数 */
    private BigDecimal valiDays;

    /** 开方医师 */
    private String prscDrName;

    /** 审核药师 */
    private String pharName;

    /** 调配、复核药师 */
    private String disRevPharName;

    /** 核对、发药药师 */
    private String checkPharName;

    /** 延长处方用量原因 */
    private String reason;

    /** 药品信息列表 */
    private List<MedDetail> medDetailList;

    /**
     * 药品明细信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor  // 关键注解
    public static class MedDetail {

        /** 药品名字 */
        private String medName;

        /** 药品规格 */
        private String drugSpec;

        /** 单次用量 */
        private BigDecimal sinDoscnt;

        /** 单次剂量单位 */
        private String sinDosunt;

        /** 使用频次编码 */
        private String usedFrquCodg;

        /** 用药途径 */
        private String medWay;
    }

}
