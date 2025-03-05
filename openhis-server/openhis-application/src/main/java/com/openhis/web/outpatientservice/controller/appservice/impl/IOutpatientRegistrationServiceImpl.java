package com.openhis.web.outpatientservice.controller.appservice.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.AgeCalculatorUtil;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.outpatientservice.controller.appservice.IOutpatientRegistrationService;
import com.openhis.web.outpatientservice.dto.PatientMetadata;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 门诊挂号 实现类
 */
@Service
public class IOutpatientRegistrationServiceImpl implements IOutpatientRegistrationService {

    @Resource
    PatientMapper patientMapper;

    /**
     * 门诊挂号 - 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo    当前页
     * @param pageSize  每页多少条
     * @return 患者信息
     */
    @Override
    public Page<PatientMetadata> getPatientMetadataBySearchKey(String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<Patient> queryWrapper = HisQueryUtils.buildQueryWrapper(null,
                searchKey, new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null
        );
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 患者信息
        Page<PatientMetadata> patientMetadataPage = HisPageUtils.selectPage(patientMapper,
                queryWrapper, pageNo, pageSize, PatientMetadata.class);

        patientMetadataPage.getRecords().forEach(e -> {
            // 性别枚举
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });
        return patientMetadataPage;
    }

}
