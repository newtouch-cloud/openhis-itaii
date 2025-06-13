package com.openhis.web.reportmanage.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 西药(中成药)处方单 实体类
 *
 * @author liuhr
 * @date 2025/5/7
 */
@Data
@Accessors(chain = true)
public class PrescriptionPrintDto {

    /** 机构名 */
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    private Long orgId;
    private String orgId_dictText;

    /** 门诊/住院病历号 */
    private String encounterBusNo;

    /** 医保/就诊卡号 */
    private String no;

    /** 院内处方编号 */
    private String prescriptionNo;

    /** 姓名 */
    private String patientName;

    /** 性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    /** 年龄 */
    private String age;

    /** 电话 */
    private String phone;

    /** 皮试结果 */
    private Integer clinicalStatusEnum;
    private String psResult;

    /** 费别 【01医保电子凭证 | 02 居民身份证 | 03 社会保障卡 | 04 个人现金账户】*/
    private String typeCode;
    private String typeCode_enumText;

    /** 慢病(1-慢病 0-非慢病) */
    private Integer ncdsFlag;
    private String ncdsFlag_enumText;

    /** 药品性质 */
    private String pharmacologyCategoryCode;
    /** 药品性质文本 */
    private String pharmacologyCategoryCode_Text;

    /** 开具日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reqAuthoredTime;

    /** 科室 */
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private String departId;
    private String departId_dictText;

    /** 临床诊断 */
    private String diagName;

    /** 药品信息列表 start */
    /** 药品名字 */
    private String medName;

    /** 分组id */
    private Long groupId;

    /** 药品规格 */
    private String totalVolume;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 单次剂量单位 */
    @Dict(dictCode = "unit_code")
    private String doseUnitCode;
    private String doseUnitCode_dictText;

    /** 用药频次 */
    @Dict(dictCode = "rate_code")
    private String rateCode;
    private String rateCode_dictText;

    /** 用法 */
    @Dict(dictCode = "method_code")
    private String methodCode;
    private String methodCode_dictText;
    /** 药品信息列表 end */

    /** 开方医师 */
    @Dict(dictTable = "adm_practitioner_role", dictCode = "practitioner_id", dictText = "name")
    private Long dorId;
    private String dorId_dictText;

    /** 审核药师 */
    @Dict(dictTable = "adm_practitioner_role", dictCode = "practitioner_id", dictText = "name")
    private Long pharId;
    private String pharId_dictText;

    /** 调配药师 */
    @Dict(dictTable = "adm_practitioner_role", dictCode = "practitioner_id", dictText = "name")
    private Long disRevPharId;
    private String disRevPharId_dictText;

    /** 核对、发药药师 */
    @Dict(dictTable = "adm_practitioner_role", dictCode = "practitioner_id", dictText = "name")
    private Long checkPharId;
    private String checkPharId_dictText;

    /** 药品总金额 */
    private BigDecimal totalPrice;


}
