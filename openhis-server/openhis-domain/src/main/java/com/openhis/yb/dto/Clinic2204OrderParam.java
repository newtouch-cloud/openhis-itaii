package com.openhis.yb.dto;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import com.core.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2204】门诊费用明细信息上传
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Clinic2204OrderParam {
    // 就医地医保区划
    @JSONField(serialize = false)
    private String mdtrtareaAdmvs;
    // 参保地医保区划
    @JSONField(name = "insuplc_admdvs")
    private String insuplcAdmdvs;
    @JSONField(serialize = false)
    private String chrgBchno;
    @JSONField(name = "feedetail")
    private List<Clinic2204FeeDetailParam> feedetail;
}
