package com.openhis.pojo;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Data;

/**
 * 医保结果
 */
@Data
public class ResultBody {

    /** 交易状态码 */
    @JSONField(name="infcode")
    private String infcode;
    /** 发送方报文ID */
    @JSONField(name="warnMsg")
    private String warn_msg;
    /** 数字签名信息 */
    @JSONField(name="cainfo")
    private String cainfo;
    /** 错误信息 */
    @JSONField(name="err_msg")
    private String errMsg;
    /** 接收报文时间 */
    @JSONField(name="refmsg_time")
    private String refmsgTime;
    /** 签名类型 */
    @JSONField(name="signtype")
    private String signtype;
    /** 响应报文时间 */
    @JSONField(name="respond_time")
    private String respondTime;
    /** 接收方报文ID */
    @JSONField(name="inf_refmsgid")
    private String infRefmsgid;
    /** 交易输出 */
    @JSONField(name="output")
    private String output;
}
