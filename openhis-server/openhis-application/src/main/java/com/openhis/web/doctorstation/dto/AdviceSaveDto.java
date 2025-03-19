package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.EncounterClass;
import com.openhis.common.enums.RequestStatus;
import com.openhis.common.enums.TherapyTimeType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医嘱保存 dto
 */
@Data
@Accessors(chain = true)
public class AdviceSaveDto {

    /** 医嘱类型 */
    private Integer adviceType; // 1:药品 , 2: 耗材 , 3:项目

    /** 执行次数 */
    private Integer executeNum; // 当医嘱类型为药品时,选填

    /** 处方号 */
    private String prescriptionNo;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位编码 */
    private String unitCode;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 费用定价主表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /** 费用定价子表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionDetailId;

    /** 产品批号 */
    private String lotNumber;

    /**
     * 请求状态
     */
    private Integer statusEnum;

    /** 请求类型 */
    private Integer categoryEnum;

    /** 医嘱定义ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long adviceDefinitionId;

    /** 患者 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 开方医生 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;

    /** 所属位置 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 所属科室 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;

    /** 就诊id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 治疗类型 */
    private Integer therapyEnum;

    /** 用法 */
    private String methodCode;

    /** 用药频次 */
    private String rateCode;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 剂量单位 */
    private String doseUnitCode;

    /**
     * 分组id , 一组药品共用一个id,前端传过来
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /** 组套id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long packageId;

    /** 活动(项目)定义id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    /**
     * 设置默认值
     */
    public AdviceSaveDto() {
        this.statusEnum = RequestStatus.DRAFT.getValue();
        this.categoryEnum = EncounterClass.AMB.getValue();
        this.therapyEnum = TherapyTimeType.TEMPORARY.getValue();
        // TODO: 应该从当前登录账号获取参与者id,现在没有
        // this.practitionerId
    }

}
