package com.openhis.administration.service.impl;

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

        // 将 LocalDate 转换为 java.util.Date
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public boolean isFuture(String dateString) {
        // 创建 DateTimeFormatter 对象，并设置所需的日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        try {
            // 解析字符串为 LocalDate 对象
            LocalDate dateToCheck = LocalDate.parse(dateString, formatter);
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 检查日期是否是未来的时间
            return dateToCheck.isAfter(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            // 解析失败或其他异常，返回 false 或根据需要处理异常
            return false;
        }
    }

}