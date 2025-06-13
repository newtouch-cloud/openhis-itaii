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
 * 【电子处方医保电子签名 -输入】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_signature_input")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepSignatureInput extends HisBaseEntity {

    /** 自增主键 */
    @JSONField(serialize=false)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 定点机构代码，定点机构唯一标识，用于识别机构对应的医保数字证书 */
    private String fixmedinsCode;

    /** 原始待签名处方信息，JSONString序列化后的base64字符值 */
    private String originalValue;

    /** 原始待签名处方文件，文件base64的字符值 */
    private String originalRxFile;

    /** 扩展字段，JSON序列化成字符串后长度不能超过4000 */
    private String extras;

    /** 医保处方编号 */
    @JSONField(serialize=false)
    private String hiRxno;

    /** 院内内部处方号 */
    private String prescriptionNo;

}