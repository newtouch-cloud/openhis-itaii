package com.openhis.web.pharmacymanage.appservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.enums.DispenseStatus;
import com.openhis.common.enums.EncounterClass;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.pharmacymanage.appservice.IPendingMedicationDetailsAppService;
import com.openhis.web.pharmacymanage.dto.PendingMedicationPageDto;
import com.openhis.web.pharmacymanage.dto.PendingMedicationSearchParam;
import com.openhis.web.pharmacymanage.mapper.PendingMedicationDetailsMapper;
import com.openhis.web.reportmanage.dto.LossReportSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 待发药明细 应用实现类
 *
 * @author yuanzs
 * @date 2025/4/14
 */
@Service
public class PendingMedicationDetailsAppServiceImpl implements IPendingMedicationDetailsAppService {

    @Autowired
    private PendingMedicationDetailsMapper pendingMedicationDetailsMapper;

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
    @Override
    public R<?> getPage(PendingMedicationSearchParam pendingMedicationSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<PendingMedicationSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(
            pendingMedicationSearchParam, searchKey, new HashSet<>(Arrays.asList("medicine_name", "medicine_no")), request);

        // 查询待发药明细列表
        Page<PendingMedicationPageDto> pendingMedicationPage = pendingMedicationDetailsMapper
            .selectPendingMedicationDetailsPage(new Page<>(pageNo, pageSize), queryWrapper,
                DispenseStatus.IN_PROGRESS.getValue(), EncounterClass.AMB.getValue(), EncounterClass.IMP.getValue());

        pendingMedicationPage.getRecords().forEach(e -> {
            // 发药类型
            e.setDispenseEnum_enumText(EnumUtils.getInfoByValue(EncounterClass.class, e.getDispenseEnum()));
        });

        return R.ok(pendingMedicationPage);
    }

}
