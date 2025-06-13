package com.openhis.web.doctorstation.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患者过敏不耐受Dto
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Data
@Accessors(chain = true)
public class AllergyIntoInfoDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 临床状况 */
    private Integer clinicalStatusEnum;
    private String clinicalStatusEnum_enumText;

    /** 验证状态 */
    private Integer verificationStatusEnum;
    private String verificationStatusEnum_enumText;

    /** 类型 */
    private Integer typeEnum;

    /** 过敏原类别 */
    @Dict(dictCode = "allergy_category")
    private String categoryCode;
    private String categoryCode_dictText;

    /** 危险程度 */
    private Integer criticalityEnum;
    private String criticalityEnum_enumText;

    /** 过敏物质编码 */
    @Dict(dictCode = "allergy_code")
    private String code;
    private String code_dictText;

    /** 患者ID */
    @Dict(dictTable = "adm_patient", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;
    private String patientId_dictText;

    /** 反应描述 */
    private String description;

    /** 严重程度 */
    private Integer severityEnum;
    private String severityEnum_enumText;

    /** 过敏发生开始日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date onsetDateTime;

    /** 记录者 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_practitioner", dictCode = "id", dictText = "name")
    private Long practitionerId;
    private String practitionerId_dictText;

    /** 断言者 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long checkPractitionerId;

    /** 记录日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recordedDate;

    /** 最后反应发生日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastReactionOccurrence ;

    /** 备注 */
    private String note;

}
