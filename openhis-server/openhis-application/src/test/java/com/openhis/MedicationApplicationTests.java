/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis;

import java.io.IOException;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试类
 *
 * @author zwh
 * @date 2024-12-03
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.openhis"})
public class MedicationApplicationTests {
    @Test
    public void contextLoads() throws IOException {

    }
}
