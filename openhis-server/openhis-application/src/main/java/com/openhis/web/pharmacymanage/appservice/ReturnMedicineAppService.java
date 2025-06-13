package com.openhis.web.pharmacymanage.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.dto.EncounterInfoPageDto;
import com.openhis.web.pharmacymanage.dto.ReturnMedicineDto;

/**
 * 退药管理 应用实现接口
 *
 * @author yangmo
 * @date 2025/4/4
 */
public interface ReturnMedicineAppService {

    /**
     * 页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

    /**
     * 查询退药患者分页列表
     *
     * @param encounterInfoPageDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 退药患者分页列表
     */
    R<?> getReturnMedicinePatientPage(EncounterInfoPageDto encounterInfoPageDto, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 查询退药信息
     *
     * @param encounterId 就诊ID
     * @param refundStatus 退药id
     * @return 退药信息
     */
    R<?> getReturnMedicineInfo(Long encounterId, Integer refundStatus);

    /**
     * 退药处理
     *
     * @param medicineReturnList 退药清单
     * @return 处理结果
     */
    R<?> medicineReturn(List<ReturnMedicineDto> medicineReturnList);
}
