package com.openhis.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


/**
 * 电子处方上传预核验
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PreCheckPrescription {

    // 处方信息

    /** 就诊凭证类型(01-电子凭证令牌、02-身份证号、03-社会保障卡号) */
    private String mdtrtCertType;

    /** 就诊凭证编号 */
    private String mdtrtCertNo;

    /** 卡识别码(就诊凭证类型为"03"时必填) */
    private String cardSn;

    /** 业务类型代码(01-定点医疗机构就诊，02-互联网医院问诊) */
    private String bizTypeCode;

    /** 处方附加属性代码(01-双通道处方，02-门诊统筹处方，99-其他) */
    private String rxExraAttrCode;

    /** 电子凭证令牌(使用医保电子凭证就诊时必填) */
    private String ecToken;

    /** 电子凭证线上身份核验流水号(线上场景互联网医院问诊时使用) */
    private String authNo;

    /** 参保地编号 */
    private String insuPlcNo;

    /** 就医地编号 */
    private String mdtrtareaNo;

    /** 定点医疗机构处方编号(院内内部处方号) */
    private String hospRxno;

    /** 续方的原处方编号 */
    private String initRxno;

    /** 处方类别代码(参考rx_type_code) */
    private String rxTypeCode;

    /** 开方时间(yyyy-MM-dd HH:mm:ss) */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date prscTime;

    /** 药品类目数(剂数) */
    private String rxDrugCnt;

    /** 处方整剂用法编号 */
    private String rxUsedWayCodg;

    /** 处方整剂用法名称 */
    private String rxUsedWayName;

    /** 处方整剂频次编号(参考used_frequ) */
    private String rxFrquCodg;

    /** 处方整剂频次名称 */
    /** 处方整剂频次名称 */
    private String rxFrquName;

    /** 处方整剂剂量单位 */
    private String rxDosunt;

    /** 处方整剂单次剂量数 */
    private String rxDoscnt;

    /** 处方整剂医嘱说明 */
    private String rxDrordDscr;

    /** 处方有效天数 */
    private String valiDays;

    /** 有效截止时间(开方时间+处方有效天数) */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date valiEndTime;

    /** 复用(多次)使用标志(0-否、1-是) */
    private String reptFlag;

    /** 最大使用次数(预留字段) */
    private String maxReptCnt;

    /** 使用最小间隔(天数)(预留字段) */
    private String minInvDays;

    /** 续方标志(0-否、1-是) */
    private String rxCotnFlag;

    /** 长期处方标志(0-否、1-是) */
    private String longRxFlag;

    /** 医保处方编号(电子处方信息查询返回时插入记录) */
    @JSONField(serialize=false)
    private String hiRxno;

    // 处方明细信息
    private List<ElepVeriPrescriptionDetail> rxdrugdetail;

    // 就诊信息
    private ElepVeriVisitInfo mdtrtinfo;

    // 诊断信息
    private ElepVeriDiagnosisInfo diseinfo;

}
