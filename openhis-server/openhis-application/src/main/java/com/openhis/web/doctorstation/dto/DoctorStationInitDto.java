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

    private List<PatientEncounterStatusOption> patientEncounterStatusOptions;

    /**
     * 患者就诊状态
     */
    @Data
    public static class PatientEncounterStatusOption {
        private Integer value;
        private String label;

        public PatientEncounterStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 设置默认值
     */
    public DoctorStationInitDto() {
        List<PatientEncounterStatusOption> options = new ArrayList<>();
        options.add(
            new PatientEncounterStatusOption(EncounterStatus.PLANNED.getValue(), EncounterStatus.PLANNED.getInfo()));
        options.add(new PatientEncounterStatusOption(EncounterStatus.IN_PROGRESS.getValue(),
            EncounterStatus.IN_PROGRESS.getInfo()));
        options.add(
            new PatientEncounterStatusOption(EncounterStatus.ON_HOLD.getValue(), EncounterStatus.ON_HOLD.getInfo()));
        options.add(new PatientEncounterStatusOption(EncounterStatus.DISCHARGED.getValue(),
            EncounterStatus.DISCHARGED.getInfo()));
        this.patientEncounterStatusOptions = options;
    }

}
