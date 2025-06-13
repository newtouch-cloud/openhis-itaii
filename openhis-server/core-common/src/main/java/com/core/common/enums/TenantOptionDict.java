package com.core.common.enums;

/**
 * 租户配置项字典（不存在DB中，以此文件为基准，新增修改只需在这里改）
 * 
 * @author system
 */
public enum TenantOptionDict {
    /**
     * 医院名称
     */
    HOSPITAL_NAME("hospitalName", "医院名称"),
    /**
     * 定点医药机构编号
     */
    FIXMEDINS_CODE("fixmedinsCode", "定点医药机构编号"),
    /**
     * 电子发票appid
     */
    APP_ID("app_id", "电子发票appid"),
    /**
     * 电子发票key
     */
    KEY("key", "电子发票key"),
    /**
     * 电子发票url
     */
    URL("url", "电子发票url"),
    /**
     * 医保开关
     */
    YB_SWITCH("yb_switch", "医保开关"),
    /**
     * 客户端私钥
     */
    CLI_PRV_KEY("cliPrvKey", "客户端私钥"),
    /**
     * 客户端公钥
     */
    CLI_PUB_KEY("cliPubKey", "客户端公钥"),
    /**
     * 服务端公钥
     */
    SERVER_PUB_KEY("serverPubKey", "服务端公钥"),
    /**
     * 定点医药机构名称
     */
    FIXMEDINS_NAME("fixmedinsName", "定点医药机构名称"),
    /**
     * 行政区划
     */
    ADMVS("admvs", "行政区划"),
    /**
     * 授权范围
     */
    SCOPE("scope", "授权范围"),
    /**
     * 授权类型
     */
    GRANT_TYPE("grantType", "授权类型"),
    /**
     * 密码
     */
    PASSWORD("password", "密码"),
    /**
     * 用户名
     */
    USERNAME("username", "用户名"),
    /**
     * 客户端安全码
     */
    CLIENT_SECRET("clientSecret", "客户端安全码"),
    /**
     * 客户端ID
     */
    CLIENT_ID("clientId", "客户端ID"),
    /**
     * 生产环境客户端公钥
     */
    PROD_CLI_PUB_KEY("prod_cliPubKey", "生产环境客户端公钥"),
    /**
     * 生产环境客户端私钥
     */
    PROD_CLI_PRV_KEY("prod_cliPrvKey", "生产环境客户端私钥"),
    /**
     * 生产环境客户端ID
     */
    PROD_CLIENT_ID("prod_clientId", "生产环境客户端ID"),
    /**
     * 文件路径
     */
    FILE_PATH("filePath", "文件路径"),
    /**
     * 电子地址
     */
    ELE_ADDRESS("eleAddress", "电子地址"),
    /**
     * 服务地址
     */
    ADDRESS("address", "服务地址"),
    /**
     * 超时时间
     */
    TIME("time", "超时时间"),
    /**
     * 是否加密
     */
    IS_ENCRYPT("isEncrypt", "是否加密"),
    /**
     * 医保区划
     */
    INSUPLC_ADMDVS("insuplc_admdvs", "医保区划"),
    /**
     * 电子处方appId
     */
    PRE_APP_ID("pre_app_id", "电子处方appId"),
    /**
     * 电子处方appSecret
     */
    PRE_APP_SECRET("pre_app_secret", "电子处方appSecret"),
    /**
     * 电子处方私钥
     */
    APP_PRVKEY("APP_PRVKEY", "电子处方私钥"),
    /**
     * 电子处方公钥
     */
    PLAF_PUBKEY("PLAF_PUBKEY", "电子处方公钥"),
    /**
     * 医保客户端ID
     */
    YB_CLIENT_ID("ybClientId", "医保客户端ID"),
    /**
     * 医保客户端安全码
     */
    YB_CLIENT_SECRET("ybClientSecret", "医保客户端安全码"),
    /**
     * 医保用户名
     */
    YB_USERNAME("ybUsername", "医保用户名"),
    /**
     * 医保密码
     */
    YB_PASSWORD("ybPassword", "医保密码"),
    /**
     * 医保授权类型
     */
    YB_GRANT_TYPE("ybGrantType", "医保授权类型"),
    /**
     * 医保授权范围
     */
    YB_SCOPE("ybScope", "医保授权范围"),
    /**
     * 医保密钥
     */
    YB_CLI_PRV_KEY("ybCliPrvKey", "医保密钥"),
    /**
     * 医保服务URL
     */
    YB_URL("ybUrl", "医保服务URL"),
    /**
     * 医院等级
     */
    HOSPITAL_LV("hospital_lv", "医院等级");

    private final String code;
    private final String name;

    TenantOptionDict(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TenantOptionDict getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (TenantOptionDict val : values()) {
            if (val.getCode().equals(code)) {
                return val;
            }
        }
        return null;
    }
}
