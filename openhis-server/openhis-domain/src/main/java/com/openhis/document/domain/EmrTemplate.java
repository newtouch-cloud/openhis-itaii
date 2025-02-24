package com.openhis.document.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 病历模板Entity实体
 *
 * @author system
 * @date 2025-02-21
 */
@Data
@TableName("doc_emr_template")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EmrTemplate extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 模板名称 */
    private String templateName;

    /** 模板类型 */
    private String templateCode;

    /** 使用范围 */
    private String useScopeCode;

    /** 个人/科室ID */
    private Long userId;

    /** 病历内容 */
    private String contextJson;

}