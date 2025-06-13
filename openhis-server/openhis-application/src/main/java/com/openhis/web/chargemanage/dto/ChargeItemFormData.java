package com.openhis.web.chargemanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.core.common.utils.SecurityUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.ChargeItemContext;
import com.openhis.common.enums.ChargeItemStatus;
import com.openhis.common.enums.EncounterClass;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 费用项管理 表单数据
 */
@Data
@Accessors(chain = true)
public class ChargeItemFormData {

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 患者id */
    @NotNull(message = "患者id不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 层级 */
    private String busNo;

    /** 状态 */
    private Integer statusEnum;

    /** 类别 */
    private Integer contextEnum;

    /** 发生时间 */
    private Date occurrenceTime;

    /** 执行人Id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long performerId;

    /** 费用定价ID */
    @NotNull(message = "费用定价ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /** 开立人ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long entererId;

    /** 开立时间 */
    private Date enteredDate;

    /** 医疗服务类型 */
    private String serviceTable;

    /** 医疗服务ID */
    @NotNull(message = "医疗服务ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serviceId;

    /** 总价 */
    @NotNull(message = "总价不能为空")
    private BigDecimal totalPrice;

    /** 关联账户ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 设置默认值
     */
    public ChargeItemFormData() {
        this.statusEnum = ChargeItemStatus.PLANNED.getValue();
        this.contextEnum = ChargeItemContext.REGISTER.getValue();
        this.occurrenceTime = new Date();
        this.performerId = SecurityUtils.getLoginUser().getPractitionerId();
        this.entererId = SecurityUtils.getLoginUser().getPractitionerId();
        this.enteredDate = new Date();
        this.serviceTable = CommonConstants.TableName.ADM_HEALTHCARE_SERVICE;
    }

}
