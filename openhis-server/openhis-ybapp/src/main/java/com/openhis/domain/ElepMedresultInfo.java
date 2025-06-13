package com.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【电子处方取药结果查询-输出】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_medresult_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepMedresultInfo extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 医保处方编号 */
    private String hiRxno;

    /** 医保结算时间(yyyy-MM-dd HH:mm:ss) */
    private String setlTime;

    /** 医保处方状态编码(参考rx_stas_codg) */
    private String rxStasCodg;

    /** 医保处方状态名称 */
    private String rxStasName;

    /** 处方使用状态编号(参考rx_used_stas_codg) */
    private String rxUsedStasCodg;

    /** 处方使用状态名称 */
    private String rxUsedStasName;

}