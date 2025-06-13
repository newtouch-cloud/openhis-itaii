package com.openhis.web.basicservice.dto;

import com.openhis.common.enums.PublicationStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 费用定价管理表单数据
 *
 * @author system
 * @date 2025-02-20
 */
@Data
public class ChargeItemDefinitionFormData {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String chargeName;

    /**
     * 标题
     */
    private String title;

    /**
     * 状态
     */
    private Integer statusEnum;

    /**
     * 机构ID
     */
    @NotBlank(message = "机构ID不能为空")
    private Long orgId;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;

    /**
     * 代码
     */
    private String instanceTable;

    /**
     * 关联项目
     */
    private Long instanceId;

    /**
     * 有效时间开始
     */
    private Date effectiveStart;

    /**
     * 有效时间结束
     */
    private Date effectiveEnd;

    /**
     * 财务类别
     */
    @NotBlank(message = "财务类别不能为空")
    private String typeCode;

    /**
     * 医保类别
     */
    @NotBlank(message = "医保类别不能为空")
    private String ybType;

    /**
     * 是否使用详细价格规则
     */
    @NotBlank(message = "是否使用详细价格规则不能为空")
    private Integer conditionFlag;

    /**
     * 基础价格
     */
    @NotBlank(message = "基础价格不能为空")
    private BigDecimal price;
}
