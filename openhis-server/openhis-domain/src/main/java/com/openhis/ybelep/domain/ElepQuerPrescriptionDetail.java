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
 * 【电子处方信息查询-输出-处方明细信息（节点标识：rxDetlList） 】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_quer_prescription_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepQuerPrescriptionDetail extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 医疗目录编码(医保目录编码) */
    private String medListCodg;

    /** 定点医药机构目录编号(院内药品编码) */
    private String fixmedinsHilistId;

    /** 院内制剂标志(Y/N) */
    private String hospPrepFlag;

    /** 处方项目分类代码 */
    private String rxItemTypeCode;

    /** 处方项目分类名称 */
    private String rxItemTypeName;

    /** 中药类别名称 */
    private String tcmdrugTypeName;

    /** 中药类别代码 */
    private String tcmdrugTypeCode;

    /** 草药脚注 */
    private String tcmherbFoote;

    /** 药物类型代码 */
    private String mednTypeCode;

    /** 药物类型 */
    private String mednTypeName;

    /** 主要用药标志 */
    private String mainMedcFlag;

    /** 加急标志 */
    private String urgtFlag;

    /** 基本药物标志(Y/N) */
    private String basMednFlag;

    /** 基本药物标志(Y/N) */
    private String impDrugFlag;

    /** 药品商品名 */
    private String drugProdname;

    /** 药品通用名 */
    private String drugGenname;

    /** 药品剂型 */
    private String drugDosform;

    /** 药品规格 */
    private String drugSpec;

    /** 生产厂家 */
    private String prdrName;

    /** 用药途径代码 */
    private String medcWayCodg;

    /** 用药途径描述 */
    private String medcWayDscr;

    /** 用药开始时间(yyyy-MM-dd HH:mm:ss) */
    private String medcBegntime;

    /** 用药结束时间(yyyy-MM-dd HH:mm:ss) */
    private String medcEndtime;

    /** 用药天数 */
    private BigDecimal medcDays;

    /** 药品总用药量 */
    private BigDecimal drugCnt;

    /** 药品总用药量单位 */
    private String drugDosunt;

    /** 单次用量 */
    private BigDecimal sinDoscnt;

    /** 单次剂量单位 */
    private String sinDosunt;

    /** 使用频次编码 */
    private String usedFrquCodg;

    /** 使用频次名称 */
    private String usedFrquName;

    /** 医院审批标志(Y/N) */
    private String hospApprFlag;

    /** 取药标志位(Y/N) */
    private String takeDrugFlag;

    /** 是否OTC药品(0-处方药品、1-OTC药品) */
    private String otcFlag;

    /** 自费原因类型 */
    private String selfPayRea;

    /** 自费原因描述 */
    private String realDscr;

    /** 所需药品库存数量 */
    private String drugTotlcnt;

    /** 所需药品库存单位 */
    private String drugTotlcntEmp;

}