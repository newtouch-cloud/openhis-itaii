package com.openhis.ybelep.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 【电子处方上传预核验-输入-处方明细信息（节点标识 rxdrugdetail） 】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_veri_prescription_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepVeriPrescriptionDetail extends HisBaseEntity {

    /** 自增主键 */
    @JSONField(serialize=false)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 医疗目录编码(医保药品编码) */
    private String medListCodg;

    /** 定点医药机构目录编号(院内药品编码) */
    private String fixmedinsHilistId;

    /** 医疗机构制剂标志(0-否、1-是) */
    private String hospPrepFlag;

    /** 处方项目分类代码(11:西药,12:中成药,13:中药饮片) */
    private String rxItemTypeCode;

    /** 处方项目分类名称 */
    private String rxItemTypeName;

    /** 中药类别代码(中药饮片固定传3) */
    private String tcmdrugTypeCode;

    /** 中药类别名称 */
    private String tcmdrugTypeName;

    /** 草药脚注 */
    private String tcmherbFoote;

    /** 药物类型代码(参考medn_type_code) */
    private String mednTypeCode;

    /** 药物类型名称 */
    private String mednTypeName;

    /** 主要用药标志(0-否、1-是) */
    private String mainMedcFlag;

    /** 加急标志(0-否、1-是) */
    private String urgtFlag;

    /** 基本药物标志(0-否、1-是) */
    private String basMednFlag;

    /** 是否进口药品(0-否、1-是) */
    private String impDrugFlag;

    /** 是否OTC药品(0-处方药品、1-OTC药品) */
    private String otcFlag;

    /** 药品通用名 */
    private String drugGenname;

    /** 药品剂型 */
    private String drugDosform;

    /** 药品规格 */
    private String drugSpec;

    /** 药品商品名(非必填) */
    private String drugProdname;

    /** 生产厂家(非必填) */
    private String prdrName;

    /** 用药途径代码(西药/中成药必填) */
    private String medcWayCodg;

    /** 用药途径描述(西药/中成药必填) */
    private String medcWayDscr;

    /** 用药开始时间(yyyy-MM-dd HH:mm:ss) */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date medcBegntime;

    /** 用药结束时间(yyyy-MM-dd HH:mm:ss) */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date medcEndtime;

    /** 用药天数 */
    private String medcDays;

    /** 单次剂量单位(西药/中成药必填) */
    private String sinDosunt;

    /** 单次用量(西药/中成药必填) */
    private String sinDoscnt;

    /** 使用频次编码(西药/中成药必填) */
    private String usedFrquCodg;

    /** 使用频次名称(西药/中成药必填) */
    private String usedFrquName;

    /** 药品总用药量单位(发药计价单位) */
    private String drugDosunt;

    /** 药品总用药量(医保结算数量) */
    private String drugCnt;

    /** 药品单价(按drug_dosunt计价) */
    private String drugPric;

    /** 药品总金额(drug_cnt×drug_pric) */
    private String drugSumamt;

    /** 医院审批标志(1-纳入报销,2-自费) */
    private String hospApprFlag;

    /** 自费原因类型(hosp_appr_flag=2时必填) */
    private String selfPayRea;

    /** 自费原因描述(自费原因类型为6时必填) */
    private String realDscr;

    /** 扩展数据(地方业务扩展信息) */
    private String extras;

    /** 院内内部处方号 */
    @JSONField(serialize=false)
    private String prescriptionNo;

    /** 医保处方编号(电子处方信息查询返回时插入记录) */
    @JSONField(serialize=false)
    private String hiRxno;

}