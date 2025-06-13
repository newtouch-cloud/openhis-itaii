package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医嘱请求基础 dto
 */
@Data
@Accessors(chain = true)
public class RequestBaseDto {

    /** 医嘱类型 */
    private Integer adviceType; // 1:药品 , 2: 耗材 , 3:项目

    /**
     * 唯一标识
     */
    private String uniqueKey; // requestId拼接adviceType

    /**
     * 请求人id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_practitioner", dictCode = "id", dictText = "name")
    private Long requesterId;
    private String requesterId_dictText;

    /** 请求时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;

    /**
     * 当前登录账号请求标记 | 1:是,0:否
     */
    private String bizRequestFlag;

    /**
     * 请求内容json
     */
    private String contentJson;

    /**
     * 诊断定义名称
     */
    private String conditionDefinitionName;

    /**
     * 分组id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /**
     * 请求id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestId;

    /**
     * 费用项id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chargeItemId;

    /** 医嘱名称 */
    private String adviceName;

    /**
     * 规格
     */
    private String volume;

    /** 产品批号 */
    private String lotNumber;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位编码 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 请求状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 收费状态 */
    private Integer chargeStatus;
    private String chargeStatus_enumText;

    /** 是否皮试 */
    private Integer skinTestFlag;
    private String skinTestFlag_enumText;

    /** 是否为注射药物 */
    private Integer injectFlag;
    private String injectFlag_enumText;

    /**
     * 用法
     */
    @Dict(dictCode = "method_code")
    private String methodCode;
    private String methodCode_dictText;

    /**
     * 使用频次
     */
    @Dict(dictCode = "rate_code")
    private String rateCode;
    private String rateCode_dictText;

    /**
     * 单次剂量
     */
    private BigDecimal dose;

    /** 剂量单位 */
    @Dict(dictCode = "unit_code")
    private String doseUnitCode;
    private String doseUnitCode_dictText;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 物理位置id | 可能是 发药药房id,耗材房id,执行科室id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long positionId;

    /**
     * 物理位置| 可能是 发药药房,耗材房,执行科室
     */
    private String positionName;

    /** 用药天数 */
    private Integer dispensePerDuration;

    /** 拆零比 */
    private BigDecimal partPercent;

}
