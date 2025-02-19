package com.core.flowable.domain.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author system
 * @date 2021/4/21 20:55
 */
@Data
public class FlowViewerDto implements Serializable {

    /**
     * 流程key
     */
    private String key;

    /**
     * 是否完成(已经审批)
     */
    private boolean completed;
}
