package com.openhis.web.datadictionary.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 项目定价 init基础数据
 */
@Data
@Accessors(chain = true)
public class ItemDefinitionInitDto {

    private List<PublicationStatusOption> publicationStatusOptions;
    private List<ChargeItemContextOption> chargeItemContextOptions;

    /**
     * 状态
     */
    @Data
    public static class PublicationStatusOption {
        private Integer value;
        private String label;

        public PublicationStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 收费项目类型
     */
    @Data
    public static class ChargeItemContextOption {
        private Integer value;
        private String label;

        public ChargeItemContextOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

}
