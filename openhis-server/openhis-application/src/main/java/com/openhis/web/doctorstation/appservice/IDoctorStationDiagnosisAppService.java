package com.openhis.web.doctorstation.appservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.ConditionDefinitionMetadata;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingDto;
import com.openhis.web.doctorstation.dto.SaveDiagnosisParam;

/**
 * 医生站-诊断 应用Service
 */
public interface IDoctorStationDiagnosisAppService {

    /**
     * 新增诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    R<?> addDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto);

    /**
     * 编辑诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    R<?> updateDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto);

    /**
     * 查询诊断归属绑定列表
     *
     * @param diagnosisBelongBindingDto 查询条件dto
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断归属绑定列表
     */
    R<?> getDiagnosisBelongBindingPage(DiagnosisBelongBindingDto diagnosisBelongBindingDto, String searchKey,
        Integer pageNo, Integer pageSize);

    /**
     * 删除诊断归属绑定
     *
     * @param id ID
     * @return 结果
     */
    R<?> delDiagnosisBelongBinding(Long id);

    /**
     * 查询诊断信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断信息
     */
    Page<ConditionDefinitionMetadata> getConditionDefinitionMetadataSearchKey(String searchKey, Integer pageNo,
        Integer pageSize);

    /**
     * 医生保存诊断
     *
     * @param saveDiagnosisParam 诊断信息
     * @return 结果
     */
    R<?> saveDoctorDiagnosis(SaveDiagnosisParam saveDiagnosisParam);

    /**
     * 查询诊断定义业务分类数据
     *
     * @param patientId 患者id
     * @return 诊断定义业务分类数据
     */
    R<?> getConditionDefinitionBusinessClass(Long patientId);

}
