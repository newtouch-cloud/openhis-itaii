/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 *【3508】定点医药机构商品库存信息查询
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Medical3508Result {

    // 有效期止
    private Date expyEnd;

    // 医疗目录编码
    private String medListCodg;

    // 统筹区编号
    private String poolareaNo;

    // 创建机构编号
    private String crteOptinsNo;

    // 库存数量
    private BigDecimal invCnt;

    // 医药机构目录编码
    private String medinsListCodg;

    // 备注
    private String memo;

    // 数据更新时间
    private Date updtTime;

    // 生产日期
    private Date manuDate;

    // 经办人姓名
    private String opterName;

    // 数据唯一记录号
    private String rid;

    // 数据创建时间
    private Date crteTime;

    // 库存日期
    private Date invdate;

    // 有效标志
    private String valiFlag;

    // 定点医药机构编号
    private String fixmedinsCode;

    // 处方药标志
    private String rxFlag;

    // 目录特项标志
    private String listSpItemFlag;

    // 定点医药机构批次流水号
    private String fixmedinsBchno;

    // 经办时间
    private Date optTime;

    // 经办人ID
    private String opterId;

    // 生产批号
    private String manuLotnum;

    // 医药机构目录名称
    private String medinsListName;

    // 创建人姓名
    private String crterName;

    // 定点医药机构商品库存流水号
    private String medinsProdInvNo;

    // 创建人ID
    private String crterId;

    // 经办机构编号
    private String optinsNo;

    // 拆零标志
    private String trdnFlag;
}
