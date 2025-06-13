package com.openhis.web.pharmacymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.dto.MedDetailsSearchParam;

/**
 * 发药明细 应用实现接口
 *
 * @author yuanzs
 * @date 2025/4/14
 */
public interface IMedicationDetailsAppService {

    /**
     * 页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();

    /**
     * 门诊人员发药明细表
     *
     * @param medDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 门诊发药明细表
     */
    R<?> getAmbPractitionerDetailPage(MedDetailsSearchParam medDetailsSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

    /**
     * 门诊发药明细流水账
     *
     * @param medDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 门诊发药明细流水账
     */
    R<?> getAmbMedicationDispenseDetailPage(MedDetailsSearchParam medDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request);

//    /**
//     * 门诊/住院人员发药明细帐、住院耗材记账领用明细
//     *
//     * @param medDetailsSearchParam 查询条件
//     * @param pageNo 当前页码
//     * @param pageSize 查询条数
//     * @param searchKey 模糊查询关键字
//     * @param request 请求数据
//     * @return 门诊/住院人员发药明细、住院耗材记账领用明细分页列表
//     */
//    R<?> getMedDetailedAccountPage(MedDetailsSearchParam medDetailsSearchParam, Integer pageNo, Integer pageSize,
//        String searchKey, HttpServletRequest request);
//
//    /**
//     * 门诊/住院发药明细流水帐、住院耗材记账领用流水账
//     *
//     * @param medDetailsSearchParam 查询条件
//     * @param pageNo 当前页码
//     * @param pageSize 查询条数
//     * @param searchKey 模糊查询关键字
//     * @param request 请求数据
//     * @return 门诊/住院发药明细流水帐、住院耗材记账领用流水账分页列表
//     */
//    R<?> getMedRunningAccountPage(MedDetailsSearchParam medDetailsSearchParam, Integer pageNo, Integer pageSize,
//        String searchKey, HttpServletRequest request);

}
