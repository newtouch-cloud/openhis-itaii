/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.payment.appservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.service.IContractService;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.payment.appservice.IPaymentRecService;
import com.openhis.web.payment.dto.PaymentDetailDto;
import com.openhis.web.payment.dto.PaymentDto;
import com.openhis.web.payment.mapper.PaymentMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 付款应用层Service
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Component
public class PaymentRecServiceImpl implements IPaymentRecService {
    @Resource
    IPaymentReconciliationService paymentReconciliationService;
    @Resource
    IPaymentRecDetailService paymentRecDetailService;
    @Resource
    IContractService contractService;
    @Resource
    AssignSeqUtil assignSeqUtil;
    @Resource
    PaymentMapper paymentMapper;

    @Override
    @Transactional
    public R<?> savePayment(PaymentDto paymentDto) {
        Contract contract = contractService.getOne(new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, paymentDto.getContractNo()));
        if (contract == null) {
            //todo:sjq 返回提示信息
            R.fail();
        }
        Category categoryEnum = Category.getByValue(contract.getCategoryEnum());
        //todo:sjq 医保支付与普通门诊支付多2207操作
        switch (categoryEnum) {
            case SELF:
            case PUBLIC:
                return normalCharge(paymentDto);
            case PROVINCIAL_INSURANCE:
            case MUNICIPAL_INSURANCE:
                return insuranceCharge(paymentDto);
        }
        return null;
    }

    /**
     * 医保支付
     * @param paymentDto
     * @return
     */
    private R<?> insuranceCharge(PaymentDto paymentDto) {
        normalCharge(paymentDto);
        //todo:sjq 调用2207

        return R.ok();
    }

    /**
     * 自费支付
     * @param paymentDto
     * @return
     */
    private R<?> normalCharge(PaymentDto paymentDto) {
        //获取参数
        List<PaymentDetailDto> paymentDetails = paymentDto.getPaymentDetails();
        PaymentReconciliation paymentReconciliation = new PaymentReconciliation();
        BigDecimal sum = BigDecimal.ZERO;
        //数额校验
        ArrayList<PaymentRecDetail> paymentRecDetails = new ArrayList<>();
        for (PaymentDetailDto paymentDetail : paymentDetails) {
            PaymentRecDetail paymentRecDetail = new PaymentRecDetail();
            BeanUtils.copyProperties(paymentDetail,paymentRecDetail);
            paymentRecDetail.setPredecessorId(paymentReconciliation.getId());
            paymentRecDetail.setAccountId(paymentDetail.getAccountId()==null?0:paymentDetail.getAccountId());
            paymentRecDetail.setResultEnum(PaymentResult.PAID.getValue());
            paymentRecDetails.add(paymentRecDetail);
            sum = sum.add(paymentRecDetail.getAmount());
        }
        if(sum.compareTo(paymentReconciliation.getTenderedAmount()) != 0){
            //todo:sjq 返回文字提示：实收金额合计不等于应收金额
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
        }

        BeanUtils.copyProperties(paymentDto, paymentReconciliation);
        paymentReconciliation.setStatusEnum(PaymentStatus.SUCCESS.getValue())
                .setPaymentNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.PAYMENT_NO.getPrefix(), 20))
                .setPaymentEnum(PaymentType.PAY.getValue());

        //保存付款
        paymentReconciliationService.save(paymentReconciliation);

        //保存付款详情
        paymentRecDetailService.saveBatch(paymentRecDetails);
        return R.ok();
    }

    @Override
    public R<?> cancelPayment(PaymentDto paymentDto) {
        //todo:sjq 退费方法
        return null;
    }

    @Override
    public IPage<PaymentDto> getPage(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<PaymentDto> queryWrapper =
                HisQueryUtils.buildQueryWrapper(new PaymentDto(), searchKey,
                        new HashSet<>(Arrays.asList(CommonConstants.FieldName.encouterId,
                                CommonConstants.FieldName.contractNo, CommonConstants.FieldName.paymentNo)),
                        request);

        IPage<PaymentDto> paymentDtoIPage =
                paymentMapper.getPage(new Page<>(pageNo, pageSize), queryWrapper);

        //todo：sjq 1.是否联表查询患者名字，收款员姓名等信息 2.赋值
//        paymentDtoIPage.getRecords().forEach(e -> {
//            // 性别
//            e.setKindEnum_enumText(EnumUtils.getInfoByValue(PaymentKind.class, e.getKindEnum()));
//            // 计算年龄
//            e.setAgeString(AgeCalculatorUtil.getAge(DateUtils.parseDate(e.getBirthDate())));
//        });

        return paymentDtoIPage;
    }

    @Override
    public List<PaymentDetailDto> getDetail(PaymentDto paymentDto) {
        return paymentMapper.getPaymentDetailList(paymentDto.getId());
    }
}
