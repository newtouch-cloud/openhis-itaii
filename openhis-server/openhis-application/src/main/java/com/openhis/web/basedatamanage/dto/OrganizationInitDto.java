/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.List;

import com.openhis.common.enums.OrganizationClass;
import com.openhis.common.enums.OrganizationType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 科室初始化 dto
 *
 * @author
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class OrganizationInitDto {

    private List<OrganizationTypeOption> organizationTypeOptions;

    private List<OrganizationClassOption> organizationClassOptions;

    @Data
    public static class OrganizationTypeOption {
        private Integer value;
        private String info;

        public OrganizationTypeOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

    @Data
    public static class OrganizationClassOption {
        private Integer value;
        private String info;

        public OrganizationClassOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

    public OrganizationInitDto() {
        List<OrganizationTypeOption> organizationTypeOptionList = List.of(
            new OrganizationTypeOption(OrganizationType.HOSPITAL.getValue(), OrganizationType.HOSPITAL.getInfo()),
            new OrganizationTypeOption(OrganizationType.DEPARTMENT.getValue(), OrganizationType.DEPARTMENT.getInfo()));

        List<OrganizationClassOption> organizationClassOptionList = List.of(
            new OrganizationClassOption(OrganizationClass.CLINIC.getValue(), OrganizationClass.CLINIC.getInfo()),
            new OrganizationClassOption(OrganizationClass.INPATIENT.getValue(), OrganizationClass.INPATIENT.getInfo()),
            new OrganizationClassOption(OrganizationClass.PHARMACY.getValue(), OrganizationClass.PHARMACY.getInfo()),
            new OrganizationClassOption(OrganizationClass.STORAGE.getValue(), OrganizationClass.STORAGE.getInfo()),
            new OrganizationClassOption(OrganizationClass.FIN.getValue(), OrganizationClass.FIN.getInfo()),
            new OrganizationClassOption(OrganizationClass.NS.getValue(), OrganizationClass.NS.getInfo()),
            new OrganizationClassOption(OrganizationClass.MANAGER.getValue(), OrganizationClass.MANAGER.getInfo()),
            new OrganizationClassOption(OrganizationClass.SUPPORT.getValue(), OrganizationClass.SUPPORT.getInfo()),
            new OrganizationClassOption(OrganizationClass.OTHER.getValue(), OrganizationClass.OTHER.getInfo()));

        this.organizationTypeOptions = organizationTypeOptionList;
        this.organizationClassOptions = organizationClassOptionList;
    }
}
