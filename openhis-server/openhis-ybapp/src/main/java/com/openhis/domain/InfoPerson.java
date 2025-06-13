package com.openhis.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.vo.Info5301SpecialConditionResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *【1101】获取身份信息
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@TableName("yb_pub_perinfo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InfoPerson extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    @TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private Long id;

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
    private String insuplcAdmdvs;
    //险种类型
    private String insutype;
    //余额
    private String balc;

    private String param1101;

    private String result1101;

    //人员类别
    private String psnType;

    //人员参保状态
    private String psnInsuStas;

    //个人参保日期
    private String psnInsuDate;

    //公务员标志
    private String cvlservFlag;

    //单位名称
    private String empName;

    //特慢病
    @TableField(exist = false)
    private List<Info5301SpecialConditionResult> feedetail;
}
