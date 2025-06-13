package com.openhis.quartz.util;

import org.quartz.JobExecutionContext;

import com.core.quartz.domain.SysJob;
import com.core.quartz.util.JobInvokeUtil;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author system
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
