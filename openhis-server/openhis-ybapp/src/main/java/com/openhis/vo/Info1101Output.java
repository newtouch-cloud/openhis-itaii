package com.openhis.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.core.common.core.domain.HisBaseEntity;
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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Info1101Output extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	//人员编号
    private String psnNo;
	//人员证件类型
    private String psnCertType;
	//证件号码
    private String certno;
	//人员姓名
    private String psnName;
	//性别
    private String gend;
	//民族
    private String naty;
	//出生日期
    private Date brdy;
	//年龄
    private Double age;
	//参保地医保区划
    private String insuplcAdmdvs;
    //险种类型
    private String insutype;
    //余额
    private String balc;
    //人员类别
    @TableField(exist = false)
    private String psnType;

    //人员参保状态
    @TableField(exist = false)
    private String psnInsuStas;

    //个人参保日期
    @TableField(exist = false)
    private String psnInsuDate;

    //公务员标志
    @TableField(exist = false)
    private String cvlservFlag;

    //单位名称
    @TableField(exist = false)
    private String empName;
    //特慢病
    @TableField(exist = false)
    private List<Info5301SpecialConditionResult> feedetail;
}
