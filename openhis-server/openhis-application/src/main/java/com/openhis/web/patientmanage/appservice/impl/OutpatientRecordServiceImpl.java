package com.openhis.web.patientmanage.appservice.impl;

import java.util.List;

import javax.annotation.Resource;

import com.core.common.core.domain.R;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.EncounterSubjectStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.patientmanage.appservice.IOutpatientRecordService;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;
import com.openhis.web.patientmanage.mapper.PatientManageMapper;

/**
 * 门诊记录查询 应用实现
 *
 * @author Wuser
 * @date 2025/3/15
 */
@Service
public class OutpatientRecordServiceImpl implements IOutpatientRecordService {

    @Resource
    PatientManageMapper patientManageMapper;

    /**
     * 分页查询门诊记录
     *
     * @param outpatientRecordSearchParam 门诊录查询参数
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     * @return 分页查询
     */
    public Page<OutpatientRecordDto> getPatient(OutpatientRecordSearchParam outpatientRecordSearchParam, Integer pageNo,
        Integer pageSize) {

        // 跳过的记录数
        Integer offset = (pageNo - 1) * pageSize;
        // 连表查询患者信息
        List<OutpatientRecordDto> listOutpatientRecords =
            patientManageMapper.getOutpatientRecord(outpatientRecordSearchParam, pageSize, offset);
        // 查询总记录数
        long total = patientManageMapper.countOutpatientRecords(outpatientRecordSearchParam);
        // 创建Page对象并设置属性
        Page<OutpatientRecordDto> outpatientRecordPage = new Page<>(pageNo, pageSize, total);
        outpatientRecordPage.setRecords(listOutpatientRecords);
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
