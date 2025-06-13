/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.IRegisterReportAppService;
import com.openhis.web.reportmanage.appservice.IRegisterReportAppService;
import com.openhis.web.reportmanage.dto.ChargeReportInitDto;
import com.openhis.web.reportmanage.dto.RegisterReportInitDto;
import com.openhis.web.reportmanage.dto.RegisterReportPageDto;
import com.openhis.web.reportmanage.dto.RegisterReportSearchParam;
import com.openhis.web.reportmanage.mapper.RegisterReportMapper;
import com.openhis.web.reportmanage.mapper.RegisterReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 挂号明细报表 impl
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Service
public class RegisterReportAppServiceImpl implements IRegisterReportAppService {

    @Autowired
    private RegisterReportMapper registerReportMapper;
    @Resource
    private IPractitionerService practitionerService;
    @Resource
    private IOrganizationService organizationService;

    /**
     * 下拉框
     *
     * @return 下拉框信息
     */
    @Override
    public R<?> registerReportInit() {
        RegisterReportInitDto initDto = new RegisterReportInitDto();
        // 查询科室列表
        List<Organization> organizationList =
            organizationService.getList(OrganizationType.DEPARTMENT.getValue(), OrganizationClass.CLINIC.getValue());
        // 科室
        List<RegisterReportInitDto.longCommonStatusOption> departmentOptions = organizationList.stream()
            .map(organization -> new RegisterReportInitDto.longCommonStatusOption(organization.getId(),
                organization.getName()))
            .collect(Collectors.toList());
        // 查询医生列表
        List<Practitioner> applicantList = practitionerService.getList();

        initDto.setDoctorOptions(applicantList).setDepartmentOptions(departmentOptions);
        return R.ok(initDto);
    }

    /**
     * 挂号明细列表
     *
     * @param registerReportSearchParam 挂号明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 挂号明细
     */
    @Override
    public R<?> getPage(RegisterReportSearchParam registerReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<RegisterReportSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(registerReportSearchParam, searchKey, null, request);

        // 查询挂号明细分页列表
        Page<RegisterReportPageDto> registerReportPage =
            registerReportMapper.selectRegisterReportPage(new Page<>(pageNo, pageSize), queryWrapper,
                ChargeItemContext.REGISTER.getValue(), ParticipantType.REGISTRATION_DOCTOR.getCode(),
                PaymentStatus.SUCCESS.getValue(), PaymentStatus.REFUND_ALL.getValue());
        // 状态转换 单位
        registerReportPage.getRecords().forEach(infoDto -> {
            if (infoDto.getQuantityUnit() == null || infoDto.getQuantityUnit().isEmpty()) {
                infoDto.setQuantityUnit_dictText("次");
            }
        });

        return R.ok(registerReportPage);
    }
}
