package com.openhis.web.pharmacymanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.dto.PendingMedicationSearchParam;

/**
 * 待发药明细 应用实现接口
 *
 * @author yuanzs
 * @date 2025/4/14
 */
public interface IPendingMedicationDetailsAppService {

    /**
     * 分页查询待发药明细
     *
     * @param pendingMedicationSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 待发药明细
     */
    R<?> getPage(PendingMedicationSearchParam pendingMedicationSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);

}
