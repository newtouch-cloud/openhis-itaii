package com.openhis.web.patientmanage.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.patientmanage.dto.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 病人管理——病人信息
 *
 * @author liuhr
 * @date 2025/3/15
 */
public interface IPatientInformationService {

    /**
     * 获取病人信息记录初期数据列表
     *
     * @return 病人信息记录初期数据列表
     */
    PatientInfoInitDto getPatientInfoInit();

    /**
     * 分页查询门诊记录
     *
     * @param patientInfoSearchParam 病人查询参数
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     * @return 分页查询
     */
    IPage<PatientInformationDto> getPatientInfo(PatientInfoSearchParam patientInfoSearchParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request);


    /**
     * 修改病人信息
     *
     * @param patientInformationDto 病人信息
     * @return 更新结果
     */
    R<?>  editPatient(PatientInformationDto patientInformationDto);

    /**
     * 添加病人信息
     *
     * @param patientInformationDto 病人信息
     */
    R<?> addPatient(PatientInformationDto patientInformationDto);

}
