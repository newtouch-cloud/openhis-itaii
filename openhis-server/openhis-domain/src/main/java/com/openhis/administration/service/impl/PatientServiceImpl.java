package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.service.IPatientService;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * 患者管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

}