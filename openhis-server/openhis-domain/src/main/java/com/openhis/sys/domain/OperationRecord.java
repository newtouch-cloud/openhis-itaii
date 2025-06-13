package com.openhis.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统操作记录Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("sys_operation_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OperationRecord extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 操作类型|1新增,2修改,3删除 */
    private String dbOpType;

    /** 表名 */
    private String tableName;

    /** 参数json */
    private String paramJson;

}
