package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.LocationBedStatus;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.enums.LocationMode;
import com.openhis.common.enums.LocationStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 位置管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_location")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Location extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 编码 */
    private String busNo;

    /** 名称 */
    private String name;

    /** 状态编码 */
    private LocationStatus statusEnum;

    /** 操作状态 */
    private LocationBedStatus operationalEnum;

    /** 模式编码 */
    private LocationMode modeEnum;

    /** 模式编码 */
    private String typeCode;

    /** 功能编码 */
    private String typeJson;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 物理形式枚举 */
    private LocationForm formEnum;

    /** 机构编码 */
    private Long organizationId;

    /** 显示顺序 */
    private Integer displayOrder;


}
