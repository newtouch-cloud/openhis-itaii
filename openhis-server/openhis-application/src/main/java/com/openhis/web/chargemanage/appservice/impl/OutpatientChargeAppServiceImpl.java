/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.chargemanage.appservice.IOutpatientChargeAppService;
import com.openhis.web.chargemanage.dto.EncounterPatientPageDto;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;
import com.openhis.web.chargemanage.mapper.OutpatientChargeAppMapper;

/**
 * 门诊收费 impl
 *
 * @author zwh
 * @date 2025-03-12
 */
@Service
public class OutpatientChargeAppServiceImpl implements IOutpatientChargeAppService {

    @Autowired
    private OutpatientChargeAppMapper outpatientChargeAppMapper;

    /**
     * 查询就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    @Override
    public R<?> getEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<EncounterPatientPageParam> queryWrapper = HisQueryUtils.buildQueryWrapper(
            encounterPatientPageParam, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientWbStr, CommonConstants.FieldName.PatientPyStr,
                CommonConstants.FieldName.PatientName, CommonConstants.FieldName.PatientBusNo,
                CommonConstants.FieldName.EncounterBusNo, CommonConstants.FieldName.idCard)),
            request);
        // 就诊患者分页列表
        Page<EncounterPatientPageDto> encounterPatientPage =
            outpatientChargeAppMapper.selectEncounterPatientPage(new Page<>(pageNo, pageSize), queryWrapper);

        encounterPatientPage.getRecords().forEach(e -> {
            // 性别枚举
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });
        return R.ok(encounterPatientPage);
    }
}
