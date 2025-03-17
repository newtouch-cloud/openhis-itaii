package com.openhis.web.swaggerexample.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * POST请求参数类
 */
@Data
@ApiModel(value = "POST请求参数类")
public class PostExampleParam {
    // TODO:这只是个例子，后期请删除

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private Integer code;
    /**
     * 总额
     */
    @ApiModelProperty(value = "总额")
    private BigDecimal amount;
}
