package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;

import com.core.common.utils.SecurityUtils;
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

    /**
     * 医嘱详细分类
     */
    private String categoryCode;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 拆分属性 */
    private Integer partAttributeEnum;

    /** 执行次数 */
    private Integer executeNum; // 当医嘱类型为药品时,选填

    /** 处方号 */
    private String prescriptionNo;

    /** 请求数量 */
    private Integer quantity;

    /** 每次发药供应天数 */
    private Integer dispensePerDuration;

    /** 请求单位编码 */
    private String unitCode;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

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

    /**
     * 医嘱对应表名
     */
    private String adviceTableName;

    /** 患者 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 开方医生 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;

    /** 请求发起的位置 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 发放位置 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long performLocation;

    /** 所属科室 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;

    /** 就诊id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 账户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 诊断ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long conditionId;

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
     * 皮试标志 1:是 , 0:否
     */
    private Integer skinTestFlag;

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
        this.practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        this.orgId = SecurityUtils.getLoginUser().getOrgId(); // 开方人科室
    }

}
