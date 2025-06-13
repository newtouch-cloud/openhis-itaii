package com.openhis.web.doctorstation.appservice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.AllergyIntoInfoDto;

/**
 * 医生站-患者过敏与不耐受管理的应用类
 *
 * @author liuhr
 * @date 2025/4/10
 */
public interface IDoctorStationAllergyIntolAppService {

    /**
     * 患者过敏与不耐受数据初始化
     *
     * @return 基础数据
     */
    @GetMapping(value = "/init")
    R<?> init();

    /**
     * 查询患者过敏与不耐受信息
     *
     * @param patientId 患者Id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者过敏与不耐受信息
     */
    R<?> getAllergyIntoleranceInfo(Long patientId, Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 作废当条患者过敏与不耐受信息
     *
     * @param allergyIntoInfoDto 患者过敏与不耐受信息
     * @return
     */
    R<?> invalidateAllergyIntolerance(AllergyIntoInfoDto allergyIntoInfoDto);

    /**
     * 新增患者过敏与不耐受信息
     *
     * @param allergyIntoInfoDto 患者过敏与不耐受信息
     * @return
     */
    R<?> addAllergyIntoleranceInfo(AllergyIntoInfoDto allergyIntoInfoDto);

}
