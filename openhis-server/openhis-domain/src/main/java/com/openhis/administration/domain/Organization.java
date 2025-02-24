package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.OrganizationClass;
import com.openhis.common.enums.OrganizationType;
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
    private String bus_no;

    /** 名称 */
    private String name;

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构类型枚举 */
    private OrganizationType typeEnum;

    /** 机构分类枚举 */
    private OrganizationClass classEnum;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 医保码 */
    private String ybNo;

    /** 医保名称 */
    private String ybName;

    /** 显示顺序 */
    private Integer displayOrder;


}
