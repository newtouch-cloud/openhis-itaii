package com.openhis.web.patientmanage.appservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.patientmanage.dto.PatientInfoInitDto;
import com.openhis.web.patientmanage.dto.PatientInformationDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

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
     * 分页查询病人信息
     *
     * @param busNo 病人ID（可选）
     * @param name 病人姓名（可选）
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10））
     * @return 分页查询
     */
    Page<PatientInformationDto> getPatient(String busNo, String name, Integer pageNo, Integer pageSize);

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

    /**
     * 判断身份证号是否存在
     *
     * @param idCard 身份证号
     * @return 是/否
     */
    boolean existsByIdCard(String idCard);

}
