package com.openhis.workflow.domain;

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
 * 服务申请详情管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_service_request_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ServiceRequestDetail extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 申请主表id */
    private Long serviceReqId;

    /** 类型编码 */
    private String typeCode;

    /** 订单相关表 */
    private String orderDetailTable;

    /** 订单相关id */
    private Long orderDetailId;

    /** 订单详情 */
    private String detailJson;


}