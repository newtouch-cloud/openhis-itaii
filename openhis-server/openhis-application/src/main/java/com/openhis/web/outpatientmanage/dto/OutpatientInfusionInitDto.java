package com.openhis.web.outpatientmanage.dto;

import com.openhis.common.enums.ClinicalStatus;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentInitDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 门诊输液初期查询
 *
 * @author liuhr
 * @date 2025/3/12
 */
@Data
@Accessors(chain = true)
public class OutpatientInfusionInitDto {

    //发药状态
    private List<statusEnumOption> medicationStatus;
    //皮试结果
    private List<statusEnumOption>  clinicalStatus;
    //当天位执行的输液记录
    private List<OutpatientInfusionRecordDto>  infusionList;

    /**
     * 状态
     */
    @Data
    public static class statusEnumOption {
        private Integer value;
        private String info;

        public statusEnumOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

}
