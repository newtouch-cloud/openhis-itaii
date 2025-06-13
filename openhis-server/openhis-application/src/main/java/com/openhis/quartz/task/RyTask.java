package com.openhis.quartz.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.core.common.utils.StringUtils;
import com.core.framework.config.TenantContext;
import com.openhis.administration.domain.Location;
import com.openhis.administration.service.ILocationService;

/**
 * 定时任务调度测试
 * 
 * @author system
 */
@Component("ryTask")
public class RyTask {

    @Resource
    ILocationService locationService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        // 定时任务指定租户id,示例
        try {
            Integer tenantId = i;
            // 设置当前线程的租户ID
            TenantContext.setCurrentTenant(tenantId);
            List<Location> pharmacyList = locationService.getPharmacyList();
            System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
        } finally {
            // 清除线程局部变量，防止内存泄漏
            TenantContext.clear();
        }

    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }
}
