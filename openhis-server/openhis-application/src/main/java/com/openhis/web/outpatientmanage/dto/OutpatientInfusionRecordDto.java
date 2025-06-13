package com.openhis.web.outpatientmanage.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 门诊输液记录Dto
 *
 * @author liuhr
 * @date 2025/3/12
 */
@Data
@Accessors(chain = true)
public class OutpatientInfusionRecordDto {

    /**
     * 服务申请管理表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serviceId;

    /**
     * 诊疗名称
     */
    private String serviceName;

    /**
     * 服务申请状态
     */
    private Integer serviceStatus;
    private String serviceStatus_enumText;

    /**
     * 请求基于什么的ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long basedOnId;

    /**
     * 服务请求编码
     */
    private String busNo;

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 执行次数
     */
    private Integer executeNum;

    /**
     * 已经行次数
     */
    private Integer performCount;

    /**
     * 分组id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /**
     * 执行护士
     */
    private String performerName;

    /**
     * 执行科室
     */
    private String orgName;

    /**
     * 开单医生
     */
    private String practitionerName;

    /**
     * 预计结束时间
     */
    private String occurrenceEndTime;

    /**
     * 药品名称
     */
    private String medicationName;

    /**
     * 药品数量
     */
    private BigDecimal quantity;

    /**
     * 请求单位编码
     */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /**
     * 单次剂量
     */
    private BigDecimal dose;

    /**
     * 单次剂量单位
     */
    @Dict(dictCode = "unit_code")
    private String doseUnitCode;
    private String doseUnitCode_dictText;

    /**
     * 用法
     */
    @Dict(dictCode = "method_code")
    private String methodCode;
    private String methodCode_dictText;

    /**
     * 用药频次
     */
    @Dict(dictCode = "rate_code")
    private String rateCode;
    private String rateCode_dictText;

    /**
     * 执行人
     */
    @Dict(dictTable = "adm_practitioner", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long performerId;
    private String performerId_dictText;

    /**
     * 配药人
     */
    @Dict(dictTable = "adm_practitioner", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long preparerId;
    private String preparerId_dictText;

    /**
     * 输液速度
     */
    private Integer speed;

    /**
     * 发放状态
     */
    private Integer dispenseStatus;
    private String dispenseStatus_enumText;

    /**
     * 皮试标志（是/否）
     */
    private Integer skinTestFlag;
    private String skinTestFlag_enumText;

    /**
     * 开单时间
     */
    private String authoredTime;

    /**
     * 打印次数
     */
    private Integer printCount;

    /**
     * 执行科室名称
     */
    private String deptName;


}
