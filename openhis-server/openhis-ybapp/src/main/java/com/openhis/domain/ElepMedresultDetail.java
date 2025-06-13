package com.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 【电子处方取药结果查询-输出（节点标识：seltdelts）】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_medresult_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepMedresultDetail extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 医疗目录编码 */
    private String medListCodg;

    /** 药品通用名 */
    private String drugGenname;

    /** 药品商品名 */
    private String drugProdname;

    /** 药品剂型 */
    private String drugDosform;

    /** 药品规格 */
    private BigDecimal drugSpec;

    /** 数量 */
    private BigDecimal cnt;

    /** 批准文号 */
    private String aprvno;

    /** 批次号 */
    private String bchno;

    /**  生产批号 */
    private String manuLotnum;

    /** 生产厂家 */
    private String prdrName;

    /** 取药标志位 */
    private String takeDrugFlag;

}