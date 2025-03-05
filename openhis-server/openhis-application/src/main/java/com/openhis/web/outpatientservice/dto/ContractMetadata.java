package com.openhis.web.outpatientservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 费用性质 元数据
 */
@Data
@Accessors(chain = true)
public class ContractMetadata {
    /** 合同名称 */
    private String contractName;
    /** 合同编码 */
    private String busNo;
}
