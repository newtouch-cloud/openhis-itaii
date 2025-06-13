package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
