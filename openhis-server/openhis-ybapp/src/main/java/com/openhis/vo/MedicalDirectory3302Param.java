package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicalDirectory3302Param {
    // 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 定点医药机构目录编号
    @JSONField(name = "fixmedins_hilist_id")
    private String fixmedinsHilistId;

    // 目录类别
    @JSONField(name = "list_type")
    private String listType;

    // 医疗目录编码
    @JSONField(name = "med_list_codg")
    private String medListCodg;
}
