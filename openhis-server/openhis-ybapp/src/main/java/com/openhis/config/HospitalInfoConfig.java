/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * TODO:医疗机构的配置参数
 *
 * @author SunJQ
 * @date 2025-03-15
 */
@Data
@Component
@PropertySource(value="classpath:/config/hospital-config.properties",encoding="UTF-8",ignoreResourceNotFound=true)
@ConfigurationProperties(prefix = "pro")
public class HospitalInfoConfig {
    /**  同步工作流引擎1同步0不同步 */
    private Integer activitiSync;
    /**多租户id配置，编辑用户的时候设置*/
    private String relTenantIds;
    /**医保类型，1-市医保，2-省医保，3-省市医保*/
    private String medicareType;
    /** 定点医药机构编号 */
    private String fixmedinsCode;
    /** 定点医药机构名称 */
    private String fixmedinsName;
    /** 统筹区号 */
    private String admvs;
    /** 行政区划 */
    private String district;
}
