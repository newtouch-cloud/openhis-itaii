package com.openhis.web.patientmanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.EncounterSubjectStatus;
import com.openhis.common.enums.ParticipantType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.patientmanage.appservice.IOutpatientRecordService;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;
import com.openhis.web.patientmanage.mapper.PatientManageMapper;

/**
 * 门诊记录查询 应用实现
 *
 * @author liuhr
 * @date 2025/3/15
 */
@Service
public class OutpatientRecordServiceImpl implements IOutpatientRecordService {

    @Resource
    private PatientManageMapper patientManageMapper;

    /**
     * 分页查询门诊记录
     *
     * @param outpatientRecordSearchParam 门诊录查询参数
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     * @return 分页查询
     */
    @Override
    public IPage<OutpatientRecordDto> getPatient(OutpatientRecordSearchParam outpatientRecordSearchParam,
        String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<OutpatientRecordDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(outpatientRecordSearchParam, searchKey,
                new HashSet<>(Arrays.asList(CommonConstants.FieldName.idCard, CommonConstants.FieldName.Name,
                    CommonConstants.FieldName.PatientBusNo, CommonConstants.FieldName.EncounterBusNo)),
                request);

        IPage<OutpatientRecordDto> outpatientRecordPage = patientManageMapper
            .getOutpatientRecord(ParticipantType.ADMITTER.getCode(), new Page<>(pageNo, pageSize), queryWrapper);

        outpatientRecordPage.getRecords().forEach(e -> {
            // 性别枚举类回显赋值
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 就诊对象状态枚举类回显赋值
            e.setSubjectStatusEnum_enumText(
                EnumUtils.getInfoByValue(EncounterSubjectStatus.class, e.getSubjectStatusEnum()));
        });

        return outpatientRecordPage;
    }

    /**
     * 获取医生名字列表
     *
     * @return 医生名字列表
     */
    public R<?> getDoctorNames() {
        return R.ok(patientManageMapper.getDoctorNames());
    }

}
