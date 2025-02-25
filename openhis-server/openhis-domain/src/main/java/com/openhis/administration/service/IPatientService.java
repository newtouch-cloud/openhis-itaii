package com.openhis.administration.service;

import java.util.Date;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Patient;

/**
 * 患者管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IPatientService extends IService<Patient> {

    /**
     * 从身份证号码中提取生日
     * 
     * @param idCard 身份证号
     * @return 出生日
     */
    Date extractBirthday(String idCard);
}