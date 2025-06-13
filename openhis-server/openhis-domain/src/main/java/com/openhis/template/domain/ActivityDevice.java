package com.openhis.template.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("tmp_activity_device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ActivityDevice extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 范围 */
    private String rangeCode;

    /** 类型 */
    private String typeCode;

    /** 诊疗/用法/号源的编码(id) */
    private String itemNo;

    /** 耗材/诊疗id */
    private Long devActId;

    /**
     * 耗材/诊疗表名
     */
    private String devActTable;

    /** 耗材数量 */
    private Integer quantity;

    /** 状态 */
    private Integer statusEnum;

    public ActivityDevice() {
        this.statusEnum = PublicationStatus.ACTIVE.getValue();
    }
}
