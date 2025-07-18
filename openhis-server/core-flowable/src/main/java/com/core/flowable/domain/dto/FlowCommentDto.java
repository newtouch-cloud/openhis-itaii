package com.core.flowable.domain.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @author system
 * @date 2021/3/28 15:50
 */
@Data
@Builder
public class FlowCommentDto implements Serializable {

    /**
     * 意见类别 0 正常意见 1 退回意见 2 驳回意见
     */
    private String type;

    /**
     * 意见内容
     */
    private String comment;
}
