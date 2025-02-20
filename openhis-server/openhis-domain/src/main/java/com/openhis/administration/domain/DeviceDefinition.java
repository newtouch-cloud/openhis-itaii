package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 器材定义管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_device_definition")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DeviceDefinition extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 编码 */
    private String code;

    /** 器材名称 */
    private String name;

    /** 器材名称拼音 */
    private String pyCode;

    /** 器材五笔拼音 */
    private String wbCode;

    /** 器材分类 */
    private String deviceClass;

    /** 器材种类 */
    private String deviceTypeCode;

    /** 包装单位 */
    private String unitCode;

    /** 包装规格 */
    private String size;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 最小使用单位 */
    private String minUnitCode;

    /** 产品型号 */
    private String modelNumber;

    /** 高值器材标志 */
    private Integer hvcmFlag;

    /** 销售单位 */
    private String salesUnitCode;

    /** 批准文号 */
    private String approvalNumber;

    /** 医保标记 */
    private Integer ybFlag;

    /** 医保编码 */
    private String ybCode;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 状态 */
    private Integer status;

    /** 生产厂家 */
    private Long manufacturerId;

    /** 供应商 */
    private Long supplyId;

    /** 说明 */
    private String description;

    /** 适用范围 */
    private Long jurisdiction;

    /** 执行科室 */
    private Long ruleId;

    /** 器材版本 */
    private String version;

    /** 主要成分 */
    private String substanceText;

    /** 过敏标记 */
    private Integer allergenFlag;

    /** 删除状态 */
    private Integer deleteFlag;

}