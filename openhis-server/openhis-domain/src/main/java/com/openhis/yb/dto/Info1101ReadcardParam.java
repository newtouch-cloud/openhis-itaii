package com.openhis.yb.dto;

import java.io.Serializable;

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
public class Info1101ReadcardParam extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	//就诊凭证类型
    @JSONField(name="mdtrt_cert_type")
    private String mdtrtCertType;
	//就诊凭证编号
    @JSONField(name="mdtrt_cert_no")
    private String mdtrtCertNo;
	//卡识别码
    @JSONField(name="card_sn")
    private String cardSn;
	//开始时间
    @JSONField(name="begntime")
    private String begntime;
	//人员证件类型
    @JSONField(name="psn_cert_type")
    private String psnCertType;
	//证件号码
    @JSONField(name="certno")
    private String certno;
	//人员姓名
    @JSONField(name="psn_name")
    private String psnName;
}
