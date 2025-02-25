package com.openhis.web.patientmanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.administration.domain.Patient;
import com.openhis.web.patientmanage.dto.PatientInformationDto;

/**
 * 病人信息管理
 *
 * @author Wuser
 * @date 2025/2/25
 */
@Repository
public interface PatientManageMapper extends BaseMapper<Patient> {

    /**
     * 病人信息分页查询
     *
     * @param busNo 病人ID
     * @param name 病人姓名
     * @param pageSize 页面大小
     * @param offset 跳过条数
     * @return 分页查询
     */
    List<PatientInformationDto> getPatientPage(@Param("busNo") String busNo, @Param("name") String name,
        @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    /**
     * 统计总记录数的方法
     *
     * @param busNo 病人ID
     * @param name 病人姓名
     * @return 分页查询
     */
    long countPatients(@Param("busNo") String busNo, @Param("name") String name);

}
