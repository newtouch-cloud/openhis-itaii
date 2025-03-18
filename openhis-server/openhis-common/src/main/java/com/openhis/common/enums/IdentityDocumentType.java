package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 身份证件类型
 *
 * @author liuhr
 * @date 2025/3/18
 */

@Getter
@AllArgsConstructor
public enum IdentityDocumentType implements HisEnumInterface {

    RESIDENT_ID_CARD(1, "residentIdCard", "居民身份证"),
    HOUSEHOLD_REGISTER(2, "householdRegister", "居民户口簿"),
    PASSPORT(3, "passport", "护照"),
    OFFICER_CERTIFICATE(4, "officerCertificate", "军官证"),
    DRIVING_LICENSE(5, "drivingLicense", "驾驶证"),
    HONGKONG_MACAO_PASS(6, "hongkongMacaoPass", "港澳居民来往内地通行证"),
    TAIWAN_PASS(7, "taiwanPass", "台湾居民来往内地通行证"),
    OTHER(99, "other", "其他身份证件");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
