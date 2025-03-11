package com.openhis.web.doctorstation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保存诊断 子参数类
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class SaveDiagnosisChildParam {

    /**
     * 诊断定义id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /**
     * 医保编码
     */
    private String ybNo;

    /**
     * 验证状态
     */
    private Integer verificationStatusEnum;

    /**
     * 主诊断标记 (1:是,0:否)
     */
    private Integer maindiseFlag;

    /**
     * 诊断ID - 用于存储 adm_encounter_diagnosis表
     */
    private Long conditionId;

}
