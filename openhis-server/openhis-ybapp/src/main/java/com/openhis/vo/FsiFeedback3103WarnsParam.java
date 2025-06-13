package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3103】事前事中服务反馈服务（输入-反馈处理数据）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiFeedback3103WarnsParam {
    // 1. 违规标识
    @JSONField(name = "warn_rslt_id")
    private String warnRsltId;

    // 2. 处理方式
    @JSONField(name = "dspo_way")
    private String dspoWay;

    // 3. 处理原因
    @JSONField(name = "dspo_way_rea")
    private String dspoWayRea;
}
