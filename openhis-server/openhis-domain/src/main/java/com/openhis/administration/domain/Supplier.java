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
 * 供应商管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_supplier")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Supplier extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 编号 */
    private String busNo;

    /** 名称 */
    private String name;

    /** 类型 */
    private Integer typeEnum;

    /** 地址 */
    private String address;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 联系人电话 */
    private String phone;

    /** 联系人邮箱 */
    private String email;

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构编号 */
    private Long orgId;


}