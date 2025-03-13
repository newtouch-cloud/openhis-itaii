package com.openhis.web.basedatamanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.OrganizationLocation;
import com.openhis.administration.mapper.OrganizationLocationMapper;
import com.openhis.administration.service.IOrganizationLocationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.appservice.IOrganizationLocationAppService;
import com.openhis.web.basedatamanage.dto.OrgLocQueryDto;
import com.openhis.web.basedatamanage.dto.OrgLocQueryParam;

@Service
public class OrganizationLocationAppServiceImpl implements IOrganizationLocationAppService {

    @Autowired
    private OrganizationLocationMapper organizationLocationMapper;

    @Autowired
    private IOrganizationLocationService organizationLocationService;

    @Override
    public R<?> getOrgLocPage(OrgLocQueryParam orgLocQueryParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<OrganizationLocation> queryWrapper = HisQueryUtils.buildQueryWrapper(orgLocQueryParam, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), request);

        // 查询机构位置分页列表
        Page<OrgLocQueryDto> orgLocQueryDtoPage =
            HisPageUtils.selectPage(organizationLocationMapper, queryWrapper, pageNo, pageSize, OrgLocQueryDto.class);

        return R.ok(orgLocQueryDtoPage);
    }

    /**
     * 机构位置信息详情
     *
     * @param orgLocId 机构位置信息id
     * @return 机构位置信息详情
     */
    @Override
    public R<?> getOrgLocById(Long orgLocId) {
        OrganizationLocation orgLoc = organizationLocationService.getById(orgLocId);
        return R.ok(orgLoc, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"机构位置信息查询"}));
    }

    /**
     * 添加/编辑机构位置信息
     *
     * @param orgLocQueryDto 机构位置信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditOrgLoc(OrgLocQueryDto orgLocQueryDto) {

        OrganizationLocation orgLoc = new OrganizationLocation();
        BeanUtils.copyProperties(orgLocQueryDto, orgLoc);

        if (orgLocQueryDto.getId() != null) {
            // 更新机构位置信息
            organizationLocationService.updateById(orgLoc);
        } else {
            // 新增机构位置信息
            organizationLocationService.save(orgLoc);
        }
        // 返回机构位置id
        return R.ok(orgLoc.getId(),
            MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"机构位置信息更新添加"}));
    }

    /**
     * 删除机构位置
     *
     * @param orgLocId 机构位置信息id
     * @return 操作结果
     */
    @Override
    public R<?> deleteOrgLoc(Long orgLocId) {
        // 删除机构位置信息
        boolean deleteOrgLocSuccess = organizationLocationService.removeById(orgLocId);
        return deleteOrgLocSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"机构位置信息"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"机构位置信息"}));
    }

}
