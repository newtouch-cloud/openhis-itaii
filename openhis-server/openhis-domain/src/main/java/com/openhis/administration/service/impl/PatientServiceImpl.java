package com.openhis.administration.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.service.IPatientService;

/**
 * 患者管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    /**
     * 从身份证号码中提取生日
     * 
     * @param idCard 身份证号
     * @return 出生日
     */
    public Date extractBirthday(String idCard) {
        if (idCard == null) {
            // 身份证号码为空
            return null;
        }

        // 提取第7到14位作为生日字符串
        String birthdayStr = idCard.substring(6, 14);
        // 将生日字符串转换为 LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(birthdayStr, formatter);
        // 使用系统默认时区,将 LocalDate 转换为 Date
        return Date.from(Instant.from(date.atStartOfDay(ZoneId.systemDefault())));
    }

}