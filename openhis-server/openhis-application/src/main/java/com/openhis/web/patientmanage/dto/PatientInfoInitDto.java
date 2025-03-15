package com.openhis.web.patientmanage.dto;

import com.openhis.web.outpatientmanage.dto.OutpatientInfusionInitDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 病人信息初期信息列表
 *
 * @author liuhr
 * @date 2025/2/25
 */
@Data
@Accessors(chain = true)
public class PatientInfoInitDto {

    //获取婚姻状态列表
    private List<PatientInfoInitDto.statusEnumOption> maritalStatus;
    //获取职业编码列表
    private List<PatientInfoInitDto.statusEnumOption>  occupationType;
    //获取性别列表
    private List<PatientInfoInitDto.statusEnumOption>  administrativeGender;
    //获取ABO血型列表
    private List<PatientInfoInitDto.statusEnumOption>  bloodTypeABO;
    //获取RH血型列表
    private List<PatientInfoInitDto.statusEnumOption>  bloodTypeRH;
    //获取家庭关系列表
    private List<PatientInfoInitDto.statusEnumOption>  familyRelationshipType;

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
