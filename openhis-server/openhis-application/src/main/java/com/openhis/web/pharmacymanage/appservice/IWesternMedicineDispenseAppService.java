package com.openhis.web.pharmacymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.dto.DispenseMedicineDto;
import com.openhis.web.pharmacymanage.dto.EncounterInfoPageDto;

import java.util.List;

/**
 * 处方管理 应用实现接口
 *
 * @author wangyang
 * @date 2025/3/18
 */
public interface IWesternMedicineDispenseAppService {

    /**
     * 页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

    /**
     * 分页查询发药病人列表
     *
     * @param encounterInfoPageDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 发药病人列表
     */
    R<?> getEncounterInfoListPage(EncounterInfoPageDto encounterInfoPageDto, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 查询处方单列表
     *
     * @param encounterId 就诊号
     * @param dispenseStatus 发药状态
     * @return 处方单列表
     */
    R<?> getPrescriptionInfo(Long encounterId,Integer dispenseStatus);

    /**
     * 配药
     *
     * @param dispenseMedicineList 配药信息
     * @return 处理结果
     */
    R<?> medicinePrepare(List<DispenseMedicineDto> dispenseMedicineList);

    /**
     * 处方单核对发药
     *
     * @param dispenseMedicineList 发药信息
     * @return 处理结果
     */
    R<?> medicineDispense(List<DispenseMedicineDto> dispenseMedicineList);

    /**
     * 作废
     *
     * @param dispenseMedicineList 作废信息
     * @return 处理结果
     */
    R<?> medicineCancel(List<DispenseMedicineDto> dispenseMedicineList);
}
