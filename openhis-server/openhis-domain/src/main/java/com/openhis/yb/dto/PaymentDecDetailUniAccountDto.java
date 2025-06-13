package com.openhis.yb.dto;

import com.openhis.financial.domain.PaymentRecDetail;
import lombok.Data;

/**
 * 付款详情关联账户的sql查询结果集
 */
@Data
public class PaymentDecDetailUniAccountDto extends PaymentRecDetail {
    /**
     * 合同编号
     */
    private String contractNo;

    //险种类型
    private String insutype;

    //医疗类别
    private String medType;
}
