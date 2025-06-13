/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 【9101】【9102】公用
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class File {

    // 文件查询号
    private String fileQuryNo;

    // 文件名称
    private String filename;

    // 下载截止时间
    private Date dldEndtime;

    // 文件数据
    private byte[] fileData;

    // 医药机构编号
    private String fixmedinsCode;


}
