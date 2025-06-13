package com.openhis.yb.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OutpatientRegistrationModel {
    //就诊凭证类型
    private String mdtrtCertType;
    //卡密
    private String busiCardInfo;
    //结算方式
    private String psnSetlWay;
    //总价
    private BigDecimal totalPrice;
}
