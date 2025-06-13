package com.openhis.yb.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.annotation.Excel;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *【2204】门诊费用明细信息上传
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
public class Clinic2204FeeDetailParam extends HisBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
	//主键
	@TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private String id;
	//就诊ID内部
    @JSONField(serialize=false)
    private String regId;
	//订单主键
    @JSONField(serialize=false)
    private String orderId;
	//定点医药机构编号
    @JSONField(serialize=false)
    private String fixmedinsCode;
	//定点医药机构名称
    @JSONField(serialize=false)
    private String fixmedinsName;
	//就医地医保区划
    @JSONField(serialize=false)
    private String mdtrtareaAdmvs;
	//参保地医保区划
    @JSONField(serialize=false)
    private String insuplcAdmdvs;
	//病历号
    @JSONField(serialize=false)
    private String medicalNo;
	//费用明细流水号
    @JSONField(name="feedetl_sn")
    private String feedetlSn;
	//就诊ID
    @JSONField(name="mdtrt_id")
    private String mdtrtId;
	//人员编号
    @JSONField(name="psn_no")
    private String psnNo;
	//收费批次号
    @JSONField(name="chrg_bchno")
    private String chrgBchno;
	//病种编码
    @JSONField(name="dise_codg")
    private String diseCodg;
	//处方号
    @JSONField(name="rxno")
    private String rxno;
	//外购处方标志
    @Dict(dictCode = "rx_circ_flag")
    @JSONField(name="rx_circ_flag")
    private String rxCircFlag;
	//费用发生时间
	@Excel(name = "费用发生时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(name="fee_ocur_time")
    private Date feeOcurTime;
	//商品名
	@Excel(name = "商品名", width = 15)
    private String tradename;
	//药品类别
    @Dict(dictCode = "list_type")
    private String drugtype;
	//剂型
    @Dict(dictCode = "drug_dosform")
    private String dosemodel;
	//项目等级
    @Dict(dictCode = "chrgitm_lv")
    private String feegrade;
	//处方标志
    @Dict(dictCode = "rx_flag")
    private String rxflag;
	//生产厂家
	@Excel(name = "生产厂家", width = 15)
    private String producingarea;
	//医疗目录编码
	@Excel(name = "医疗目录编码", width = 15)
    @JSONField(name="med_list_codg")
    private String medListCodg;
	//医药机构目录编码
	@Excel(name = "医药机构目录编码", width = 15)
    @JSONField(name="medins_list_codg")
    private String medinsListCodg;
	//明细项目费用总额
	@Excel(name = "明细项目费用总额", width = 15)
    @JSONField(name="det_item_fee_sumamt")
    private Double detItemFeeSumamt;
	//数量
	@Excel(name = "数量", width = 15)
    @JSONField(name="cnt")
    private Double cnt;
	//单价
    @JSONField(name="pric")
    private Double pric;
	//单次剂量描述
    @JSONField(name="sin_dos_dscr")
    private String sinDosDscr;
	//使用频次描述
    @JSONField(name="used_frqu_dscr")
    private String usedFrquDscr;
	//周期天数
    @JSONField(name="prd_days")
    private String prdDays;
	//用药途径描述
    @JSONField(name="medc_way_dscr")
    private String medcWayDscr;
	//开单科室编码
    @JSONField(name="bilg_dept_codg")
    private String bilgDeptCodg;
	//开单科室名称
    @JSONField(name="bilg_dept_name")
    private String bilgDeptName;
	//开单医生编码
    @JSONField(name="bilg_dr_codg")
    private String bilgDrCodg;
	//开单医师姓名
    @JSONField(name="bilg_dr_name")
    private String bilgDrName;
	//受单科室编码
    @JSONField(name="acord_dept_codg")
    private String acordDeptCodg;
	//受单科室名称
    @JSONField(name="acord_dept_name")
    private String acordDeptName;
	//受单医生编码
    @JSONField(name="orders_dr_code")
    private String ordersDrCode;
	//受单医生姓名
    @JSONField(name="orders_dr_name")
    private String ordersDrName;
	//医院审批标志
    @Dict(dictCode = "hosp_appr_flag")
    @JSONField(name="hosp_appr_flag")
    private String hospApprFlag;
	//中药使用方式
    @Dict(dictCode = "tcmdrug_used_way")
    @JSONField(name="tcmdrug_used_way")
    private String tcmdrugUsedWay;
	//外检标志
    @Dict(dictCode = "etip_flag")
    @JSONField(name="etip_flag")
    private String etipFlag;
	//外检医院编码
    @JSONField(name="etip_hosp_code")
    private String etipHospCode;
	//出院带药标志
    @Dict(dictCode = "dscg_tkdrug_flag")
    @JSONField(name="dscg_tkdrug_flag")
    private String dscgTkdrugFlag;
	//生育费用标志
    @Dict(dictCode = "matn_fee_flag")
    @JSONField(name="matn_fee_flag")
    private String matnFeeFlag;
	//定价上限金额
    @JSONField(name="pric_uplmt_amt")
    private Double pricUplmtAmt;
	//自付比例
    @JSONField(name="selfpay_prop")
    private Double selfpayProp;
	//全自费金额
    @JSONField(name="fulamt_ownpay_amt")
    private Double fulamtOwnpayAmt;
	//超限价金额
    @JSONField(name="overlmt_amt")
    private Double overlmtAmt;
	//先行自付金额
    @JSONField(name="preselfpay_amt")
    private Double preselfpayAmt;
	//符合政策范围金额
    @JSONField(name="inscp_scp_amt")
    private Double inscpScpAmt;
	//收费项目等级
    @Dict(dictCode = "chrgitm_lv")
    @JSONField(name="chrgitm_lv")
    private String chrgitmLv;
	//医疗收费项目类别
    @Dict(dictCode = "med_chrgitm_type")
    @JSONField(name="med_chrgitm_type")
    private String medChrgitmType;
	//基本药物标志
    @Dict(dictCode = "bas_medn_flag")
    @JSONField(name="bas_medn_flag")
    private String basMednFlag;
	//医保谈判药品标志
    @Dict(dictCode = "hi_nego_drug_flag")
    @JSONField(name="hi_nego_drug_flag")
    private String hiNegoDrugFlag;
	//儿童用药标志
    @Dict(dictCode = "chld_medc_flag")
    @JSONField(name="chld_medc_flag")
    private String chldMedcFlag;
	//目录特项标志
    @Dict(dictCode = "list_sp_item_flag")
    @JSONField(name="list_sp_item_flag")
    private String listSpItemFlag;
	//限制使用标志
    @Dict(dictCode = "lmt_used_flag")
    @JSONField(name="lmt_used_flag")
    private String lmtUsedFlag;
	//直报标志
    @Dict(dictCode = "drt_reim_flag")
    @JSONField(name="drt_reim_flag")
    private String drtReimFlag;
	//备注
    @JSONField(name="memo")
    private String memo;
	//上报状态，0-未上报，1-已上报
    @JSONField(serialize=false)
    private String status;
    private String district;
    @TableField(exist = false)
    private String setlTimeStart;
    @TableField(exist = false)
    private String setlTimeEnd;
}
