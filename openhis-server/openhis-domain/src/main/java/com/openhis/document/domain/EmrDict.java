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
 * 电子病历字典Entity实体
 *
 * @author system
 * @date 2025-02-22
 */
@Data
@TableName("doc_emr_dict")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EmrDict extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 病历内容code */
    private String emrKey;

    /** 病历内容value */
    private String emrValue;

}