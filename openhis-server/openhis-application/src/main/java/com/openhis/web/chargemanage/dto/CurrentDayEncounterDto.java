package com.openhis.web.chargemanage.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 当天就诊信息
 */
@Data
@Accessors(chain = true)
public class CurrentDayEncounterDto {

    /** 租户ID */
    private Integer tenantId;

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 科室ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organizationId;

    /**
     * 科室名称
     */
    private String organizationName;

    /**
     * 挂号类型
     */
    private String healthcareName;

    /**
     * 专家账号id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerUserId;

    /**
     * 专家
     */
    private String practitionerName;

    /**
     * 费用性质
     */
    private String contractName;

    /**
     * 患者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者性别
     */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 就诊状态
     */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /**
     * 挂号日期/时间
     */
    private Date registerTime;

}
