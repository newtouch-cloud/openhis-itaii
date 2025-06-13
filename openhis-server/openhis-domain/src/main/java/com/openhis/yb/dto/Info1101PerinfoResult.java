package com.openhis.yb.dto;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【1101】获取身份信息
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Info1101PerinfoResult extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	//人员编号
    @JSONField(name="psn_no")
    private String psnNo;
	//人员证件类型
    @JSONField(name="psn_cert_type")
    private String psnCertType;
	//证件号码
    @JSONField(name="certno")
    private String certno;
	//人员姓名
    @JSONField(name="psn_name")
    private String psnName;
	//性别
    @JSONField(name="gend")
    private String gend;
	//民族
    @JSONField(name="naty")
    private String naty;
	//出生日期
    @JSONField(name="brdy")
    private Date brdy;
	//年龄
    @JSONField(name="age")
    private Double age;
	//参保地医保区划
    @JSONField(serialize = false)
    private String insuplcAdmdvs;
    //险种类型
    @JSONField(serialize = false)
    private String insutype;
    //余额
    @JSONField(serialize = false)
    private String balc;
}
