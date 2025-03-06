package com.openhis.web.outpatientservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.*;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 就诊 表单数据
 */
@Data
@Accessors(chain = true)
public class EncounterFormData {

    /**
     * 患者ID
     */
    @NotBlank(message = "患者ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /**
     * 状态编码
     */
    private Integer statusEnum;

    /**
     * 类别编码
     */
    private Integer classEnum;

    /**
     * 类别医保编码
     */
    private Integer ybClassEnum;

    /**
     * 优先级编码
     */
    @NotBlank(message = "优先级编码不能为空")
    private Integer priorityEnum;

    /**
     * 分类编码
     */
    private Integer typeEnum;

    /**
     * 服务ID
     */
    @NotBlank(message = "服务ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serviceTypeId;

    /**
     * 就诊对象状态
     */
    private Integer subjectStatusEnum;

    /**
     * 机构ID
     */
    @NotBlank(message = "机构ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organizationId;

    /**
     * 设置默认值
     */
    public EncounterFormData() {
        this.statusEnum = EncounterStatus.PLANNED.getValue();
        this.classEnum = EncounterClass.AMB.getValue();
        this.ybClassEnum = EncounterYbClass.ORDINARY_OUTPATIENT.getValue();
        this.typeEnum = OutpatientClass.GENERAL_OUTPATIENT_SERVICE.getValue();
        this.subjectStatusEnum = EncounterSubjectStatus.TRIAGED.getValue();
    }

}
