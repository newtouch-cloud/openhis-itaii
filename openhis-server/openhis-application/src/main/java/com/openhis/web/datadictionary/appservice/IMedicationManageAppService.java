/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;

/**
 * 药品目录 service
 *
 * @author
 * @date 2025-03-17
 */
public interface IMedicationManageAppService {
    /**
     * 初始化药品目录信息
     *
     */
    R<?> getMedicationInit();

    /**
     * 药品目录查询
     *
     * @param searchKey 查询条件
     * @param ybMatchFlag 查询条件-是否对码
     * @param statusEnum 查询条件-状态
     * @param categoryCode 查询条件-药品分类
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 药品目录查询结果
     */
    R<?> getMedicationList(String searchKey, Integer ybMatchFlag, Integer statusEnum, String categoryCode,
        Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 药品目录详细查询
     *
     * @param id 查询条件
     * @return 药品目录查询结果
     */
    R<?> getMedicationOne(@PathVariable("id") Long id);

    /**
     * 编辑药品目录信息
     *
     * @param medicationManageUpDto 药品目录信息
     */
    R<?> editMedication(MedicationManageUpDto medicationManageUpDto);

    /**
     * 药品目录停用
     *
     * @param ids 药品目录ID列表
     * @return
     */
    R<?> editMedicationStop(@RequestBody List<Long> ids);

    /**
     * 药品目录启用
     *
     * @param ids 药品目录ID列表
     * @return
     */
    R<?> editMedicationStart(@RequestBody List<Long> ids);

    /**
     * 添加药品目录信息
     *
     * @param medicationManageUpDto 药品目录信息
     */
    R<?> addMedication(MedicationManageUpDto medicationManageUpDto);

    R<?> exportMedication(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "ybMatchFlag", defaultValue = "-1") Integer ybMatchFlag,
        @RequestParam(value = "statusEnum", defaultValue = "-1") Integer statusEnum,
        @RequestParam(value = "categoryCode", defaultValue = "") String categoryCode, HttpServletResponse response);
}
