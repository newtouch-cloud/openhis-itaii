package com.openhis.web.basedatamanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.dto.OrgLocQueryDto;
import com.openhis.web.basedatamanage.dto.OrgLocQueryParam;

/**
 * Organization 应该服务类
 */
public interface IOrganizationLocationAppService {
    /**
     * 查询机构位置
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 机构位置分页列表
     */
    R<?> getOrgLocPage(OrgLocQueryParam orgLocQueryParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 机构位置信息详情
     *
     * @param orgLocId 机构位置信息id
     * @return 机构位置信息详情
     */
    R<?> getOrgLocById(Long orgLocId);

    /**
     * 添加/编辑机构位置信息
     *
     * @param orgLocQueryDto 机构位置信息
     * @return 操作结果
     */
    R<?> addOrEditOrgLoc(OrgLocQueryDto orgLocQueryDto);

    /**
     * 机构位置信息
     *
     * @param orgLocId 机构位置信息id
     * @return 操作结果
     */
    R<?> deleteOrgLoc(Long orgLocId);

}
