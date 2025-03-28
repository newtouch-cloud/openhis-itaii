package com.openhis.web.datadictionary.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentSelParam;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentUpDto;

/**
 * 诊疗目录
 *
 * @author liuhr
 * @date 2025/3/28
 */
public interface IDiagnosisTreatmentManageAppService {

    /**
     * 诊疗目录初期查询
     *
     * @return
     */
    R<?> getDiseaseTreatmentInit();

    /**
     * 查询诊疗目录分页列表
     *
     * @param DiagnosisTreatmentSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    R<?> getDiseaseTreatmentPage(DiagnosisTreatmentSelParam DiagnosisTreatmentSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request);

    /**
     * 根据id查询诊疗详情
     *
     * @param id 诊疗ID
     * @return
     */
    R<?> getDiseaseTreatmentOne(Long id);

    /**
     * 诊疗目录编辑
     *
     * @param diagnosisTreatmentUpDto 诊疗目录列表
     * @return
     */
    R<?> editDiseaseTreatment(DiagnosisTreatmentUpDto diagnosisTreatmentUpDto);

    /**
     * 诊疗目录停用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    R<?> editDiseaseTreatmentStop(List<Long> ids);

    /**
     * 诊疗目录启用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    R<?> editDiseaseTreatmentStart(List<Long> ids);

    /**
     * 新增外来诊疗目录
     *
     * @param diagnosisTreatmentUpDto 诊疗目录
     * @return
     */
    @PostMapping("/information")
    R<?> addDiseaseTreatment(DiagnosisTreatmentUpDto diagnosisTreatmentUpDto);

}