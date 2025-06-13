package com.openhis.web.ybmanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.yb.dto.Info5301SpecialConditionResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class PatientInfoDto {

    //患者信息
    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 活动标记 */
    private Integer activeFlag;

    /** 临时标识 */
    private Integer tempFlag;

    /** 患者姓名 */
    private String name;

    /** 患者院内编码/病历号 */
    private String busNo;

    /** 性别编码 */
    private Integer genderEnum;

    /** 生日 */
    private Date birthDate;

    /** 民族 */
    private String nationalityCode;

    /** 身份证号 */
    private String idCard;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 机构Id */
    private Long organizationId;

    /** 特慢病等 */
    private List<Info5301SpecialConditionResult> feedetail;

    //医保信息 2025/05/23 弃用，费用性质固定，如农大只有省医保，费用性质只显示省医保和自费即可，由@GetMapping(value = "/contract-list")接口提供数据
    /** 合同Id */
    private Long contractId;//弃用 2025/05/23

    /** 合同名称 */
    private String contractName;//弃用 2025/05/23

    /** 状态 */
    private Integer statusEnum;//弃用 2025/05/23

    /** 合同的类别 */
    private Integer categoryEnum;//弃用 2025/05/23

    /** 合同编码 */
    private String contractBusNo;//弃用 2025/05/23

    /** 机构 */
    private Long contractOrgId;//弃用 2025/05/23

    /** 是否医保 */
    private Integer ybFlag;//弃用 2025/05/23

    /** 医保区划 */
    private String admVs;//弃用 2025/05/23

}
