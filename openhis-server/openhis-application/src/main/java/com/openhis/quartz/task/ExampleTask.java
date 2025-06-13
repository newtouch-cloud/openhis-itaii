package com.openhis.quartz.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.core.common.core.domain.R;
import com.core.common.utils.DateUtils;
import com.openhis.web.basedatamanage.appservice.IOrganizationAppService;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务例子
 * 
 * @author system
 */
@Slf4j
@Component("exampleTask")
public class ExampleTask {

    @Autowired
    private IOrganizationAppService organizationAppService;

    /**
     * 定时获取机构信息
     */
    public void getOrgInfo() {
        log.info("定时获取机构信息START，时间：{}", DateUtils.getDate());

        // 处理部分
        R<?> r = organizationAppService.getOrgInfo(1907249098877554689L);

        log.info("定时获取机构信息END，机构信息：{}，时间：{}", r.getData(), DateUtils.getDate());
    }
}
