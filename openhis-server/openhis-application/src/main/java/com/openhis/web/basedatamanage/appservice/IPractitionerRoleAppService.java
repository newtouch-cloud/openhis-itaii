package com.openhis.web.basedatamanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.dto.PractRoleSearchParam;
import com.openhis.web.basedatamanage.dto.PractitionerRoleDto;

/**
 * PractitionerRole 应该服务类
 */
public interface IPractitionerRoleAppService {
    /**
     * 查询岗位
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 岗位分页列表
     */
    R<?> getPractitionerPage(PractRoleSearchParam practRoleSearchParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 岗位信息详情
     *
     * @param practitionerRoleId 岗位信息id
     * @return 岗位信息详情
     */
    R<?> getPractitionerRoleById(Long practitionerRoleId);

    /**
     * 添加/编辑岗位信息
     *
     * @param practitionerRoleDto 岗位信息
     * @return 操作结果
     */
    R<?> addOrEditPractitionerRole(PractitionerRoleDto practitionerRoleDto);

    /**
     * 岗位信息
     *
     * @param practitionerRoleId 岗位信息id
     * @return 操作结果
     */
    R<?> deletePractitionerRole(Long practitionerRoleId);

}
