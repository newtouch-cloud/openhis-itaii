package com.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【电子处方撤销-输出】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_revoke_output")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepRevokeOutput extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 医保处方编号 */
    private String hiRxno;

    /** 医保处方状态编码 */
    private String rxStasCodg;

    /** 医保处方状态名称 */
    private String rxStasName;

}