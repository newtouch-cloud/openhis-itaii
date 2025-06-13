package com.openhis.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Financial3201Output {

    //结算经办机构
    private String setlOptins;
    //对账结果
    private String stmtRslt;
    //对账结果说明
    private String stmtRsltDscr;

}
