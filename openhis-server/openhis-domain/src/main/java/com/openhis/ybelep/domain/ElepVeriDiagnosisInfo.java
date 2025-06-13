package com.openhis.ybelep.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 【电子处方上传预核验-输入-诊断信息（节点表示：diseinfo)】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_veri_diagnosis_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepVeriDiagnosisInfo extends HisBaseEntity {

    /** 自增主键 */
    @JSONField(serialize=false)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 诊断类别，参考诊断类别(diag_type) */
    private String diagType;

    /** 主诊断标志，0-否、1-是 */
    private String maindiagFlag;

    /** 诊断排序号 */
    private String diagSrtNo;

    /** 诊断代码，医保疾病诊断代码 */
    private String diagCode;

    /** 诊断名称 */
    private String diagName;

    /** 诊断科室名称 */
    private String diagDept;

    /** 诊断科室代码，与科室信息上传中的hosp_dept_codg医院科室编码保持一致 */
    private String diagDeptCode;

    /** 诊断医生编码，国家医保医师代码 */
    private String diagDrNo;

    /** 诊断医生姓名 */
    private String diagDrName;

    /** 诊断时间，格式：yyyy-MM-dd HH:mm:ss */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date diagTime;

    /** 中医病名代码，diag_type为中医诊断(值为2,3)时上传 */
    private String tcmDiseCode;

    /** 中医病名，diag_type为中医诊断(值为2,3)时上传 */
    private String tcmDiseName;

    /** 中医证候代码，diag_type为中医诊断(值为2,3)时上传 */
    private String tcmsympCode;

    /** 中医证候，diag_type为中医诊断(值为2,3)时上传 */
    private String tcmsymp;

    /** 院内内部处方号 */
    @JSONField(serialize=false)
    private String prescriptionNo;

    /** 医保处方编号(电子处方信息查询返回时插入记录) */
    @JSONField(serialize=false)
    private String hiRxno;
}