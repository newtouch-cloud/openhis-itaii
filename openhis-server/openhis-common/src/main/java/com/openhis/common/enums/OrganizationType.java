package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrganizationType implements HisEnumInterface {
    // // Healthcare Provider
    // HEALTHCARE_PROVIDER(1, "Healthcare Provider", "医院"),
    //
    // // Hospital Department
    // HOSPITAL_DEPARTMENT(2, "Hospital Department", "科室"),
    //
    // // Organizational team
    // ORGANIZATIONAL_TEAM(3, "Organizational team", "团队"),
    //
    // // Government
    // GOVERNMENT(4, "Government", "政府"),
    //
    // // Insurance Company
    // INSURANCE_COMPANY(5, "Insurance Company", "医保局"),
    //
    // // Payer
    // PAYER(6, "Payer", "A company, charity, or governmental organization, which processes claims and/or issues
    // payments to providers on behalf of patients or groups of patients."),
    //
    // // Educational Institute
    // EDUCATIONAL_INSTITUTE(7, "Educational Institute", "An educational institution that provides education or research
    // facilities."),
    //
    // // Religious Institution
    // RELIGIOUS_INSTITUTION(8, "Religious Institution", "An organization that is identified as a part of a religious
    // institution."),
    //
    // // Clinical Research Sponsor
    // CLINICAL_RESEARCH_SPONSOR(9, "Clinical Research Sponsor", "An organization that is identified as a
    // Pharmaceutical/Clinical Research Sponsor."),
    //
    // // Community Group
    // COMMUNITY_GROUP(10, "Community Group", "An un-incorporated community group."),
    //
    // // Non-Healthcare Business or Corporation
    // NON_HEALTHCARE_BUSINESS(11, "Non-Healthcare Business or Corporation", "An organization that is a registered
    // business or corporation but not identified by other types."),
    //
    // // Network
    // NETWORK(12, "Network", "A healthcare provider insurance network");
    HOSPITAL(1, "1", "医院"),

    DEPARTMENT(2, "2", "科室");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static OrganizationType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (OrganizationType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
