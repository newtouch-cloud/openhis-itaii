/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【9101】【9102】公用
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FileResult {

    // 文件查询号
    @JSONField(name="file_qury_no")
    private String fileQuryNo;

    // 文件名称
    @JSONField(name="filename")
    private String filename;

    // 下载截止时间
    @JSONField(name="dld_endtime")
    private Date dldEndtime;

    // 文件数据
    @JSONField(serialize=false)
    private byte[] fileData;

    // 医药机构编号
    @JSONField(name="fixmedins_code")
    private String fixmedinsCode;
}
