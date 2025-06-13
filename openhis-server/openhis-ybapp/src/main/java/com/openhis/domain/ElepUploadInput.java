package com.openhis.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【电子处方上传-输入】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_upload_input")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepUploadInput extends HisBaseEntity {

    /** 自增主键 */
    @JSONField(serialize=false)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 处方追溯码 */
    private String rxTraceCode;

    /** 医保处方编号 */
    private String hiRxno;

    /** 医院就诊 ID */
    private String mdtrtId;

    /** 患者姓名 */
    private String patnName;

    /** 人员证件类型 */
    private String psnCertType;

    /** 证件号码 */
    private String certno;

    /** 定点医疗机构名称 */
    private String fixmedinsName;

    /** 定点医疗机构编号 */
    private String fixmedinsCode;

    /** 开方医保医师代码 */
    private String drCode;

    /** 开方医师姓名 */
    private String prscDrName;

    /** 审方药师科室名称 */
    private String pharDeptName;

    /** 审方药师科室编号 */
    private String pharDeptCode;

    /** 审方药师职称编码 */
    private String pharProfttlCodg;

    /** 审方药师职称名称 */
    private String pharProfttlName;

    /** 审方医保药师代码 */
    private String pharCode;

    /** 审方药师证件类型 */
    private String pharCertType;

    /** 审方药师证件号码 */
    private String pharCertno;

    /** 审方药师姓名 */
    private String pharName;

    /** 审方药师执业资格证号 */
    private String pharPracCertNo;

    /** 医疗机构药师审方时间，格式：yyyy-MM-dd HH:mm:ss */
    private String pharChkTime;

    /** 处方原件，医保电子签名后的处方文件base64字符（PDF或OFD格式） */
    private String rxFile;

    /** 处方信息签名值 */
    private String signDigest;

    /** 扩展字段，JSON格式，长度不超过4000 */
    private String extras;

}