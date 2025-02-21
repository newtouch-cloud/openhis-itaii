package com.openhis.administration.domain;

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
 * 机构管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_organization")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Organization extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构类型枚举 */
    private Integer typeEnum;

    /** 机构分类枚举 */
    private Integer classEnum;

    /** 拼音码 */
    private String spellCode;

    /** 五笔码 */
    private String wbCode;

    /** 医保码 */
    private String ybCode;

    /** 医保名称 */
    private String ybName;

    /** 显示顺序 */
    private Integer displayOrder;


}