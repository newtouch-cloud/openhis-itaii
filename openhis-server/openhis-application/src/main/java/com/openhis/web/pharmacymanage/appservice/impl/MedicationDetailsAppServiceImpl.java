package com.openhis.web.pharmacymanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.enums.*;
import com.openhis.web.pharmacymanage.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.pharmacymanage.appservice.IMedicationDetailsAppService;
import com.openhis.web.pharmacymanage.mapper.MedicationDetailsMapper;

/**
 * 发药明细 应用实现类
 *
 * @author yuanzs
 * @date 2025/4/14
 */
@Service
public class MedicationDetailsAppServiceImpl implements IMedicationDetailsAppService {

    @Autowired
    private MedicationDetailsMapper medicationDetailsMapper;

    @Autowired
    private IPractitionerService iPractitionerService;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        MedDetailsInitDto initDto = new MedDetailsInitDto();

        // 查询医疗参与者列表
        List<Practitioner> list = iPractitionerService.getList();
        initDto.setPractitionerList(list);
        // 获取状态
        List<MedDetailsInitDto.statusEnumOption> statusEnumOptions = Stream.of(PublicationStatus.values())
            .map(status -> new MedDetailsInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());

        // 结算状态
        initDto.setStatusSettlementOptions(statusEnumOptions)
            // 出院状态
            .setStatusDischargeOptions(statusEnumOptions);

        return R.ok(initDto);

    }

    /**
     * 门诊人员发药明细表
     *
     * @param medDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 门诊人员发药明细表
     */
    @Override
    public R<?> getAmbPractitionerDetailPage(MedDetailsSearchParam medDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<MedDetailsSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(medDetailsSearchParam,
            searchKey, new HashSet<>(Arrays.asList("patient_name", "medication_name", "bus_no")), request);

        Page<MedDetailedAccountPageDto> medDetailsPage = medicationDetailsMapper.selectAmbPractitionerDetailPage(
            new Page<>(pageNo, pageSize), queryWrapper, EncounterClass.AMB.getValue(),
            DispenseStatus.COMPLETED.getValue(), DispenseStatus.REFUNDED.getValue(), 1);

        return R.ok(medDetailsPage);

    }

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
    @Override
    public R<?> getAmbMedicationDispenseDetailPage(MedDetailsSearchParam medDetailsSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<MedDetailsSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(medDetailsSearchParam,
            searchKey, new HashSet<>(Arrays.asList("patient_name", "medication_name", "bus_no")), request);

        Page<MedDetailedAccountPageDto> medDetailsPage = medicationDetailsMapper.selectAmbPractitionerDetailPage(
            new Page<>(pageNo, pageSize), queryWrapper, EncounterClass.AMB.getValue(),
            DispenseStatus.COMPLETED.getValue(), DispenseStatus.REFUNDED.getValue(), 2);

        return R.ok(medDetailsPage);

    }

}
