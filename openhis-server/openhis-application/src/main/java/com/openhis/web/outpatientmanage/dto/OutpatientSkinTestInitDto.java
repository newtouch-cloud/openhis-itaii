package com.openhis.web.outpatientmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 皮试初始化记录
 *
 * @author liuhr
 * @date 2025/3/15
 */
@Data
@Accessors(chain = true)
public class OutpatientSkinTestInitDto {
    //皮试检查项目状态
    private List<OutpatientInfusionInitDto.statusEnumOption> VerificationStatus;
    //皮试结果
    private List<OutpatientInfusionInitDto.statusEnumOption>  clinicalStatus;

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
