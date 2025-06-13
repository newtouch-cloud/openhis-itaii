package com.openhis.web.basedatamanage.appservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.domain.OrganizationLocation;
import com.openhis.administration.mapper.OrganizationLocationMapper;
import com.openhis.administration.service.ILocationService;
import com.openhis.administration.service.IOrganizationLocationService;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.enums.OrganizationType;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.appservice.IOrganizationLocationAppService;
import com.openhis.web.basedatamanage.dto.OrgLocInitDto;
import com.openhis.web.basedatamanage.dto.OrgLocQueryDto;
import com.openhis.web.basedatamanage.dto.OrgLocQueryParam;

@Service
public class OrganizationLocationAppServiceImpl implements IOrganizationLocationAppService {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private OrganizationLocationMapper organizationLocationMapper;

    @Autowired
    private IOrganizationLocationService organizationLocationService;

    /**
     * 机构位置关系初始化
     *
     * @return 操作结果
     */
    @Override
    public R<?> organizationLocationInit() {
        OrgLocInitDto initDto = new OrgLocInitDto();
        // 位置类型
        List<OrgLocInitDto.locationFormOption> chargeItemStatusOptions = new ArrayList<>();
        chargeItemStatusOptions
            .add(new OrgLocInitDto.locationFormOption(LocationForm.CABINET.getValue(), LocationForm.CABINET.getInfo()));
        chargeItemStatusOptions.add(
            new OrgLocInitDto.locationFormOption(LocationForm.PHARMACY.getValue(), LocationForm.PHARMACY.getInfo()));

        // 获取科室下拉选列表
        List<Organization> organizationList = organizationService.getList(OrganizationType.DEPARTMENT.getValue(), null);
        List<OrgLocInitDto.departmentOption> organizationOptions = organizationList.stream()
            .map(organization -> new OrgLocInitDto.departmentOption(organization.getId(), organization.getName()))
            .collect(Collectors.toList());
        initDto.setLocationFormOptions(chargeItemStatusOptions).setDepartmentOptions(organizationOptions);
        return R.ok(initDto);
    }

    /**
     * 根据类型查询药房/药库
     *
     * @param locationForm 查询字段
     * @return 机构位置关系分页列表
     */
    @Override
    public R<?> getLocationListByForm(Integer locationForm) {

        OrgLocInitDto orgLocInitDto = new OrgLocInitDto();
        List<Location> locationList = new ArrayList<>();
        if (LocationForm.CABINET.getValue().equals(locationForm)) {
            locationList = locationService.getCabinetList();
        } else if (LocationForm.PHARMACY.getValue().equals(locationForm)) {
            locationList = locationService.getPharmacyList();
        }
        List<OrgLocInitDto.locationOption> locationOptions = locationList.stream()
            .map(location -> new OrgLocInitDto.locationOption(location.getId(), location.getName()))
            .collect(Collectors.toList());
        orgLocInitDto.setLocationOptions(locationOptions);
        return R.ok(orgLocInitDto);
    }

    /**
     * 机构位置关系分页列表
     *
     * @param orgLocQueryParam 查询字段
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 机构位置关系分页列表
     */
    @Override
    public R<?> getOrgLocPage(OrgLocQueryParam orgLocQueryParam, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<OrganizationLocation> queryWrapper =
            HisQueryUtils.buildQueryWrapper(orgLocQueryParam, null, null, request);
        // 查询机构位置分页列表
        Page<OrgLocQueryDto> orgLocQueryDtoPage =
            HisPageUtils.selectPage(organizationLocationMapper, queryWrapper, pageNo, pageSize, OrgLocQueryDto.class);
        return R.ok(orgLocQueryDtoPage);
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
