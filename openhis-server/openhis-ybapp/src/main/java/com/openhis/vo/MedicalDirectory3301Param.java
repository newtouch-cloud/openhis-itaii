/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【3301】目录对照上传
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicalDirectory3301Param {

    // 定点医药机构目录编号
    @JSONField(name = "fixmedins_hilist_id")
    private String fixmedinsHilistId;

    // 定点医药机构目录名称
    @JSONField(name = "fixmedins_hilist_name")
    private String fixmedinsHilistName;

    // 目录类别
    @JSONField(name = "list_type")
    private String listType;

    // 医疗目录编码
    @JSONField(name = "med_list_codg")
    private String medListCodg;

    // 开始日期
    @JSONField(name = "begndate")
    private String begndate;

    // 结束日期
    @JSONField(name = "enddate")
    private String enddate;

    // 批准文号
    private String aprvno;

    // 剂型
    private String dosform;

    // 除外内容
    private String exctCont;

    // 项目内涵
    private String itemCont;

    // 计价单位
    private String prcunt;

    // 规格
    private String spec;

    // 包装规格
    private String pacspec;

    // 备注
    private String memo;
}
