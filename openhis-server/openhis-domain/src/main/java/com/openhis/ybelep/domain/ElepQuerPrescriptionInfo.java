package com.openhis.ybelep.domain;

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
 * 【电子处方信息查询-输出-处方信息 】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_quer_prescription_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepQuerPrescriptionInfo extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 医保处方编号 */
    private String hiRxno;

    /** 定点医疗机构编号 */
    private String fixmedinsCode;

    /** 定点医疗机构名称 */
    private String fixmedinsName;

    /** 医保处方状态编码 */
    private String rxStasCodg;

    /** 医保处方状态名称 */
    private String rxStasName;

    /** 医保处方使用状态编码 */
    private String rxUsedStasCodg;

    /** 医保处方使用状态名称 */
    private String rxUsedStasName;

    /** 开方时间 */
    private String prscTime;

    /** 药品总用量（剂数） */
    private BigDecimal rxDrugCnt;

    /** 处方整剂用法编码 */
    private String rxUsedWayCodg;

    /** 处方整剂用法名称 */
    private String rxUsedWayName;

    /** 处方整剂频次编号 */
    private String rxFrquCodg;

    /** 处方整剂频次名称 */
    private String rxFrquName;

    /** 处方整剂剂量单位 */
    private String rxDosunt;

    /** 处方整剂单次剂量数 */
    private BigDecimal rxDoscnt;

    /** 处方整剂医嘱说明 */
    private String rxDrordDscr;

    /** 处方有效天数 */
    private BigDecimal valiDays;

    /** 有效截止时间 */
    private String valiEndTime;

    /** 复用（多次）使用标志，0-否、1-是 */
    private String reptFlag;

    /** 最大复用次数 */
    private BigDecimal maxReptCnt;

    /** 已复用次数 */
    private BigDecimal reptdCnt;

    /** 使用最小间隔（天数） */
    private BigDecimal minInrvDays;

    /** 处方类别编码 */
    private String rxTypeCode;

    /** 处方类别名称 */
    private String rxTypeName;

    /** 长期处方标志，0-否、1-是 */
    private String longRxFlag;

    /** 业务类型代码 */
    private String bizTypeCode;

    /** 业务类型名称 */
    private String bizTypeName;

    /** 处方附加属性代码 */
    private String rxExraAttrCode;

    /** 处方附加属性名称 */
    private String rxExraAttrName;

    /** 处方循环限制标志 */
    private String rxCircLimitFlag;
}