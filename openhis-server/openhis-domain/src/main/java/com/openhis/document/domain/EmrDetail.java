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
 * 电子病历详情Entity实体
 *
 * @author system
 * @date 2025-02-22
 */
@Data
@TableName("doc_emr_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EmrDetail extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 病历ID */
    private Long emrId;

    /** 病历内容key */
    private String emrKey;

    /** 病历内容value */
    private String emrValue;

}