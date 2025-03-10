package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;

/**
 * 医疗参与者管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IPractitionerService extends IService<Practitioner> {

    /**
     * 根据系统登录的userId查询
     *
     * @param userId 系统登录的userId
     * @return 医疗参与者管理实体
     */
    Practitioner getPractitionerByUserId(long userId);

}