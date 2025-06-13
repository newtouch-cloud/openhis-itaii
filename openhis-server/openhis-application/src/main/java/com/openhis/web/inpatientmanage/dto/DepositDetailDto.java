package com.openhis.web.inpatientmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 预交金信息
 *
 * @author gaoyy
 * @since 2025/05/20
 */
@Data
@Accessors(chain = true)
public class DepositDetailDto {

    /** 患者ID(住院号) */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "住院号不能为空")
    private Long patientId;

    /** 患者姓名 */
    private String name;

    /** 性别编码 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;

    /** 病人年龄 */
    private String ageString;

    /** 床位号 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private Long bedLocationId;
    private String bedLocationId_dictText;

    /** 机构id(科室) */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    private Long organizationId;
    private String organizationId_dictText;

    /** 总额 */
    private BigDecimal totalPrice;

    /** 预交金 */
    private BigDecimal deposit;

    /** 余额 */
    private BigDecimal balanceAmount;

    /** 支付方式 */
    private String payWay;

    /** 支付状态 */
    private Integer paymentEnum;
    private String paymentEnum_enumText;

    /** 支付金额 */
    private BigDecimal tenderedAmount;

    /** 收据号 */
    private String busNo;

    /** 票据状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 可退金额 */
    private BigDecimal afterBalance;

    /** 收款时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 收款人 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictCode = "id", dictTable = "adm_practitioner", dictText = "name")
    private Long entererId;
    private String entererId_dictText;
}
