package com.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3301、3302】医保目录对照历史表
 *
 * @author SunJQ
 * @date 2025-04-27
 */
@Data
@TableName("yb_directory_check_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DirectoryCheckRecord extends HisBaseEntity {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 3301/3302 */
    private Integer type;

    /** 表名 */
    private String tableName;

    /** 业务表主键 */
    private Long tableId;

    /** 组织机构id */
    private Long orgId;

    /** 参数 */
    private String param;

}