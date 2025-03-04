package com.openhis.web.patientmanage.dto;

import lombok.Data;

import java.util.Date;

/**
 * 门诊记录Dto
 *
 * @author liuhr
 * @date 2025/2/28
 */
@Data
public class OutpatientRecordDto {

    /** 患者姓名 */
    private String name;

    /** 身份证号 */
    private String idCard;

    /** 疾病与诊断描述 */
    private String description;

    /** 患者院内编码/病历号 */
    private String patientBusNo;

    /** 就诊号 */
    private String encounterBusNo;

    /** 性别编码 */
    private Integer genderEnum;

    /** 就诊时间 */
    private Date encounterTime;

    /** 就诊对象状态 */
    private Integer subjectStatusEnum;

    /** 机构名称/接诊医院 */
    private String organizationName;

    /** 接诊医生姓名 */
    private String doctorName;

    /** 手机号码 */
    private String phone;

}
