package com.openhis.ybelep.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【电子处方上传预核验-输出】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_veri_prescription_output")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepVeriPrescriptionOutput extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 处方追溯码 */
    private String rxTraceCode;

    /** 医保处方编号 */
    private String hiRxno;

    /** 药品请求的处方号 */
    private String prescriptionNo;
}