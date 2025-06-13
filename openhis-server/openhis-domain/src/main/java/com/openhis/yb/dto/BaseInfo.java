/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sun.jna.platform.win32.Winspool;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医保所需要的基础信息，如医生信息或者医院信息，目的是为医保服务提供数据信息
 *
 * @author SunJQ
 * @date 2025-05-04
 */
@Data
@Accessors(chain = true)
public class BaseInfo {
    /******************医保机构信息******************/


    /********************医院信息********************/

    /** (必填)统筹区号 */
    private String admvs;//

    /** (必填)定点医药机构编号 */
    private String fixmedinsCode;

    /** (必填)定点医药机构名称 */
    private String fixmedinsName;

    /** (必填)医保app的Controller路径 */
    private String ybUrl;

    /** (必填)客戶端id */
    private String ybClientId;//医保用 2025/05/21 无人解答医保与电子处方的这些信息是否一致，防止修改某一变量导致另一个功能失效，分开保存

    /** (必填)客戶端安全码 */
    private String ybClientSecret;//医保用

    /** (必填)医保服务平台账号 */
    private String ybUsername;//医保用

    /** (必填)医保服务平台密码 */
    private String ybPassword;//医保用

    /** (必填)终端授权类型 */
    private String ybGrantType;//医保用

    /** (必填)终端授权范围 */
    private String ybScope;//医保用

    /** (必填)秘钥 */
    private String ybCliPrvKey;//医保用

    /********************医护信息********************/

    /** (必填) 用户id  */
    private Long UserId;

    /** (必填)登录人名字 */
    private String realname;

    /********************患者信息********************/

    /** 参保地医保区划 */
    private String insuplcAdmdvs;//如果交易输入中含有人员编号，此项必填，可通过【1101】人员信息获取交易取得 2025/05/20 经测试发现2201-2208几个接口没传psn_no但是也要传这个

    /********************电子处方********************/
    //电子处方
    private String cliPrvKey;
    private String cliPubKey;
    private String clientId;
    private String eleAddress;
    private String username;
    private String password;
    private String scope;
    private String grantType;
    private String clientSecret;
    private String time;
    private String preAppId;
    private String preAppSecret;
    private String prePrvKey;
    private String prePubKey;
    private String templatePath;
    private String outputPath;
    private String hospitalSealPath;
    /********************共通参数********************/
    //是否解密
    private String decryptFlag;
}
