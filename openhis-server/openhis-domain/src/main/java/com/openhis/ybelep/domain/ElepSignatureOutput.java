package com.openhis.ybelep.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【电子处方医保电子签名 -输出】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_signature_output")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepSignatureOutput extends HisBaseEntity {

    /** 自增主键 */
    @JSONField(serialize=false)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 处方文件，医保电子签名后处方文件originalRxFile的base64值 */
    private String rxFile;

    /** 签名摘要值，医保电子签名后处方信息originalValue的签名结果值 */
    private String signDigest;

    /** 签名机构证书SN */
    private String signCertSn;

    /** 签名机构证书DN */
    private String signCertDn;

    /** 医保处方编号 */
    @JSONField(serialize=false)
    private String hiRxno;

}