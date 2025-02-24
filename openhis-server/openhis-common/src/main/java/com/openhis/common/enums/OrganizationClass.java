package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrganizationClass {
    CLINIC(1, "clinic", "门诊"),
    INPATIENT(2, "inpatient", "住院"),
    PHARMACY(3, "pharmacy", "药房"),
    STORAGE(4, "storage", "库房"),
    FIN(5, "fin", "财务"),
    NS(6, "ns", "护士站"),
    MANAGER(7, "manager", "管理部门"),
    SUPPORT(8, "support", "后勤部门"),
    OTHER(9, "other", "其他");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
