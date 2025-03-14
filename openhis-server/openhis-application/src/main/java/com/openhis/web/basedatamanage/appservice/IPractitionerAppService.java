package com.openhis.web.basedatamanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.dto.PractSearchParam;
import com.openhis.web.basedatamanage.dto.PractitionerDto;

/**
 * Practitioner 应该服务类
 */
public interface IPractitionerAppService {
    /**
     * 查询员工信息
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 员工分页列表
     */
    R<?> getPractitionerPage(PractSearchParam practSearchParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 员工信息详情
     *
     * @param practitionerId 员工信息id
     * @return 员工信息详情
     */
    R<?> getPractitionerById(Long practitionerId);

    /**
     * 添加/编辑员工信息
     *
     * @param practitionerDto 员工信息
     * @return 操作结果
     */
    R<?> addOrEditPractitioner(PractitionerDto practitionerDto);

    /**
     * 员工信息
     *
     * @param practitionerId 员工信息id
     * @return 操作结果
     */
    R<?> deletePractitioner(Long practitionerId);
}
