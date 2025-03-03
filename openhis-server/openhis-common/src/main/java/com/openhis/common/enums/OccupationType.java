package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OccupationType implements HisEnumInterface {
    STATE_CIVIL_SERVANT(11, "11", "国家公务员"),
    PROFESSIONAL(13, "13", "专业技术人员"),
    CLERK(17, "17", "职员"),
    BUSINESS_MANAGER(21, "21", "企业管理人员"),
    WORKER(24, "24", "工人"),
    FARMER(27, "27", "农民"),
    STUDENT(31, "31", "学生"),
    ACTIVE_MILITARY(37, "37", "现役军人"),
    FREELANCER(51, "51", "自由职业者"),
    SELF_EMPLOYED(54, "54", "个体经营者"),
    UNEMPLOYED(70, "70", "无业人员"),
    RETIREE(80, "80", "退（离）休人员"),
    OTHER(90, "90", "其他");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
