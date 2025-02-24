package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.DeviceCategory;
import com.openhis.common.enums.DeviceSafety;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 器材基本信息管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Device extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 器材编码 */
    private String busNo;

    /** 器材定义编号 */
    private Long deviceDefId;

    /** 器材状态 */
    private Integer statusEnum;

    /** 器材可用状态 */
    private Integer availabilityEnum;

    /** 厂家 */
    private String manufacturer;

    /** 生产日期 */
    private Date manufactureDate;

    /** 到期日期 */
    private Date expirationDate;

    /** 批号 */
    private String lotNumber;

    /** 厂家序列号 */
    private String serialNumber;

    /** 器材名称 */
    private String name;

    /** 器材别名 */
    private String alias;

    /** 制造商编号 */
    private String modelNumber;

    /** 零件编号 */
    private String partNumber;

    /** 器材种类 */
    private DeviceCategory categoryEnum;

    /** 器材类型 */
    private String typeCode;

    /** 器材版本 */
    private String version;

    /** 器材规格 */
    private String deviceSpecifications;

    /** 归属科室 */
    private Long orgId;

    /** 所在位置 */
    private Long locationId;

    /** 负责人/科室 */
    private String support;

    /** 器材安全 */
    private DeviceSafety safetyEnum;


}
