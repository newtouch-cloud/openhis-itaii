package com.openhis.web.reportmanage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 处置单 实体类
 *
 * @author Wuser
 * @date 2025/5/10
 */
@Data
@Accessors(chain = true)
public class DisposalDto {

    /** 机构名 */
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    private Long orgId;
    private String orgId_dictText;

    /** 服务请求编码 */
    private String busNo;

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

    /** 门诊/住院病历号 */
    private String encounterBusNo;

    /** 医保/就诊卡号 */
    private String no;

    /** 科室 */
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private String organizationId;
    private String organizationId_dictText;

    /** 备注 */
    private String note;

    /** 临床诊断 */
    private String diagName;

    /** 治疗项目名 */
    private String activityName;

    /** 数量 */
    private Integer quantity;

    /** 单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 开具日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reqAuthoredTime;

    /** 开方医师 */
    @Dict(dictTable = "adm_practitioner_role", dictCode = "practitioner_id", dictText = "name")
    private Long dorId;
    private String dorId_dictText;

    /** 处置总金额 */
    private BigDecimal totalPrice;

}
