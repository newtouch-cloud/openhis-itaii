/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.utils.AgeCalculatorUtil;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.basedatamanage.dto.UserAndPractitionerDto;
import com.openhis.web.pharmacymanage.dto.MedDetailedAccountPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.*;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.IChargeReportAppService;
import com.openhis.web.reportmanage.dto.*;
import com.openhis.web.reportmanage.mapper.ChargeReportMapper;

/**
 * 费用明细报表 impl
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Service
public class ChargeReportAppServiceImpl implements IChargeReportAppService {

    @Autowired
    private ChargeReportMapper chargeReportMapper;
    @Resource
    private IPractitionerService practitionerService;

    /**
     * 下拉框
     *
     * @return 下拉框信息
     */
    @Override
    public R<?> chargeReportInit() {
        ChargeReportInitDto initDto = new ChargeReportInitDto();

        // 统计类型
        List<ChargeReportInitDto.commonStatusOption> statisticsTypeOptions = Stream.of(StatisticsType.values())
            .map(statisticsType -> new ChargeReportInitDto.commonStatusOption(statisticsType.getValue(),
                statisticsType.getInfo()))
            .collect(Collectors.toList());
        // 查询制单人列表
        List<Practitioner> applicantList = practitionerService.getList();

        // 项目类型
        List<ChargeReportInitDto.commonStatusOption> clinicalTypeOptions = Stream.of(ChargeItemContext.values())
            .map(ChargeItemContext -> new ChargeReportInitDto.commonStatusOption(ChargeItemContext.getValue(),
                ChargeItemContext.getInfo()))
            .collect(Collectors.toList());

        initDto.setStatisticsTypeOptions(statisticsTypeOptions)
            .setIssuerOptions(applicantList).setPayeeOptions(applicantList).setClinicalTypeOptions(clinicalTypeOptions);
        return R.ok(initDto);
    }

    /**
     * 费用明细列表
     *
     * @param chargeReportSearchParam 费用明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 费用明细
     */
    @Override
    public R<?> getPage(ChargeReportSearchParam chargeReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {
        // 保存统计类型
        String statisticsType = chargeReportSearchParam.getStatisticsType();
        chargeReportSearchParam.setStatisticsType(null);

        // 构建查询条件
        QueryWrapper<ChargeReportSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(chargeReportSearchParam, searchKey,
                new HashSet<>(Arrays.asList(CommonConstants.FieldName.YbCode, CommonConstants.FieldName.BusNo,
                    CommonConstants.FieldName.Name, CommonConstants.FieldName.ClinicalName,
                    CommonConstants.FieldName.ClinicalNo)),
                request);
        queryWrapper.orderByAsc(CommonConstants.FieldName.BusNo);


        // 最终返回结果
        Page<ChargeReportPageDto> chargeReportList;
        // 判断统计类型
        if (Objects.equals(statisticsType, StatisticsType.OUTPATIENT_REVENUE.getCode())) {
            // 门诊整体收入明细
            chargeReportList = chargeReportMapper.selectRevenueReportPage(new Page<>(pageNo, pageSize),queryWrapper,
                PaymentStatus.SUCCESS.getValue(), PaymentStatus.REFUND_ALL.getValue(),
                ChargeItemContext.MEDICATION.getValue(), ChargeItemContext.DEVICE.getValue(),
                ChargeItemContext.ACTIVITY.getValue(), ChargeItemContext.REGISTER.getValue(), 2);
        } else if (Objects.equals(statisticsType, StatisticsType.OUTPATIENT_YB_REVENUE.getCode())) {
            // 门诊医保收入明细
            chargeReportList = chargeReportMapper.selectRevenueReportPage(new Page<>(pageNo, pageSize),queryWrapper,
                PaymentStatus.SUCCESS.getValue(), PaymentStatus.REFUND_ALL.getValue(),
                ChargeItemContext.MEDICATION.getValue(), ChargeItemContext.DEVICE.getValue(),
                ChargeItemContext.ACTIVITY.getValue(), ChargeItemContext.REGISTER.getValue(), 1);

        } else if (Objects.equals(statisticsType, StatisticsType.OUTPATIENT_SELF_REVENUE.getCode())) {
            // 门诊自费收入明细
            chargeReportList = chargeReportMapper.selectRevenueReportPage(new Page<>(pageNo, pageSize),queryWrapper,
                PaymentStatus.SUCCESS.getValue(), PaymentStatus.REFUND_ALL.getValue(),
                ChargeItemContext.MEDICATION.getValue(), ChargeItemContext.DEVICE.getValue(),
                ChargeItemContext.ACTIVITY.getValue(), ChargeItemContext.REGISTER.getValue(), 0);
        } else if (Objects.equals(statisticsType, StatisticsType.OUTPATIENT_REFUND.getCode())) {
            // 门诊退费明细
            chargeReportList = chargeReportMapper.selectRevenueReportPage(new Page<>(pageNo, pageSize),queryWrapper,
                PaymentStatus.SUCCESS.getValue(), PaymentStatus.REFUND_ALL.getValue(),
                ChargeItemContext.MEDICATION.getValue(), ChargeItemContext.DEVICE.getValue(),
                ChargeItemContext.ACTIVITY.getValue(), ChargeItemContext.REGISTER.getValue(), 3);

        } else {
            return R.fail(PromptMsgConstant.Report.M00002);
        }

        // 年龄 性别 医保等级 状态转换
        chargeReportList.getRecords().forEach(infoDto -> {
            infoDto.setAge(AgeCalculatorUtil.getAge(infoDto.getBirthDate()));
            infoDto
                .setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, infoDto.getGenderEnum()));
            infoDto.setChrgitmLv_enumText(EnumUtils.getInfoByValue(InsuranceLevel.class, infoDto.getChrgitmLv()));
        });

        // 提取所有数据
        List<ChargeReportPageDto> content = chargeReportList.getRecords();
        // 按 busNo 分组，并合计 totalPrice
        Map<String, BigDecimal> subtotalAmount = content.stream()
            .collect(Collectors.groupingBy(
                ChargeReportPageDto::getBusNo,
                    Collectors.reducing(
                        BigDecimal.ZERO, // 初始值
                        dto -> dto.getTotalPrice() != null ? dto.getTotalPrice() : BigDecimal.ZERO,
                        BigDecimal::add) // 累加操作
                )
            );

        // 更新每条数据的 Price 字段
        for (ChargeReportPageDto dto : content) {
            String busNo = dto.getBusNo();
            dto.setSubtotalAmount(subtotalAmount.get(busNo));
        }
        chargeReportList.setRecords(content);

        return R.ok(chargeReportList);
    }
}
