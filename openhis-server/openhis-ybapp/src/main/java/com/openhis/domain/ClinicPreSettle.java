/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 【2206】医保预结算实体
 *
 * @author SunJQ
 * @date 2025-04-19
 */
@Data
@TableName("yb_clinc_pre_settle")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ClinicPreSettle extends HisBaseEntity implements Serializable {

    //主键
    @TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private Long id;

    //就诊ID
    @JSONField(name="mdtrt_id")
    private String mdtrtId;

    //内部就诊ID
    @JSONField(serialize=false)
    private String regId;

    //人员编号
    @JSONField(name="psn_no")
    private String psnNo;

    //结算批次号
    @JSONField(name="chrg_bchno")
    private String chrgBchno;//对应payment的显示用流水

    private String param2203;

    private String result2203;

    private String param2204;

    private String result2204;

    private String param2206;

    private String result2206;

}
