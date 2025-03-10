package com.openhis.web.doctorstation.dto;

import java.util.ArrayList;
import java.util.List;

import com.openhis.common.enums.EncounterStatus;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医生站 init基础数据
 */
@Data
@Accessors(chain = true)
public class DoctorStationInitDto {

    private List<patientEncounterStatusOption> patientEncounterStatusOptions;

    /**
     * 患者就诊状态
     */
    @Data
    public static class patientEncounterStatusOption {
        private Integer value;
        private String label;

        public patientEncounterStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 设置默认值
     */
    public DoctorStationInitDto() {
        List<patientEncounterStatusOption> options = new ArrayList<>();
        options.add(
            new patientEncounterStatusOption(EncounterStatus.PLANNED.getValue(), EncounterStatus.PLANNED.getInfo()));
        options.add(new patientEncounterStatusOption(EncounterStatus.IN_PROGRESS.getValue(),
            EncounterStatus.IN_PROGRESS.getInfo()));
        options.add(
            new patientEncounterStatusOption(EncounterStatus.ON_HOLD.getValue(), EncounterStatus.ON_HOLD.getInfo()));
        options.add(new patientEncounterStatusOption(EncounterStatus.DISCHARGED.getValue(),
            EncounterStatus.DISCHARGED.getInfo()));
        this.patientEncounterStatusOptions = options;
    }

}
