package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Patient;

import java.util.Date;

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

    /**
     * 判断日期是否为未来时间
     *
     * @param date 字符串日期
     * @return 是/否
     */
    boolean isFuture(String date);


}