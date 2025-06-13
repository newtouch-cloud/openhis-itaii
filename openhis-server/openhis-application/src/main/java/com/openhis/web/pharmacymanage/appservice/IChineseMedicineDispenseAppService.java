package com.openhis.web.pharmacymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.dto.EncounterInfoSearchParam;

/**
 * 处方管理 应用实现接口
 *
 * @author wangyang
 * @date 2025/3/18
 */
public interface IChineseMedicineDispenseAppService {

    /**
     * 页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

    /**
     * 分页查询就诊病人列表
     *
     * @param encounterInfoSearchParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 就诊病人分页列表
     */
    R<?> getEncounterInfoListPage(EncounterInfoSearchParam encounterInfoSearchParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 查询处方单列表
     *
     * @param encounterId 就诊号
     * @return 处方单列表
     */
    R<?> getPrescriptionInfo(Long encounterId);

    /**
     * 配药
     *
     * @param prescriptionNo 处方号
     * @param preparerId 配药人
     * @return 处理结果
     */
    R<?> prepare(String prescriptionNo, Long preparerId);

    /**
     * 处方单核对发药
     *
     * @param prescriptionNo 处方号
     * @return 处理结果
     */
    R<?> medicineDispense(String prescriptionNo);

    /**
     * 处方单作废
     *
     * @param prescriptionNo 处方号
     * @param notPerformedReasonEnum 未发药原因
     * @return 处理结果
     */
    R<?> medicineCancel(String prescriptionNo, Integer notPerformedReasonEnum);
}
