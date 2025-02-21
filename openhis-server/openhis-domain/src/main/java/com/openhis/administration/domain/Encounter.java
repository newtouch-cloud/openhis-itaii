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
 * 就诊管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_encounter")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Encounter extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 患者ID */
    private Long patientId;

    /** 群组ID */
    private Long groupId;

    /** 就诊编码 */
    private String code;

    /** 状态编码 */
    private String statusCode;

    /** 类别编码 */
    private Integer classEnum;

    /** 类别医保编码 */
    private Integer ybClassEnum;

    /** 类别编码补充 */
    private String classJson;

    /** 优先级编码 */
    private Integer priorityEnum;

    /** 分类编码 */
    private Integer typeEnum;

    /** 服务ID */
    private Long serviceTypeId;

    /** 就诊对象状态 */
    private Integer subjectStatusEnum;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 机构id */
    private Long organizationId;

    /** 就诊序号 */
    private Integer displayOrder;

    /** 删除状态 */
    private Integer deleteFlag;

}