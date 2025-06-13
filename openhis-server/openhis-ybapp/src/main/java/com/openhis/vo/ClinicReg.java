package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *【2201】【2202】挂号 退号
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ClinicReg extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	//主键
	@TableId(type = IdType.ASSIGN_ID)
	@JSONField(serialize=false)
    private String id;
	//就诊ID
	@JSONField(name="mdtrt_id")
    private String mdtrtId;
	//参保地医保区划
	@JSONField(serialize=false)
    private String insuplcAdmdvs;
	//人员编号
	@JSONField(name="psn_no")
    private String psnNo;
	//险种类型
	//@Dict(dictCode = "insutype")
	@JSONField(name="insutype")
    private String insutype;
	//医疗类别
	//@Dict(dictCode = "med_type")
	@JSONField(serialize=false)
    private String medType;
	//挂号时间
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JSONField(name="begntime")
    private Date begntime;
	//就诊凭证类型
	@Dict(dictCode = "mdtrt_cert_type")
	@JSONField(name="mdtrt_cert_type")
    private String mdtrtCertType;
	//就诊凭证编号
	@JSONField(name="mdtrt_cert_no")
    private String mdtrtCertNo;
//	//证件加密串
//	@JSONField(serialize=false)
//	@TableField(exist = false)
//	private String mdtrtCertNoEncrypt;
	//住院/门诊号
	@JSONField(name="ipt_otp_no")
    private String iptOtpNo;//使用bus_no
	//医师编码
	//@Dict(dictTable = "business_pub_medicalstaff", dictText = "name", dictCode = "id")
	@JSONField(name="atddr_no")
    private String atddrNo;
	//医师姓名
	@JSONField(name="dr_name")
    private String drName;
	//科室编码
	//@Dict(dictTable = "business_pub_dept", dictText = "hosp_dept_name", dictCode = "id")
	@JSONField(name="dept_code")
    private String deptCode;
	//科室名称
	@JSONField(name="dept_name")
    private String deptName;
	//科别
	@Dict(dictCode = "dept")
	@JSONField(name="caty")
    private String caty;
	//挂号费
	@JSONField(serialize=false)
    private Double regFee;
	//检查费
	@JSONField(serialize=false)
    private Double checkFee;
	//应收金额 
	@JSONField(serialize=false)
    private Double totalFee;
	//状态,0-挂号,1-看诊，2-费用明细，3-预结算，4-结算
	@JSONField(serialize=false)
	@Dict(dictCode = "clinc_status")
    private String status;

	@JSONField(name="psn_cert_type")
	@TableField(exist = false)
	private String psnCertType;
	@JSONField(name="certno")
	@TableField(exist = false)
	private String certno;
	@JSONField(name="psn_type")
	@TableField(exist = false)
	private String psnType;
	@JSONField(name="psn_name")
	@TableField(exist = false)
	private String psnName;
}
