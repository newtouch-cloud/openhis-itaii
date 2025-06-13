package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;

import java.util.List;

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

    /**
     * 根据参与者Id，查询其权限下所有科室id
     *
     * @param  practitionerId 参与者Id
     * @return 科室id
     */
    List<Long> getOrgIdsByPractitionerId(Long practitionerId);

    /**
     * 根据参与者Id，查询其权限下所有位置id
     *
     * @param  practitionerId 参与者Id
     * @return 位置id
     */
    List<Long> getLocationIdsByPractitionerId(Long practitionerId);


    /**
     * 根据科室Id，查询医生列表
     *
     * @param  orgId 科室Id
     * @return 医生列表
     */
    List<PractitionerRole> getDoctorList(Long orgId);

    /**
     * 根据科室Id，查询护士列表
     *
     * @return 护士列表
     */
    List<PractitionerRole> getNurseList(Long orgId);

}
