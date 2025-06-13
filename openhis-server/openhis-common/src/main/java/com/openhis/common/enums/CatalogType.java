/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CatalogType (0: MEMBER_FIRST; 1: MEMBER_SECOND;)
 *
 * @author SunJQ
 * @date 2025-04-09
 */
@Getter
@AllArgsConstructor
public enum CatalogType {

    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1301(1301, "西药中成药目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1302(1302, "中药饮片目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1303(1303, "医疗机构制剂目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1304(1304, "民族药品目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1305(1305, "医疗服务项目目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1306(1306, "医用耗材目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1307(1307, "疾病与诊断目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1308(1308, "手术操作目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1309(1309, "门诊慢特病种目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1310(1310, "按病种付费病种目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1311(1311, "日间手术治疗病种"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1312(1312, "医保目录信息查询"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1313(1313, "肿瘤形态学目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1314(1314, "中医疾病目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1315(1315, "中医证候目录"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1316(1316, "医疗目录与医保目录匹配信息"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1317(1317, "医药机构目录匹配信息"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1318(1318, "医保目录限价信息"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1319(1319, "医保目录先自付比例信息"),
    /**
     * MEMBER_FIRST
     */
    CATALOG_TYPE_1320(1320, "中药配方颗粒目录"),
    /**
     * MEMBER_SECOND
     */
    CATALOG_TYPE_1321(1321, "医疗服务项目（新）目录");

    private Integer value;
    private String description;

    public static CatalogType getByValue(Integer value) {
        if (value==null) {
            return null;
        }
        for (CatalogType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
