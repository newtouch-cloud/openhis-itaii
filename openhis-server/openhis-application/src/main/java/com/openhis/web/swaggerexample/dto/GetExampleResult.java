package com.openhis.web.swaggerexample.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * GET请求结果类
 */
@Data
@ApiModel(value = "GET请求结果类")
public class GetExampleResult {
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
