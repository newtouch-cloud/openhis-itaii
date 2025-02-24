package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrganizationType {
    // Healthcare Provider
    HEALTHCARE_PROVIDER(1, "Healthcare Provider", "An organization that provides healthcare services."),

    // Hospital Department
    HOSPITAL_DEPARTMENT(2, "Hospital Department", "A department or ward within a hospital (Generally is not applicable to top level organizations)"),

    // Organizational team
    ORGANIZATIONAL_TEAM(3, "Organizational team", "An organizational team is usually a grouping of practitioners that perform a specific function within an organization (which could be a top level organization, or a department)."),

    // Government
    GOVERNMENT(4, "Government", "A political body, often used when including organization records for government bodies such as a Federal Government, State or Local Government."),

    // Insurance Company
    INSURANCE_COMPANY(5, "Insurance Company", "A company that provides insurance to its subscribers that may include healthcare related policies."),

    // Payer
    PAYER(6, "Payer", "A company, charity, or governmental organization, which processes claims and/or issues payments to providers on behalf of patients or groups of patients."),

    // Educational Institute
    EDUCATIONAL_INSTITUTE(7, "Educational Institute", "An educational institution that provides education or research facilities."),

    // Religious Institution
    RELIGIOUS_INSTITUTION(8, "Religious Institution", "An organization that is identified as a part of a religious institution."),

    // Clinical Research Sponsor
    CLINICAL_RESEARCH_SPONSOR(9, "Clinical Research Sponsor", "An organization that is identified as a Pharmaceutical/Clinical Research Sponsor."),

    // Community Group
    COMMUNITY_GROUP(10, "Community Group", "An un-incorporated community group."),

    // Non-Healthcare Business or Corporation
    NON_HEALTHCARE_BUSINESS(11, "Non-Healthcare Business or Corporation", "An organization that is a registered business or corporation but not identified by other types."),

    // Network
    NETWORK(12, "Network", "A healthcare provider insurance network");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
