package com.openhis.clinical.domain;

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
 * 过敏与不耐受Entity实体
 *
 * @author system
 * @date 2025-03-07
 */
@Data
@TableName("cli_allergy_intolerance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AllergyIntolerance extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 服务申请ID */
    private Long requestId;

    /** 临床状况 */
    private Integer clinicalStatusEnum;

    /** 验证状态 */
    private Integer verificationStatusEnum;

    /** 类型 */
    private Integer typeEnum;

    /** 过敏原类别 */
    private String categoryCode;

    /** 危险程度 */
    private Integer criticalityEnum;

    /** 过敏物质编码 */
    private String code;

    /** 患者ID */
    private Long patientId;

    /** 反应描述 */
    private String description;

    /** 严重程度 */
    private Integer severityEnum;

    /** 过敏发生开始日期 */
    private Date onsetDateTime;

    /** 记录者 */
    private Long practitionerId;

    /** 断言者 */
    private Long checkPractitionerId;

    /** 记录日期 */
    private Date recordedDate;

    /** 最后反应发生日期 */
    private Date lastReactionOccurrence ;

    /** 曝光路线 */
    private String exposureRoute;

    /** 备注 */
    private String note;

    /** 删除状态 */
    private String deleteFlag;

}