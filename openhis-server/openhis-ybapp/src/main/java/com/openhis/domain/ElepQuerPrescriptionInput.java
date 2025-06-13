package com.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【电子处方信息查询-输入】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_quer_prescription_input")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepQuerPrescriptionInput extends HisBaseEntity {

    /** 定点医疗机构编号 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 定点医疗机构编号 */
    private String fixmedinsCode;

    /** 医保处方编号 */
    private String hiRxno;

    /** 医保就诊 ID */
    private String mdtrtId;

    /** 人员名称 */
    private String psnName;

    /** 人员证件类型 */
    private String psnCertType;

    /** 证件号码 */
    private String certno;

}