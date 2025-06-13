/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 【2202】挂号撤销
 *
 * @author SunJQ
 * @date 2025-04-21
 */
@Data
@TableName("yb_clinc_un_reg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ClinicUnReg {

    //主键
    @TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private Long id;

    //就诊ID
    @JSONField(name="mdtrt_id")
    private String mdtrtId;

    //人员编号
    @JSONField(name="psn_no")
    private String psnNo;

    //住院/门诊号
    @JSONField(name="ipt_otp_no")
    private String iptOtpNo;

    //取消挂号时间
    private Date unRegTime;
}
