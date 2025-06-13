package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *【2206】门诊预结算
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
public class Clinic2206OrderParam extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	//主键
	@TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private String id;
	//内部就诊ID
    @JSONField(serialize=false)
    private String regId;
	//人员姓名
    @JSONField(serialize=false)
    private String name;
	//性别
    @Dict(dictCode = "sex")
    @JSONField(serialize=false)
    private String sex;
	//民族
    @Dict(dictCode = "naty")
    @JSONField(serialize=false)
    private String naty;
	//出生日期
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(serialize=false)
    private Date brdy;
	//年龄
    @JSONField(serialize=false)
    private Double age;
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
    @JSONField(name="insuplc_admdvs")
    private String insuplcAdmdvs;
	//病历号
    @JSONField(serialize=false)
    private String medicalNo;
	//人员编号
    @JSONField(name="psn_no")
    private String psnNo;
	//就诊凭证类型
    @Dict(dictCode = "mdtrt_cert_type")
    @JSONField(name="mdtrt_cert_type")
    private String mdtrtCertType;
	//就诊凭证编号
    @JSONField(name="mdtrt_cert_no")
    private String mdtrtCertNo;
    //就诊凭证加密
    @TableField(exist = false)
    @JSONField(serialize=false)
    private String mdtrtCertNoEncrypt;
	//医疗类别
    @Dict(dictCode = "med_type")
    @JSONField(name="med_type")
    private String medType;
	//医疗费总额
    @JSONField(name="medfee_sumamt")
    private Double medfeeSumamt;
	//个人结算方式
    @Dict(dictCode = "psn_setlway")
    @JSONField(name="psn_setlway")
    private String psnSetlway;
	//就诊ID
    @JSONField(name="mdtrt_id")
    private String mdtrtId;
	//收费批次号
    @JSONField(name="chrg_bchno")
    private String chrgBchno;
	//险种类型
    @Dict(dictCode = "insutype")
    @JSONField(name="insutype")
    private String insutype;
	//个人账户使用标志
    @Dict(dictCode = "acct_used_flag")
    @JSONField(name="acct_used_flag")
    private String acctUsedFlag;
}
