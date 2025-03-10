package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;

/**
 * 岗位管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IPractitionerRoleService extends IService<PractitionerRole> {

    /**
     * 根据执行人ID查询
     *
     * @param practitionerId 执行人ID
     * @return 岗位管理实体
     */
    PractitionerRole getPractitionerRoleById(long practitionerId);

}