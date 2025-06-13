package com.openhis.yb.dto;

import java.util.List;
import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3103】事前事中服务反馈服务（输入）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiFeedback3103DataParam {
    // 1. 反馈类型
    @JSONField(name = "warn_type")
    private String warnType;

    // 2. 处理数据集合
    @JSONField(name = "warns")
    private List<FsiFeedback3103WarnsParam> warns;
}
