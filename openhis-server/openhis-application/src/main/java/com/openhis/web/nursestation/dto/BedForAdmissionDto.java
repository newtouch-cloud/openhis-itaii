package com.openhis.web.nursestation.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 选床入科Dto
 *
 * @author liuhr
 * @date 2025/4/14
 */
@Data
@Accessors(chain = true)
public class BedForAdmissionDto {

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 患者ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 科室ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "科室ID不能为空")
    private Long organizationId;

    /** 病区ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "病区ID不能为空")
    private Long wardLocationId;

    /** 病床ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "病床ID不能为空")
    private Long bedLocationId;

    /** 患者病情 */
    private Integer priorityEnum;

    /** 住院医生 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long attendingDoctorId;

    /** 主任医生 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chiefDoctorId;

    /** 主治医生 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long principalDoctorId;

    /** 责任护士 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long attenderId;

    /** 入科时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

}
