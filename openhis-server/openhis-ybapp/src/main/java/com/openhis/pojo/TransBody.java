package com.openhis.pojo;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Builder;
import lombok.Data;

/**
 * 接口交易报文实体
 * @param <T>
 */
@Data
@Builder
public class TransBody<T> {

    /** 交易编号 */
    @JSONField(name="infno")
    private String infno;
    /** 发送方报文ID */
    @JSONField(name="msgid")
    private String msgid;
    /** 就医地医保区划 */
    @JSONField(name="mdtrtarea_admvs")
    private String mdtrtareaAdmvs;
    /** 参保地医保区划 */
    @JSONField(name="insuplc_admdvs")
    private String insuplcAdmdvs;
    /** 接收方系统代码 */
    @JSONField(name="recer_sys_code")
    private String recerSysCode;
    /** 设备编号 */
    @JSONField(name="dev_no")
    private String devNo;
    /** 设备安全信息 */
    @JSONField(name="dev_safe_info")
    private String devSafeInfo;
    /** 数字签名信息 */
    @JSONField(name="cainfo")
    private String cainfo;
    /** 签名类型 */
    @JSONField(name="signtype")
    private String signtype;
    /** 接口版本号 */
    @JSONField(name="infver")
    private String infver;
    /** 经办人类别 */
    @JSONField(name="opter_type")
    private String opterType;
    /** 经办人 */
    @JSONField(name="opter")
    private String opter;
    /** 经办人姓名 */
    @JSONField(name="opter_name")
    private String opterName;
    /** 交易时间 */
    @JSONField(name="inf_time")
    private String infTime;
    /** 定点医药机构编号 */
    @JSONField(name="fixmedins_code")
    private String fixmedinsCode;
    /** 定点医药机构名称 */
    @JSONField(name="fixmedins_name")
    private String fixmedinsName;
    /** 交易签到流水号 */
    @JSONField(name="sign_no")
    private String signNo;
    @JSONField(name="enc_type")
    private String encType;
    /** 交易输入 */
    @JSONField(name="input")
    private T input;

}
