/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.core.common.utils.http;

/**
 * 老系统http请求工具类
 *
 * @author SunJQ
 * @date 2024-12-30
 */
public class OldHisHttpUtils {
    public static String oldHisHttpPost() {

        String s1 = HttpUtils.sendPost("http://localhost:8080/myareahisnyb/gfChargeRatio/gfsum", "");

        System.out.println(s1);
        return s1;
    }

}
