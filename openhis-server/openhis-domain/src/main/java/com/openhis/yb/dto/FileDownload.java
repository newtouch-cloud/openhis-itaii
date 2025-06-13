/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【9102】下载实体
 */
@Data
@Accessors(chain = true)
public class FileDownload {
    @JSONField(serialize = false)
    private String code;
    @JSONField(name = "ver")
    private String version;
    @JSONField(name = "filename")
    private String filename;
    @JSONField(name = "file_qury_no")
    private String fileQuryNo;
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;
}
