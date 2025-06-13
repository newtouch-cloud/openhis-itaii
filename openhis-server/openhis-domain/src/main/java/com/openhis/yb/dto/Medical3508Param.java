/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【3508】定点医药机构商品库存信息查询
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Medical3508Param {
    // 定点医药机构编号
    private String fixmedinsCode;

    // 医药机构目录编码
    private String medinsListCodg;

    // 定点医药机构批次流水号
    private String fixmedinsBchno;

    // 开始日期
    private Date begndate;

    // 结束日期
    private Date enddate;

    // 定点医药机构商品库存流水号
    private String medinsProdInvNo;

    // 医疗目录编码
    private String medListCodg;

    // 医药机构目录名称
    private String medinsListName;

    // 处方药标志
    private String rxFlag;

    // 目录特项标志
    private String listSpItemFlag;

    // 拆零标志
    private String trdnFlag;

    // 库存日期
    private Date invdate;

    // 生产批号
    private String manuLotnum;

    // 生产日期
    private Date manuDate;

    // 有效期止
    private Date expyEnd;

    // 备注
    private String memo;

    // 有效标志
    private String valiFlag;

    // 数据唯一记录号
    private String rid;

    // 创建人ID
    private String crterId;

    // 创建人姓名
    private String crterName;

    // 创建机构编号
    private String crteOptinsNo;

    // 经办人ID
    private String opterId;

    // 经办人姓名
    private String opterName;

    // 经办机构编号
    private String optinsNo;

    // 统筹区编号
    private String poolareaNo;

    // 说明：medins_list_codg和fixmedins_bchno为互斥必填项，至少传其中一个字段
}

