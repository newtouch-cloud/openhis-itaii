package com.openhis.web.nursestation.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 待入科Dto
 *
 * @author liuhr
 * @date 2025/4/14
 */
@Data
@Accessors(chain = true)
public class PendingAdmissionDto {

    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 患者ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /**
     * 群组ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /**
     * 就诊编码
     */
    private String busNo;

    /**
     * 状态编码
     */
    private Integer statusEnum;

    /**
     * 类别编码
     */
    private Integer classEnum;

    /**
     * 类别医保编码|费别
     */
    private Integer ybClassEnum;
    private String ybClassEnum_enumText;

    /**
     * 优先级编码
     */
    private Integer priorityEnum;

    /**
     * 就诊对象状态
     */
    private Integer subjectStatusEnum;

    /**
     * 开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 机构id|入院科室
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private Long organizationId;
    private String organizationId_dictText;

    /** 入院病区ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "病区ID不能为空")
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    private Long wardLocationId;
    private String wardLocationId_dictText;

    /**
     * 入院方式|入科来源
     */
    @Dict(dictCode = "in_way_code")
    private String inWayCode;
    private String inWayCode_dictText;

    /** 患者姓名 */
    private String name;

    /** 性别编码 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 生日 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;

    /** 身份证号 */
    private String idCard;

    /** 病人年龄 */
    private String ageString;

    /** 是否跨科收治 */
    private Integer crossDeptStatus;
    private String crossDeptStatus_enumText;

    /** 门诊诊断 */
    private String descriptions;

}
