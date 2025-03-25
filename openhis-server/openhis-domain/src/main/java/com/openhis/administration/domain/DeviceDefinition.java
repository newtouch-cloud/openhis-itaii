package com.openhis.administration.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.DeviceCategory;
import com.openhis.common.enums.PublicationStatus;

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
    private String busNo;

    /** 器材名称 */
    private String name;

    /** 器材名称拼音 */
    private String pyStr;

    /** 器材五笔拼音 */
    private String wbStr;

    /** 器材分类 */
    private DeviceCategory categoryEnum;

    /** 器材种类 */
    private String typeCode;

    /** 包装单位 */
    private String unitCode;

    /** 包装规格 */
    private String size;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 最小使用单位 */
    private String minUnitCode;

    /** 所属科室 */
    private Long orgId;

    /** 所在位置 */
    private Long locationId;

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
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 状态 */
    private PublicationStatus statusEnum;

    /** 生产厂家 */
    private Long manufacturerId;

    /** 生产厂家 */
    private String manufacturerText;

    /** 供应商 */
    private Long supplyId;

    /** 说明 */
    private String description;

    /** 适用范围 */
    private String jurisdiction;

    /** 器材版本 */
    private String version;

    /** 主要成分 */
    private String substanceText;

    /** 过敏标记 */
    private Integer allergenFlag;

}
