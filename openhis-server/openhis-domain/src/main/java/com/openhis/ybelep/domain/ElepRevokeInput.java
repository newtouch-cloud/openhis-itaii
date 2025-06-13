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
 * 【电子处方撤销-输入】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_revoke_input")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepRevokeInput extends HisBaseEntity {

    /** 医保处方编号 */
    @JSONField(serialize=false)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 医保处方编号 */
    private String hiRxno;

    /** 定点医疗机构编号 */
    private String fixmedinsCode;

    /** 撤销医师的医保医师代码 */
    private String drCode;

    /** 撤销医师姓名 */
    private String undoDrName;

    /** 撤销医师证件类型 */
    private String undoDrCertType;

    /** 撤销医师证件号码 */
    private String undoDrCertno;

    /** 撤销原因描述 */
    private String undoRea;

    /** 撤销时间，格式：yyyy-MM-dd HH:mm:ss */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date undoTime;
}