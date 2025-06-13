/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.appservice.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.openhis.yb.dto.Clinic2208UnSetlInfoOutput;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.redis.RedisCache;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.*;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.enums.ybenums.YbMdtrtCertType;
import com.openhis.common.enums.ybenums.YbPayment;
import com.openhis.common.enums.ybenums.YbPsnSetlWay;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.model.PaymentRecDetailDto;
import com.openhis.financial.model.PaymentedItemModel;
import com.openhis.financial.model.PrePaymentResult;
import com.openhis.financial.model.PrePaymentResultModel;
import com.openhis.financial.service.IContractService;
import com.openhis.financial.service.IPaymentNoticeService;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.medication.service.IMedicationRequestService;
import com.openhis.web.chargemanage.appservice.impl.OutpatientRegistrationAppServiceImpl;
import com.openhis.web.chargemanage.dto.*;
import com.openhis.web.chargemanage.mapper.OutpatientRegistrationAppMapper;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdvicePriceDto;
import com.openhis.web.paymentmanage.appservice.IPaymentRecService;
import com.openhis.web.paymentmanage.dto.*;
import com.openhis.web.paymentmanage.mapper.PaymentMapper;
import com.openhis.web.personalization.dto.ActivityDeviceDto;
import com.openhis.workflow.domain.DeviceRequest;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IDeviceRequestService;
import com.openhis.workflow.service.IServiceRequestService;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.domain.ClinicSettle;
import com.openhis.yb.dto.Clinic2206OrderOutput;
import com.openhis.yb.model.OutpatientRegistrationModel;
import com.openhis.yb.service.IClinicSettleService;
import com.openhis.yb.service.IRegService;
import com.openhis.yb.service.YbManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 付款应用层Service
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Component
@Slf4j
public class PaymentRecServiceImpl implements IPaymentRecService {

    Logger logger = LoggerFactory.getLogger(PaymentRecServiceImpl.class);
    @Resource
    private IPaymentNoticeService paymentNoticeService;
    @Resource
    private IAccountService iAccountService;
    @Resource
    private IEncounterService iEncounterService;
    @Resource
    private IInvoiceService iInvoiceService;
    @Resource
    private IPaymentReconciliationService paymentReconciliationService;
    @Resource
    private IPaymentRecDetailService paymentRecDetailService;
    @Resource
    private IContractService contractService;
    @Resource
    private IChargeItemService chargeItemService;
    @Resource
    private AssignSeqUtil assignSeqUtil;
    @Resource
    private PaymentMapper paymentMapper;
    @Resource
    private IMedicationDispenseService medicationDispenseService;
    @Resource
    private IDeviceDispenseService deviceDispenseService;
    @Resource
    private IMedicationRequestService medicationRequestService;
    @Resource
    private IDeviceRequestService deviceRequestService;
    @Resource
    private IServiceRequestService serviceRequestService;
    @Resource
    private IClinicSettleService clinicSettleService;
    @Resource
    private OutpatientRegistrationAppMapper outpatientRegistrationAppMapper;
    @Resource
    private IRegService iRegService;
    @Resource
    private IPatientService iPatientService;
    @Autowired
    private IPractitionerService iPractitionerService;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private IEncounterParticipantService iEncounterParticipantService;
    @Autowired
    private IChargeItemService iChargeItemService;
    @Autowired
    private OutpatientRegistrationAppServiceImpl outpatientRegistrationAppService;
    @Autowired
    private IServiceRequestService iServiceRequestService;
    @Autowired
    private IDoctorStationAdviceAppService iDoctorStationAdviceAppService;
    @Autowired
    private IEncounterDiagnosisService iEncounterDiagnosisService;
    @Autowired
    private IPaymentReconciliationService iPaymentReconciliationService;
    @Autowired
    private IPaymentRecDetailService iPaymentRecDetailService;
    @Autowired
    private YbManager ybManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public R<?> prePayment(PrePaymentDto prePaymentDto) {
        logger.info("预结算：参数：" + JSON.toJSONString(prePaymentDto));
        // 查收费项
        List<ChargeItem> chargeItemList = getChargeItems(prePaymentDto.getChargeItemIds());
        if (chargeItemList.isEmpty()) {
            return R.fail("未选择收费项");
        }

        // 此次chargeItem的就诊诊断id集合
        List<Long> diagIdList =
            chargeItemList.stream().map(ChargeItem::getEncounterDiagnosisId).collect(Collectors.toList());
        if(diagIdList.isEmpty()){
            throw new ServiceException("收费项的就诊诊断查询为空");
        }
        // 此次就诊的医疗类型列表
        List<EncounterDiagnosis> diagList = iEncounterDiagnosisService.getDiagnosisList(diagIdList);
        if(diagList.isEmpty()){
            throw new ServiceException("就诊诊断查询为空，错误信息：EncounterDiagnosis");
        }
        List<String> medTypeList =
            diagList.stream().map(EncounterDiagnosis::getMedTypeCode).distinct().collect(Collectors.toList());
        if (medTypeList.size() > 1) {
            return R.fail("收费项的就诊类型未统一");
        }

        // 获取所有的账户id
        List<Long> accountIdList = chargeItemList.stream().map(ChargeItem::getAccountId).collect(Collectors.toList());
        // account去重
        List<Long> distinctAccountIdList = accountIdList.stream().distinct().collect(Collectors.toList());
        List<Account> accountList = iAccountService.list(new LambdaQueryWrapper<Account>()
            .in(Account::getId, distinctAccountIdList).eq(Account::getEncounterId, prePaymentDto.getEncounterId()));
        if (accountList.size() != distinctAccountIdList.size()) {
            throw new ServiceException("未查询到账户信息");
        }

        // 账户id，对应的账单列表
        Map<Long, List<ChargeItem>> chargeItemMapByAccountId =
            chargeItemList.stream().collect(Collectors.groupingBy(ChargeItem::getAccountId));
        // 查询合同信息
        List<Contract> contractList = contractService.list();

        // 根据不同的账户支付
        List<PrePaymentResult> preSettleResultList = new ArrayList<>();
        for (Account account : accountList) {
            Contract settleContract = null;
            if (account != null && account.getContractNo() != null && contractList != null) {
                settleContract = contractList.stream().filter(Objects::nonNull)
                    .filter(e -> account.getContractNo().equals(e.getBusNo())).findFirst() // 或 findAny，根据业务需求
                    .orElse(null);
            } // 根据accountid,获取结算合同
            if (settleContract != null) {
                List<ChargeItem> chargeItemsForPreSettle = chargeItemMapByAccountId.get(account.getId());
                List<PaymentedItemModel> prePaymentedItems = new ArrayList<>();
                for (ChargeItem chargeItem : chargeItemsForPreSettle) {
                    // TODO:后续添加可调价逻辑，当前都用子项目价格进行结算
                    // 处理套餐项目，如果ChargeItem有子项，并且此项目是不可调价项目要被拆出来
                    // 这是不拆的逻辑，如果TODO完成，会多一种情况进入这个逻辑
                    if (StringUtils.isEmpty(chargeItem.getChildrenJson()) || "0000".equals(account.getContractNo())
                        || "0".equals(
                            SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH))) {
                        PaymentedItemModel piModel = ConverterToPaymenItemModel(diagList, chargeItem);
                        prePaymentedItems.add(piModel);
                    } else {
                        try {
                            List<ChargeItem> childrenList =
                                JSON.parseArray(chargeItem.getChildrenJson(), ChargeItem.class);
                            for (ChargeItem child : childrenList) {
                                PaymentedItemModel piModel = ConverterToPaymenItemModel(diagList, child);
                                prePaymentedItems.add(piModel);
                            }
                        } catch (Exception ex) {
                            throw new ServiceException("套餐项目反序列化失败");
                        }
                    }
                }

                // 调用核心预结算方法
                PrePaymentResult prePaymentResult = iPaymentReconciliationService.prePayment(
                    YbMdtrtCertType.getByValue(prePaymentDto.getYbMdtrtCertType()), prePaymentDto.getBusiCardInfo(),
                    settleContract.getBusNo(), prePaymentedItems);
                // 把多个account的预结算结果加入preSettleResultList
                if (prePaymentResult != null) {
                    prePaymentResult.setAccountId(account.getId());
                    preSettleResultList.add(prePaymentResult);
                    // redisCache.setCacheObject("pre-settle:pre-settle-"+account.getNo(),prePaymentResult,180,
                    // TimeUnit.SECONDS);
                }
            } else {
                throw new ServiceException("收费项找不到相应的合同");
            }
        }

        // 定义应收金额
        BigDecimal amount = BigDecimal.ZERO;
        for (PrePaymentResult prePaymentResult : preSettleResultList) {
            amount = amount.add(prePaymentResult.getMedfeeSumamt());
        }
        // TODO 通过多次预结算，得到最后的预结算账单：finalPrePaymenResult
        // PrePaymentResultModel finalResult = calcFinalResult(preSettleResultList);
        // 创建草稿状态的付款实体
        // 处理时间
        Date setlTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(setlTime);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        Date futureTime = calendar.getTime();
        // 生成流水号
        String paymentNo = assignSeqUtil.getSeqByDay(AssignSeqEnum.PAYMENT_NO.getPrefix(), 20);

        // 新增支付信息
        PaymentReconciliation payment = new PaymentReconciliation();
        payment.setStatusEnum(PaymentStatus.DRAFT.getValue()).setPaymentNo(paymentNo).setYbSettleIds("")
            .setPaymentEnum(PaymentType.PAY.getValue()).setEntererId(SecurityUtils.getLoginUser().getPractitionerId())
            .setPaymentReconciliationId(prePaymentDto.getPatientId()).setPatientId(prePaymentDto.getPatientId())
            .setKindEnum(PaymentKind.OUTPATIENT_CLINIC.getValue()).setPractitionerId(prePaymentDto.getEntererId())
            .setOutcomeEnum(PaymentOutcome.PARTIAL.getCode()).setLocationId(-99l).setExpirationDate(futureTime)
            .setBillDate(setlTime).setPrintCount(0)
            .setChargeItemIds(
                prePaymentDto.getChargeItemIds().stream().map(String::valueOf).collect(Collectors.joining(",")))
            .setTenderedAmount(amount).setDisplayAmount(amount).setReturnedAmount(new BigDecimal("0.0"))
            .setEncounterId(prePaymentDto.getEncounterId());
        // 保存付款信息
        paymentReconciliationService.save(payment);
        // 保存付款详情
        List<PaymentRecDetail> paymentRecDetails = this.savePaymentDetail(preSettleResultList, payment);
        List<PaymentRecDetailDto> paymentRecDetailDtoList = this.getPaymentRecDetailDtoList(paymentRecDetails);

        PrePaymentResultModel prePaymentResultModel = new PrePaymentResultModel();
        prePaymentResultModel.setDetails(paymentRecDetailDtoList).setPaymentReconciliation(payment)
            .setPaymentId(String.valueOf(payment.getId()));

        return R.ok(prePaymentResultModel);
    }

    /**
     * 转换实体给页面显示
     * 
     * @param paymentRecDetails
     * @return
     */
    private List<PaymentRecDetailDto> getPaymentRecDetailDtoList(List<PaymentRecDetail> paymentRecDetails) {
        List<PaymentRecDetailDto> list = new ArrayList<>();
        PaymentRecDetailDto paymentRecDetailDto;
        for (PaymentRecDetail paymentRecDetail : paymentRecDetails) {
            YbPayment ybPayment = YbPayment.getByValue(paymentRecDetail.getPayEnum());

            paymentRecDetailDto = new PaymentRecDetailDto();
            paymentRecDetailDto.setPayEnum(paymentRecDetail.getPayEnum()).setId(paymentRecDetail.getId())
                .setAccountId(paymentRecDetail.getAccountId()).setAmount(paymentRecDetail.getAmount())
                .setPayEnumText(ybPayment == null ? "" : ybPayment.getInfo());
            list.add(paymentRecDetailDto);
        }
        return list;
    }

    /**
     * 付款/结算
     *
     * @param paymentDto 入参
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> savePayment(PaymentDto paymentDto) {
        if (paymentDto.getChargeItemIds().isEmpty()) {
            return R.fail("未选择收费项");
        }
        // 查找付款实例
        PaymentReconciliation paymentReconciliation = paymentReconciliationService.getById(paymentDto.getId());
        if (paymentReconciliation == null) {
            return R.fail("查无此付款信息");
        }
        if (!PaymentStatus.DRAFT.getValue().equals(paymentReconciliation.getStatusEnum())) {
            return R.fail("该账单不是待支付账单");
        }
        // 查找对应的收费详情,根据批次号查询
        List<PaymentRecDetail> paymentRecDetails =
            paymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>()
                .eq(PaymentRecDetail::getReconciliationId, paymentReconciliation.getId()));
        if (!paymentDto.getChargeItemIds().stream().map(String::valueOf).collect(Collectors.joining(","))
            .equals(paymentReconciliation.getChargeItemIds())) {
            return R.fail("收费项目变动，请重新预结算");
        }
        // 校验金额
        BigDecimal sum = BigDecimal.ZERO;
        for (PaymentRecDetail paymentRecDetail : paymentRecDetails) {
            if (paymentRecDetail.getPayEnum().equals(YbPayment.SELF_CASH_PAY.getValue())) {
                sum = sum.add(paymentRecDetail.getAmount());
            }
        }
        BigDecimal chargeAmount = BigDecimal.ZERO;
        for (PaymentDetailDto paymentDetail : paymentDto.getPaymentDetails()) {
            chargeAmount = chargeAmount.add(paymentDetail.getAmount());
        }
        if (sum.compareTo(chargeAmount) != 0) {
            throw new ServiceException("交款金额与预结算金额不相等");
        }

        // 前端传过来的收费项
        List<ChargeItem> chargeItemList = getChargeItems(paymentDto.getChargeItemIds());
        if (chargeItemList.isEmpty()) {
            return R.fail("未选择收费项");
        }
        if (chargeItemList.stream().map(ChargeItem::getStatusEnum)
            .anyMatch(x -> x.equals(ChargeItemStatus.BILLED.getValue()))) {
            return R.fail("已收费账单，请误重复提交");
        }
        // 分别获取各个请求id列表
        List<Long> medReqIdList = new ArrayList<>();
        List<Long> devReqIdList = new ArrayList<>();
        Integer minpacuntDrugTracCnt = 0;
        Integer mcsTracCnt = 0;
        chargeItemList.forEach(item -> {
            switch (item.getServiceTable()) {
                case CommonConstants.TableName.MED_MEDICATION_REQUEST -> medReqIdList.add(item.getServiceId());
                case CommonConstants.TableName.WOR_DEVICE_REQUEST -> devReqIdList.add(item.getServiceId());
            }
        });
        if (!medReqIdList.isEmpty()) {
            List<MedicationRequest> medicationRequestList = medicationRequestService.list(
                new LambdaQueryWrapper<MedicationRequest>().in(MedicationRequest::getId, medReqIdList));
            if (!medicationRequestList.isEmpty()) {
                minpacuntDrugTracCnt = medicationRequestList.stream().mapToInt(MedicationRequest::getQuantity).sum();
            }
        }
        if (!devReqIdList.isEmpty()) {
            List<DeviceRequest> deviceRequestList = deviceRequestService
                .list(new LambdaQueryWrapper<DeviceRequest>().in(DeviceRequest::getId, devReqIdList));
            if (!deviceRequestList.isEmpty()) {
                mcsTracCnt = deviceRequestList.stream().mapToInt(DeviceRequest::getQuantity).sum();
            }
        }

        Map<String, List<PaymentRecDetail>> payTransNoMap =
            paymentRecDetails.stream().collect(Collectors.groupingBy(PaymentRecDetail::getPayTransNo));

        com.openhis.financial.model.PaymentResult paymentResult;
        List<com.openhis.financial.model.PaymentResult> paymentResultList = new ArrayList<>();
        for (Map.Entry<String, List<PaymentRecDetail>> stringListEntry : payTransNoMap.entrySet()) {
            // paymentResult = new PaymentResult();
            paymentResult = iPaymentReconciliationService.settle(stringListEntry.getKey(),
                YbMdtrtCertType.getByValue(paymentDto.getYbMdtrtCertType()), paymentDto.getBusiCardInfo(),
                minpacuntDrugTracCnt, mcsTracCnt);
            paymentResult.setAccountId(stringListEntry.getValue().get(0).getAccountId());
            for (PaymentRecDetail paymentRecDetail : stringListEntry.getValue()) {
                paymentRecDetail.setPayTransText(paymentResult.getSetlId());//医保结算赋值结算id；用于医保对账清算时使用
            }
            if (paymentResult != null) {
                paymentResultList.add(paymentResult);
            }
        }

        List<String> collect = paymentResultList.stream().map(com.openhis.financial.model.PaymentResult::getSetlId)
            .collect(Collectors.toList());

        // 更改付款状态
        logger.info("更新付款状态：payment：" + JSON.toJSONString(paymentReconciliation));
        iPaymentReconciliationService.updatePaymentStatusAndSettleIdsById(paymentReconciliation.getId(),
            PaymentStatus.SUCCESS, collect);
        iPaymentRecDetailService.updateResultByPaymentId(paymentReconciliation.getId(), PaymentResult.PAID);

        //更改付款详情的信息
        iPaymentRecDetailService.updateBatchById(paymentRecDetails);

        // 记录卡余额等信息
        com.openhis.financial.model.PaymentResult minAmountResult = null;
        for (com.openhis.financial.model.PaymentResult result : paymentResultList) {
            if (result.getBalc() == null) {
                continue;
            }
            if (minAmountResult == null || result.getBalc().compareTo(minAmountResult.getBalc()) < 0) {
                minAmountResult = result;
            }
        }

        if (minAmountResult != null) {
            // 賬戶餘額
            BigDecimal balc = minAmountResult.getBalc();
            if (balc != null) {
                PaymentRecDetail paymentRecDetail = new PaymentRecDetail();
                // paymentRecDetail.setReconciliationId(paymentReconciliation.getId())
                // .setPayEnum(YbPayment.YB_JZ_FUND_AMOUNT.getValue())
                // .setPayLevelEnum(YbPayment.YB_JZ_FUND_AMOUNT.getLevel()).setAmount(balc)
                // .setPayTransNo(minAmountResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                // .setAccountId(minAmountResult.getAccountId());
                paymentRecDetail.setReconciliationId(paymentReconciliation.getId())
                    .setPayEnum(YbPayment.BALC.getValue()).setPayLevelEnum(YbPayment.BALC.getLevel()).setAmount(balc)
                    .setPayTransNo(minAmountResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(minAmountResult.getAccountId()).setPayTransText(minAmountResult.getSetlId());
                iPaymentRecDetailService.save(paymentRecDetail);
            }
        }

        // 更改chargeItem的状态
        List<Long> chargeItemIdList = chargeItemList.stream().map(ChargeItem::getId).collect(Collectors.toList());
        chargeItemService.updatePaymentStatus(chargeItemIdList, ChargeItemStatus.BILLED.getValue());
        // 生成发票信息
        Invoice invoice = new Invoice();
        invoice.setPatientId(paymentReconciliation.getPatientId()).setStatusEnum(InvoiceStatus.DRAFT)
            .setReconciliationId(paymentReconciliation.getId()).setTypeCode(InvoiceType.ISSUING_INVOICES.getValue())
            .setChargeItemIds(paymentReconciliation.getChargeItemIds());
        iInvoiceService.save(invoice);

        // 获取收费项对应的项目请求id
        List<Long> medicationRequestIdList = new ArrayList<>();
        List<Long> deviceRequestIdList = new ArrayList<>();
        List<Long> serviceRequestIdList = new ArrayList<>();

        chargeItemList.forEach(item -> {
            switch (item.getServiceTable()) {
                case CommonConstants.TableName.MED_MEDICATION_REQUEST -> medicationRequestIdList
                    .add(item.getServiceId());
                case CommonConstants.TableName.WOR_DEVICE_REQUEST -> deviceRequestIdList.add(item.getServiceId());
                case CommonConstants.TableName.WOR_SERVICE_REQUEST -> serviceRequestIdList.add(item.getServiceId());
            }
        });

        if (!medicationRequestIdList.isEmpty()) {
            // 更新药品发放状态为待配药
            medicationDispenseService.updatePreparationDispenseStatus(medicationRequestIdList);
        }
        if (!deviceRequestIdList.isEmpty()) {
            // 更新耗材发放状态为待配药
            deviceDispenseService.updatePreparationDispenseStatus(deviceRequestIdList);
        }
        if (!serviceRequestIdList.isEmpty()) {
            // 更新服务请求状态为待执行
            serviceRequestService.updatePreparationStatus(serviceRequestIdList);
        }
        logger.info("收费成功：payment：" + JSON.toJSONString(paymentReconciliation));
        // throw new ServiceException("");
        return R.ok(paymentReconciliation,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"收费"}));
    }

    /**
     * 退费
     *
     * @param cancelPaymentDto 入参
     * @return 结果
     */
    @Override
    public R<?> cancelPayment(CancelPaymentDto cancelPaymentDto) {
        // 当前登录用户
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        // 查找付款实例
        PaymentReconciliation paymentReconciliation = paymentReconciliationService.getById(cancelPaymentDto.getId());
        if (paymentReconciliation == null) {
            throw new ServiceException("未查询到结算信息");
        }
        // 原结算的ChargeItemIdList
        List<Long> chargeItemIdList = Arrays.asList(paymentReconciliation.getChargeItemIds().split(",")).stream()
            .filter(Objects::nonNull).map(Long::parseLong).collect(Collectors.toList());
        if (chargeItemIdList.isEmpty()) {
            throw new ServiceException("未查询到收费项信息");
        }
        // 原结算的chargeItem集合
        List<ChargeItem> chargeItems = getChargeItems(chargeItemIdList);
        if (chargeItems.isEmpty()) {
            throw new ServiceException("未查询到收费项信息");
        }
        if (chargeItems.stream().map(ChargeItem::getStatusEnum)
            .anyMatch(x -> x.equals(ChargeItemStatus.REFUNDED.getValue()))) {
            throw new ServiceException("请勿重复退费");
        }
        // 查找对应的收费详情
        List<PaymentRecDetail> PaymentRecDetails =
            paymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>()
                .eq(PaymentRecDetail::getReconciliationId, cancelPaymentDto.getId()));
        // 查询账户信息
        // Account account = iAccountService.getById(paymentReconciliation.getAccountId());
        if (PaymentRecDetails.isEmpty()) {
            throw new ServiceException("未查询到结算信息");
        }
        // 新的chargeitem
        String chargeItemIds = StringUtil.join(cancelPaymentDto.getChargeItemIds().toArray(), ",");
        List<ChargeItem> chargeItemList;
        if (chargeItemIds.isEmpty()) {
            chargeItemList = new ArrayList<>();
        } else {
            chargeItemList = chargeItemService
                .list(new LambdaQueryWrapper<ChargeItem>().in(ChargeItem::getId, cancelPaymentDto.getChargeItemIds()));
        }

        // 全退
        String ybSettleIds = paymentReconciliation.getYbSettleIds();
        PaymentReconciliation unPaymentReconciliation = normalUnCharge(paymentReconciliation, PaymentRecDetails,null);
        if (!StringUtils.isEmpty(ybSettleIds)) {
            // 医保结算信息
            List<String> ybSettleIdList = Arrays.asList(ybSettleIds.split(","));
            if (!ybSettleIdList.isEmpty()) {
                // 医保反结
                List<ClinicSettle> clinicSettleList = clinicSettleService
                    .list(new LambdaQueryWrapper<ClinicSettle>().in(ClinicSettle::getSetlId, ybSettleIdList));
                StringBuilder stringBuilder = new StringBuilder();
                for (ClinicSettle clinicSettle : clinicSettleList) {
                    R<?> r = ybManager.unSettle(clinicSettle.getSetlId());
                    if (r.getCode() == 200) {
                        //获取医保反结算后的2208输出参数
                        Clinic2208UnSetlInfoOutput clinic2208UnSetlInfoOutput = null;
                        if (Clinic2208UnSetlInfoOutput.class.isAssignableFrom(r.getData().getClass())) {
                            clinic2208UnSetlInfoOutput = (Clinic2208UnSetlInfoOutput)r.getData();
                            stringBuilder.append(clinic2208UnSetlInfoOutput.getSetlId()).append(",");
                            //对payment详情的结算id进行处理
                            List<PaymentRecDetail> paymentRecDetailList = iPaymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId, unPaymentReconciliation).eq(PaymentRecDetail::getPayTransText, clinicSettle.getSetlId()));
                            if(!paymentRecDetailList.isEmpty()){
                                for (PaymentRecDetail paymentRecDetail : paymentRecDetailList) {
                                    paymentRecDetail.setPayTransText(clinic2208UnSetlInfoOutput.getSetlId());
                                }
                                iPaymentRecDetailService.updateBatchById(paymentRecDetailList);
                            }
                        }
                    }
                }
                // 删除最后一个逗号
                if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                //回填payment的settleId字段
                unPaymentReconciliation.setYbSettleIds(stringBuilder.toString());
                iPaymentReconciliationService.updateById(unPaymentReconciliation);
            }
        }

        if (!chargeItemList.isEmpty()) {
            //
            PrePaymentDto prePaymentDto = new PrePaymentDto();
            prePaymentDto.setEncounterId(paymentReconciliation.getEncounterId()).setEntererId(practitionerId)
                .setPatientId(paymentReconciliation.getPatientId())
                .setYbMdtrtCertType(cancelPaymentDto.getYbMdtrtCertType())
                .setChargeItemIds(cancelPaymentDto.getChargeItemIds())
                .setBusiCardInfo(cancelPaymentDto.getBusiCardInfo());

            R<?> preResult = prePayment(prePaymentDto);

            if (200 != preResult.getCode()) {
                throw new ServiceException("重新预结算失败请联系工程师");
            }

            PrePaymentResultModel prePaymentResultModel =
                JSON.parseObject(JSON.toJSONString(preResult.getData()), PrePaymentResultModel.class);
            // 新的应收
            BigDecimal cashPay = BigDecimal.ZERO;
            for (PaymentRecDetailDto paymentRecDetail : prePaymentResultModel.getDetails()) {
                if (paymentRecDetail.getPayEnum().equals(YbPayment.SELF_CASH_PAY.getValue())) {
                    cashPay = cashPay.add(paymentRecDetail.getAmount());
                }
            }

            // 已付金额
            BigDecimal payAmount = BigDecimal.ZERO;
            for (PaymentRecDetail paymentRecDetail : PaymentRecDetails) {
                if (YbPayment.SELF_CASH_PAY.getValue().equals(paymentRecDetail.getPayEnum())) {
                    payAmount = payAmount.add(paymentRecDetail.getAmount());
                }
            }

            // 拼接重新结算参数
            PaymentDto paymentDto = new PaymentDto();
            ArrayList<PaymentDetailDto> paymentDetailDtoList = new ArrayList<>();
            paymentDetailDtoList.add(new PaymentDetailDto().setPayEnum(YbPayment.SELF_CASH_PAY.getValue())
                .setAmount(cashPay).setChargeAmount(cashPay).setReturnAmount(new BigDecimal("0.0")));
            paymentDto.setEncounterId(paymentReconciliation.getEncounterId())
                .setChargeItemIds(chargeItemList.stream().map(ChargeItem::getId).collect(Collectors.toList()))
                .setPaymentDetails(paymentDetailDtoList).setId(Long.parseLong(prePaymentResultModel.getPaymentId()));

            R<?> settleResult = savePayment(paymentDto);

            if (200 != settleResult.getCode()) {
                throw new ServiceException("重新结算失败请联系工程师");
            }

            // 获取新的付款参数表
            PaymentReconciliation payment =
                JSON.parseObject(JSON.toJSONString(settleResult.getData()), PaymentReconciliation.class);
            // 获取实付金额
            List<PaymentRecDetail> paymentRecDetail = paymentRecDetailService.list(
                new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId, payment.getId())
                    .eq(PaymentRecDetail::getPayEnum, YbPayment.SELF_CASH_PAY.getValue()));

            BigDecimal chargedPay = BigDecimal.ZERO;
            for (PaymentRecDetailDto paymentRecDet : prePaymentResultModel.getDetails()) {
                if (paymentRecDet.getPayEnum().equals(YbPayment.SELF_CASH_PAY.getValue())) {
                    chargedPay = chargedPay.add(paymentRecDet.getAmount());
                }
            }
            logger.info(
                "部分退款：原payment：" + JSON.toJSONString(paymentReconciliation) + "新payment：" + JSON.toJSONString(payment));
            if (chargedPay.compareTo(payAmount) >= 0) {
                return R.ok("请患者补交" + chargedPay.subtract(payAmount).setScale(1, RoundingMode.HALF_UP) + "元");
            } else {
                return R.ok("请返还患者" + payAmount.subtract(chargedPay).setScale(1, RoundingMode.HALF_UP) + "元");
            }
        }
        // 更新收费状态：已退费
        chargeItemService.updatePaymentStatus(chargeItemIdList, ChargeItemStatus.REFUNDED.getValue());
        return R.ok(unPaymentReconciliation, "退款成功");
    }

    /**
     * 单纯做 payment*-1
     * 
     * @param cancelPaymentDto 入参
     * @return
     */
    @Override
    public R<?> cancelRegPayment(CancelPaymentDto cancelPaymentDto) {
        // 获取付费id
        Long paymentId = cancelPaymentDto.getId();
        PaymentReconciliation paymentReconciliation = iPaymentReconciliationService.getById(paymentId);
        if (paymentReconciliation == null) {
            throw new ServiceException("查询不到付款记录");
        }
        List<PaymentRecDetail> paymentRecDetails = iPaymentRecDetailService
            .list(new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId, paymentId));
        if (paymentRecDetails.isEmpty()) {
            throw new ServiceException("查询不到付款记录");
        }

        PaymentReconciliation unPaymentReconciliation = normalUnCharge(paymentReconciliation, paymentRecDetails,cancelPaymentDto.getSetlId());

        return R.ok(unPaymentReconciliation);
    }

    /**
     * 分页查询
     *
     * @param searchKey 查询条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 结果
     */
    @Override
    public IPage<PaymentVO> getPage(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<PaymentVO> queryWrapper = HisQueryUtils.buildQueryWrapper(new PaymentVO(), searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.encounterId, CommonConstants.FieldName.contractNo,
                CommonConstants.FieldName.paymentNo)), request);

        IPage<PaymentVO> paymentDtoIPage = paymentMapper.getPage(new Page<>(pageNo, pageSize), queryWrapper);

        for (PaymentVO record : paymentDtoIPage.getRecords()) {
            record.setPaymentId(record.getId().toString());
            record.setEncounterIdStr(record.getEncounterId().toString());
        }

        // todo：sjq 1.是否联表查询患者名字，收款员姓名等信息 2.赋值
        // paymentDtoIPage.getRecords().forEach(e -> {
        // // 性别
        // e.setKindEnum_enumText(EnumUtils.getInfoByValue(PaymentKind.class, e.getKindEnum()));
        // // 计算年龄
        // e.setAgeString(AgeCalculatorUtil.getAge(DateUtils.parseDate(e.getBirthDate())));
        // });

        return paymentDtoIPage;
    }

    /**
     * 查找收费详情（明细）
     *
     * @param paymentDto 入参
     * @return 结果
     */
    @Override
    public List<PaymentDetailDto> getDetail(PaymentDto paymentDto) {
        return paymentMapper.getPaymentDetailList(paymentDto.getId());
    }

    /**
     * 常规退费
     *
     * @param paymentReconciliation 付款实体
     * @param paymentRecDetails 付款详情
     * @return 结果
     */
    private PaymentReconciliation normalUnCharge(PaymentReconciliation paymentReconciliation,
        List<PaymentRecDetail> paymentRecDetails, String setlIds) {

        // 获取原ID
        Long id = paymentReconciliation.getId();
        // SJQ:2025.04.01 确认locationId暂不做处理，location表只记录库房药房信息
        paymentReconciliation.setId(null).setStatusEnum(PaymentStatus.REFUND_ALL.getValue())
            .setPaymentEnum(PaymentType.UN_PAY.getValue()).setRelationId(id)
            .setEntererId(SecurityUtils.getLoginUser().getPractitionerId()).setBillDate(new Date())
            .setTenderedAmount(paymentReconciliation.getTenderedAmount().negate())
            .setReturnedAmount(paymentReconciliation.getReturnedAmount().negate())
            .setDisplayAmount(paymentReconciliation.getDisplayAmount().negate());
        if(setlIds!=null){
            paymentReconciliation.setYbSettleIds(setlIds);
        }
        paymentReconciliationService.save(paymentReconciliation);
        // 新增详情信息
        PaymentRecDetail newPaymentRecDetail;
        List<PaymentRecDetail> addDetailList = new ArrayList<>();
        for (PaymentRecDetail paymentRecDetail : paymentRecDetails) {
            newPaymentRecDetail = new PaymentRecDetail();
            BeanUtils.copyProperties(paymentRecDetail, newPaymentRecDetail);
            newPaymentRecDetail.setId(null);
            newPaymentRecDetail.setReconciliationId(paymentReconciliation.getId());
            newPaymentRecDetail.setPredecessorId(paymentRecDetail.getId());
            newPaymentRecDetail.setResultEnum(PaymentResult.PAID.getValue());
            newPaymentRecDetail.setAmount(paymentRecDetail.getAmount().multiply(new BigDecimal("-1")));
            newPaymentRecDetail.setDeleteFlag(DelFlag.NO.getCode());
            if(setlIds!=null){
                newPaymentRecDetail.setPayTransText(setlIds);
            }
            addDetailList.add(newPaymentRecDetail);
        }

        paymentRecDetailService.saveBatch(addDetailList);

        // 修改详情信息
        List<PaymentRecDetail> list = new ArrayList<>();
        for (PaymentRecDetail item : paymentRecDetails) {
            item.setResultEnum(1);
            list.add(item);
        }
        paymentRecDetailService.updateBatchById(list);
        logger.info("退款payment：" + JSON.toJSONString(paymentReconciliation));
        return paymentReconciliation;
    }

    /**
     * 初始化一个收费参数
     *
     * @return 预结算的参数
     */
    public Clinic2206OrderOutput initClinic2206OrderOutput() {
        Clinic2206OrderOutput clinic2206OrderResult = new Clinic2206OrderOutput();
        return clinic2206OrderResult.setMedfeeSumamt(new BigDecimal("0.0")).setFulamtOwnpayAmt(new BigDecimal("0.0"))
            .setOverlmtSelfpay(new BigDecimal("0.0")).setPreselfpayAmt(new BigDecimal("0.0"))
            .setInscpScpAmt(new BigDecimal("0.0")).setActPayDedc(new BigDecimal("0.0"))
            .setHifpPay(new BigDecimal("0.0")).setPoolPropSelfpay(new BigDecimal("0.0"))
            .setCvlservPay(new BigDecimal("0.0")).setHifesPay(new BigDecimal("0.0")).setHifmiPay(new BigDecimal("0.0"))
            .setHifobPay(new BigDecimal("0.00")).setMafPay(new BigDecimal("0.0")).setOthPay(new BigDecimal("0.0"))
            .setFundPaySumamt(new BigDecimal("0.0")).setPsnPartAmt(new BigDecimal("0.0"))
            .setAcctPay(new BigDecimal("0.0")).setPsnCashPay(new BigDecimal("0.0"))
            .setHospPartAmt(new BigDecimal("0.0")).setBalc(new BigDecimal("0.0"))
            .setAcctMulaidPay(new BigDecimal("0.0")).setHifdmPay(new BigDecimal("0.0"));
    }

    /**
     * 保存付款详情
     *
     * @param prePaymentResults 预付款集合
     * @param payment 付款实体
     */
    private List<PaymentRecDetail> savePaymentDetail(List<PrePaymentResult> prePaymentResults,
        PaymentReconciliation payment) {
        // 保存付款详情
        List<PaymentRecDetail> paymentRecDetails = new ArrayList<>();
        for (PrePaymentResult prePaymentResult : prePaymentResults) {
            // 个人现金支出
            BigDecimal psnCashPay = prePaymentResult.getPsnCashPay();
            // 现金支付
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail10 = new PaymentRecDetail();
                paymentRecDetail10.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_CASH_PAY.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_PAY.getLevel()).setAmount(psnCashPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail10);
            }

            // 个人负担总金额
            BigDecimal psnPartAmt = prePaymentResult.getPsnPartAmt();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail1 = new PaymentRecDetail();
                paymentRecDetail1.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_PAY.getValue())
                    .setPayLevelEnum(YbPayment.SELF_PAY.getLevel()).setAmount(psnPartAmt)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail1);
            }
            // 基本医疗保险统筹基金支出
            BigDecimal hifpPay = prePaymentResult.getHifpPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail2 = new PaymentRecDetail();
                paymentRecDetail2.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.YB_TC_FUND_AMOUNT.getValue())
                    .setPayLevelEnum(YbPayment.YB_TC_FUND_AMOUNT.getLevel()).setAmount(hifpPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail2);
            }

            // 公务员医疗补助资金支出
            BigDecimal cvlservPay = prePaymentResult.getCvlservPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail3 = new PaymentRecDetail();
                paymentRecDetail3.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getLevel()).setAmount(cvlservPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail3);
            }
            // 企业补充医疗保险基金支出
            BigDecimal hifesPay = prePaymentResult.getHifesPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail4 = new PaymentRecDetail();
                paymentRecDetail4.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.YB_BC_FUND_AMOUNT.getValue())
                    .setPayLevelEnum(YbPayment.YB_BC_FUND_AMOUNT.getLevel()).setAmount(hifesPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail4);
            }
            // 居民大病保险资金支出
            BigDecimal hifmiPay = prePaymentResult.getHifmiPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail5 = new PaymentRecDetail();
                paymentRecDetail5.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.YB_BC_JM_DB_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.YB_BC_JM_DB_VALUE.getLevel()).setAmount(hifmiPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail5);
            }
            // 职工大额医疗费用补助基金支出
            BigDecimal hifobPay = prePaymentResult.getHifobPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail6 = new PaymentRecDetail();
                paymentRecDetail6.setReconciliationId(payment.getId()).setPayTransNo(prePaymentResult.getChrgBchno())
                    .setPayLevelEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getLevel()).setAmount(hifobPay)
                    .setPayEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getValue())
                    .setResultEnum(PaymentResult.UNPAID.getValue()).setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail6);
            }
            // 医疗救助基金支出
            BigDecimal mafPay = prePaymentResult.getMafPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail7 = new PaymentRecDetail();
                paymentRecDetail7.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.YB_JZ_FUND_AMOUNT.getValue())
                    .setPayLevelEnum(YbPayment.YB_JZ_FUND_AMOUNT.getLevel()).setAmount(mafPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail7);
            }
            // 其他支出
            BigDecimal othPay = prePaymentResult.getOthPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail8 = new PaymentRecDetail();
                paymentRecDetail8.setReconciliationId(payment.getId()).setPayEnum(YbPayment.OTHER_PAY.getValue())
                    .setPayLevelEnum(YbPayment.OTHER_PAY.getLevel()).setAmount(othPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail8);
            }
            // 基金支付总额
            BigDecimal fundPaySumamt = prePaymentResult.getFundPaySumamt();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail9 = new PaymentRecDetail();
                paymentRecDetail9.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_FUND_PAY.getValue())
                    .setPayLevelEnum(YbPayment.YB_FUND_PAY.getLevel()).setAmount(fundPaySumamt)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail9);
            }
            // 个人账户支出
            BigDecimal acctPay = prePaymentResult.getAcctPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail11 = new PaymentRecDetail();
                paymentRecDetail11.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_YB_ZH_PAY.getValue())
                    .setPayLevelEnum(YbPayment.SELF_YB_ZH_PAY.getLevel()).setAmount(acctPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail11);
            }
            // 医院负担金额
            BigDecimal hospPartAmt = prePaymentResult.getHospPartAmt();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail12 = new PaymentRecDetail();
                paymentRecDetail12.setReconciliationId(payment.getId()).setPayEnum(YbPayment.HOSP_PART_AMT.getValue())
                    .setPayLevelEnum(YbPayment.HOSP_PART_AMT.getLevel()).setAmount(hospPartAmt)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail12);
            }
            // 个人账户共济支付金额
            BigDecimal acctMulaidPay = prePaymentResult.getAcctMulaidPay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail13 = new PaymentRecDetail();
                paymentRecDetail13.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getLevel()).setAmount(acctMulaidPay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail13);
            }
            // 全自费金额
            BigDecimal fulamtOwnpayAmt = prePaymentResult.getFulamtOwnpayAmt();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail14 = new PaymentRecDetail();
                paymentRecDetail14.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.FULAMT_OWNPAY_AMT.getValue())
                    .setPayLevelEnum(YbPayment.FULAMT_OWNPAY_AMT.getLevel()).setAmount(fulamtOwnpayAmt)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail14);
            }
            // 超限价自费费用
            BigDecimal overlmtSelfpay = prePaymentResult.getOverlmtSelfpay();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail15 = new PaymentRecDetail();
                paymentRecDetail15.setReconciliationId(payment.getId()).setPayEnum(YbPayment.OVERLMT_SELFPAY.getValue())
                    .setPayLevelEnum(YbPayment.OVERLMT_SELFPAY.getLevel()).setAmount(overlmtSelfpay)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail15);
            }
            // BigDecimal balc = prePaymentResult.getBalc();
            // PaymentRecDetail paymentRecDetail16 = new PaymentRecDetail();
            // paymentRecDetail16.setReconciliationId(payment.getId()).setPayEnum(YbPayment.BALC.getValue())
            // .setPayLevelEnum(YbPayment.BALC.getLevel()).setAmount(balc)
            // .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            // paymentRecDetails.add(paymentRecDetail16);
            BigDecimal preselfpayAmt = prePaymentResult.getPreselfpayAmt();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail17 = new PaymentRecDetail();
                paymentRecDetail17.setReconciliationId(payment.getId()).setPayEnum(YbPayment.PRESELFPAY_AMT.getValue())
                    .setPayLevelEnum(YbPayment.PRESELFPAY_AMT.getLevel()).setAmount(preselfpayAmt)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail17);
            }
            BigDecimal inscpScpAmt = prePaymentResult.getInscpScpAmt();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail18 = new PaymentRecDetail();
                paymentRecDetail18.setReconciliationId(payment.getId()).setPayEnum(YbPayment.INSCP_SCP_AMT.getValue())
                    .setPayLevelEnum(YbPayment.INSCP_SCP_AMT.getLevel()).setAmount(inscpScpAmt)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail18);
            }
            BigDecimal actPayDedc = prePaymentResult.getActPayDedc();
            if (psnCashPay != null) {
                PaymentRecDetail paymentRecDetail19 = new PaymentRecDetail();
                paymentRecDetail19.setReconciliationId(payment.getId()).setPayEnum(YbPayment.ACT_PAY_DEDC.getValue())
                    .setPayLevelEnum(YbPayment.ACT_PAY_DEDC.getLevel()).setAmount(actPayDedc)
                    .setPayTransNo(prePaymentResult.getChrgBchno()).setResultEnum(PaymentResult.UNPAID.getValue())
                    .setAccountId(prePaymentResult.getAccountId());
                paymentRecDetails.add(paymentRecDetail19);
            }
        }
        paymentRecDetailService.saveBatch(paymentRecDetails);
        return paymentRecDetails;
    }

    /**
     * 保存付款详情
     *
     * @param paymentResult 总付款金额集合
     * @param payment 付款实体
     * @param paymentDetails 付款详情
     */
    private void savePaymentDetail(com.openhis.financial.model.PaymentResult paymentResult,
        PaymentReconciliation payment, List<PaymentDetailDto> paymentDetails, Long accountId) {
        // 保存付款详情
        List<PaymentRecDetail> paymentRecDetails = new ArrayList<>();
        // BigDecimal amount = BigDecimal.ZERO;
        // String AccountEnum = "04";
        // Account account = accountService.getById(payment.getAccountId());
        // AccountEnum = account.getTypeCode();
        PaymentRecDetail paymentRecDetail;
        for (PaymentDetailDto paymentDetail : paymentDetails) {
            paymentRecDetail = new PaymentRecDetail();
            if (YbPayment.SELF_CASH_VX_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_VX_VALUE.getValue()).setAccountId(accountId)
                    .setPayLevelEnum(YbPayment.SELF_CASH_VX_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_ALI_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_ALI_VALUE.getValue()).setAccountId(accountId)
                    .setPayLevelEnum(YbPayment.SELF_CASH_ALI_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_UNION_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_UNION_VALUE.getValue()).setAccountId(accountId)
                    .setPayLevelEnum(YbPayment.SELF_CASH_UNION_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_CASH_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
                paymentRecDetails.add(paymentRecDetail);
            }

            // amount = amount.add(paymentDetail.getAmount());
        }
        // 个人现金支出
        BigDecimal psnCashPay = paymentResult.getPsnCashPay();
        // if(psnCashPay.compareTo(amount)!=0){
        // throw new ServiceException("金额校验失败");
        // }
        // 现金支付
        PaymentRecDetail paymentRecDetail10 = new PaymentRecDetail();
        paymentRecDetail10.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_CASH_PAY.getValue())
            .setPayLevelEnum(YbPayment.SELF_CASH_PAY.getLevel()).setAmount(psnCashPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail10);
        // 个人负担总金额
        BigDecimal psnPartAmt = paymentResult.getPsnPartAmt();
        PaymentRecDetail paymentRecDetail1 = new PaymentRecDetail();
        paymentRecDetail1.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_PAY.getValue())
            .setPayLevelEnum(YbPayment.SELF_PAY.getLevel()).setAmount(psnPartAmt).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail1);
        // 基本医疗保险统筹基金支出
        BigDecimal hifpPay = paymentResult.getHifpPay();
        PaymentRecDetail paymentRecDetail2 = new PaymentRecDetail();
        paymentRecDetail2.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_TC_FUND_AMOUNT.getValue())
            .setPayLevelEnum(YbPayment.YB_TC_FUND_AMOUNT.getLevel()).setAmount(hifpPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail2);
        // 公务员医疗补助资金支出
        BigDecimal cvlservPay = paymentResult.getCvlservPay();
        PaymentRecDetail paymentRecDetail3 = new PaymentRecDetail();
        paymentRecDetail3.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getLevel()).setAmount(cvlservPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail3);
        // 企业补充医疗保险基金支出
        BigDecimal hifesPay = paymentResult.getHifesPay();
        PaymentRecDetail paymentRecDetail4 = new PaymentRecDetail();
        paymentRecDetail4.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_BC_FUND_AMOUNT.getValue())
            .setPayLevelEnum(YbPayment.YB_BC_FUND_AMOUNT.getLevel()).setAmount(hifesPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail4);
        // 居民大病保险资金支出
        BigDecimal hifmiPay = paymentResult.getHifmiPay();
        PaymentRecDetail paymentRecDetail5 = new PaymentRecDetail();
        paymentRecDetail5.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_BC_JM_DB_VALUE.getValue())
            .setPayLevelEnum(YbPayment.YB_BC_JM_DB_VALUE.getLevel()).setAmount(hifmiPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail5);
        // 职工大额医疗费用补助基金支出
        BigDecimal hifobPay = paymentResult.getHifobPay();
        PaymentRecDetail paymentRecDetail6 = new PaymentRecDetail();
        paymentRecDetail6.setReconciliationId(payment.getId())
            .setPayLevelEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getLevel()).setAmount(hifobPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail6);
        // 医疗救助基金支出
        BigDecimal mafPay = paymentResult.getMafPay();
        PaymentRecDetail paymentRecDetail7 = new PaymentRecDetail();
        paymentRecDetail7.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_JZ_FUND_AMOUNT.getValue())
            .setPayLevelEnum(YbPayment.YB_JZ_FUND_AMOUNT.getLevel()).setAmount(mafPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail7);
        // 其他支出
        BigDecimal othPay = paymentResult.getOthPay();
        PaymentRecDetail paymentRecDetail8 = new PaymentRecDetail();
        paymentRecDetail8.setReconciliationId(payment.getId()).setPayEnum(YbPayment.OTHER_PAY.getValue())
            .setPayLevelEnum(YbPayment.OTHER_PAY.getLevel()).setAmount(othPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail8);
        // 基金支付总额
        BigDecimal fundPaySumamt = paymentResult.getFundPaySumamt();
        PaymentRecDetail paymentRecDetail9 = new PaymentRecDetail();
        paymentRecDetail9.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_FUND_PAY.getValue())
            .setPayLevelEnum(YbPayment.YB_FUND_PAY.getLevel()).setAmount(fundPaySumamt).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail9);
        // 个人账户支出
        BigDecimal acctPay = paymentResult.getAcctPay();
        PaymentRecDetail paymentRecDetail11 = new PaymentRecDetail();
        paymentRecDetail11.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_YB_ZH_PAY.getValue())
            .setPayLevelEnum(YbPayment.SELF_YB_ZH_PAY.getLevel()).setAmount(acctPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail11);
        // 医院负担金额
        BigDecimal hospPartAmt = paymentResult.getHospPartAmt();
        PaymentRecDetail paymentRecDetail12 = new PaymentRecDetail();
        paymentRecDetail12.setReconciliationId(payment.getId()).setPayEnum(YbPayment.OTHER_PAY.getValue())
            .setPayLevelEnum(YbPayment.OTHER_PAY.getLevel()).setAmount(hospPartAmt).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail12);
        // 个人账户共济支付金额
        BigDecimal acctMulaidPay = paymentResult.getAcctMulaidPay();
        PaymentRecDetail paymentRecDetail13 = new PaymentRecDetail();
        paymentRecDetail13.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getLevel()).setAmount(acctMulaidPay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail13);
        // 全自费金额
        BigDecimal fulamtOwnpayAmt = paymentResult.getFulamtOwnpayAmt();
        PaymentRecDetail paymentRecDetail14 = new PaymentRecDetail();
        paymentRecDetail14.setReconciliationId(payment.getId()).setPayEnum(YbPayment.FULAMT_OWNPAY_AMT.getValue())
            .setPayLevelEnum(YbPayment.FULAMT_OWNPAY_AMT.getLevel()).setAmount(fulamtOwnpayAmt).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail14);
        // 超限价自费费用
        BigDecimal overlmtSelfpay = paymentResult.getOverlmtSelfpay();
        PaymentRecDetail paymentRecDetail15 = new PaymentRecDetail();
        paymentRecDetail15.setReconciliationId(payment.getId()).setPayEnum(YbPayment.OVERLMT_SELFPAY.getValue())
            .setPayLevelEnum(YbPayment.OVERLMT_SELFPAY.getLevel()).setAmount(overlmtSelfpay).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(accountId);
        paymentRecDetails.add(paymentRecDetail15);
        BigDecimal balc = paymentResult.getBalc();
        PaymentRecDetail paymentRecDetail16 = new PaymentRecDetail();
        paymentRecDetail16.setReconciliationId(payment.getId()).setPayEnum(YbPayment.BALC.getValue())
            .setAccountId(accountId).setPayLevelEnum(YbPayment.BALC.getLevel()).setAmount(balc).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail16);
        BigDecimal preselfpayAmt = paymentResult.getPreselfpayAmt();
        PaymentRecDetail paymentRecDetail17 = new PaymentRecDetail();
        paymentRecDetail17.setReconciliationId(payment.getId()).setPayEnum(YbPayment.PRESELFPAY_AMT.getValue())
            .setPayLevelEnum(YbPayment.PRESELFPAY_AMT.getLevel()).setAmount(preselfpayAmt).setAccountId(accountId).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail17);
        BigDecimal inscpScpAmt = paymentResult.getInscpScpAmt();
        PaymentRecDetail paymentRecDetail18 = new PaymentRecDetail();
        paymentRecDetail18.setReconciliationId(payment.getId()).setPayEnum(YbPayment.INSCP_SCP_AMT.getValue())
            .setPayLevelEnum(YbPayment.INSCP_SCP_AMT.getLevel()).setAmount(inscpScpAmt).setAccountId(accountId).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail18);
        BigDecimal actPayDedc = paymentResult.getActPayDedc();
        PaymentRecDetail paymentRecDetail19 = new PaymentRecDetail();
        paymentRecDetail19.setReconciliationId(payment.getId()).setPayEnum(YbPayment.ACT_PAY_DEDC.getValue())
            .setPayLevelEnum(YbPayment.ACT_PAY_DEDC.getLevel()).setAmount(actPayDedc).setAccountId(accountId).setPayTransText(paymentResult.getSetlId())
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail19);

        paymentRecDetailService.saveBatch(paymentRecDetails);
    }

    /**
     * 保存付款详情(弃用，若启用，需要确认一下setAccountId是否正确)
     *
     * @param clinicOrder2207ResultList 总付款金额集合
     * @param payment 付款实体
     * @param paymentDetails 付款详情
     */
    private void savePaymentDetail(List<Clinic2207OrderResultDto> clinicOrder2207ResultList,
        PaymentReconciliation payment, List<PaymentDetailDto> paymentDetails) {
        // 保存付款详情
        List<PaymentRecDetail> paymentRecDetails = new ArrayList<>();
        // BigDecimal amount = BigDecimal.ZERO;
        // String AccountEnum = "04";
        // Account account = accountService.getById(payment.getAccountId());
        // AccountEnum = account.getTypeCode();
        PaymentRecDetail paymentRecDetail;
        for (PaymentDetailDto paymentDetail : paymentDetails) {
            paymentRecDetail = new PaymentRecDetail();
            if (YbPayment.SELF_CASH_VX_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_VX_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_VX_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_ALI_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_ALI_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_ALI_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_UNION_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_UNION_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_UNION_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_CASH_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
                paymentRecDetails.add(paymentRecDetail);
            }

            // amount = amount.add(paymentDetail.getAmount());
        }
        for (Clinic2207OrderResultDto clinicOrder2207Result : clinicOrder2207ResultList) {
            // 个人现金支出
            BigDecimal psnCashPay = clinicOrder2207Result.getPsnCashPay();
            // if(psnCashPay.compareTo(amount)!=0){
            // throw new ServiceException("金额校验失败");
            // }
            // 现金支付
            PaymentRecDetail paymentRecDetail10 = new PaymentRecDetail();
            paymentRecDetail10.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_CASH_PAY.getValue())
                .setPayLevelEnum(YbPayment.SELF_CASH_PAY.getLevel()).setAmount(psnCashPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail10);
            // 个人负担总金额
            BigDecimal psnPartAmt = clinicOrder2207Result.getPsnPartAmt();
            PaymentRecDetail paymentRecDetail1 = new PaymentRecDetail();
            paymentRecDetail1.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_PAY.getValue())
                .setPayLevelEnum(YbPayment.SELF_PAY.getLevel()).setAmount(psnPartAmt)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail1);
            // 基本医疗保险统筹基金支出
            BigDecimal hifpPay = clinicOrder2207Result.getHifpPay();
            PaymentRecDetail paymentRecDetail2 = new PaymentRecDetail();
            paymentRecDetail2.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_TC_FUND_AMOUNT.getValue())
                .setPayLevelEnum(YbPayment.YB_TC_FUND_AMOUNT.getLevel()).setAmount(hifpPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail2);
            // 公务员医疗补助资金支出
            BigDecimal cvlservPay = clinicOrder2207Result.getCvlservPay();
            PaymentRecDetail paymentRecDetail3 = new PaymentRecDetail();
            paymentRecDetail3.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getValue())
                .setPayLevelEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getLevel()).setAmount(cvlservPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail3);
            // 企业补充医疗保险基金支出
            BigDecimal hifesPay = clinicOrder2207Result.getHifesPay();
            PaymentRecDetail paymentRecDetail4 = new PaymentRecDetail();
            paymentRecDetail4.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_BC_FUND_AMOUNT.getValue())
                .setPayLevelEnum(YbPayment.YB_BC_FUND_AMOUNT.getLevel()).setAmount(hifesPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail4);
            // 居民大病保险资金支出
            BigDecimal hifmiPay = clinicOrder2207Result.getHifmiPay();
            PaymentRecDetail paymentRecDetail5 = new PaymentRecDetail();
            paymentRecDetail5.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_BC_JM_DB_VALUE.getValue())
                .setPayLevelEnum(YbPayment.YB_BC_JM_DB_VALUE.getLevel()).setAmount(hifmiPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail5);
            // 职工大额医疗费用补助基金支出
            BigDecimal hifobPay = clinicOrder2207Result.getHifobPay();
            PaymentRecDetail paymentRecDetail6 = new PaymentRecDetail();
            paymentRecDetail6.setReconciliationId(payment.getId())
                .setPayLevelEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getLevel()).setAmount(hifobPay)
                .setPayEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getValue()).setResultEnum(PaymentResult.PAID.getValue())
                .setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail6);
            // 医疗救助基金支出
            BigDecimal mafPay = clinicOrder2207Result.getMafPay();
            PaymentRecDetail paymentRecDetail7 = new PaymentRecDetail();
            paymentRecDetail7.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_JZ_FUND_AMOUNT.getValue())
                .setPayLevelEnum(YbPayment.YB_JZ_FUND_AMOUNT.getLevel()).setAmount(mafPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail7);
            // 其他支出
            BigDecimal othPay = clinicOrder2207Result.getOthPay();
            PaymentRecDetail paymentRecDetail8 = new PaymentRecDetail();
            paymentRecDetail8.setReconciliationId(payment.getId()).setPayEnum(YbPayment.OTHER_PAY.getValue())
                .setPayLevelEnum(YbPayment.OTHER_PAY.getLevel()).setAmount(othPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail8);
            // 基金支付总额
            BigDecimal fundPaySumamt = clinicOrder2207Result.getFundPaySumamt();
            PaymentRecDetail paymentRecDetail9 = new PaymentRecDetail();
            paymentRecDetail9.setReconciliationId(payment.getId()).setPayEnum(YbPayment.YB_FUND_PAY.getValue())
                .setPayLevelEnum(YbPayment.YB_FUND_PAY.getLevel()).setAmount(fundPaySumamt)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail9);
            // 个人账户支出
            BigDecimal acctPay = clinicOrder2207Result.getAcctPay();
            PaymentRecDetail paymentRecDetail11 = new PaymentRecDetail();
            paymentRecDetail11.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_YB_ZH_PAY.getValue())
                .setPayLevelEnum(YbPayment.SELF_YB_ZH_PAY.getLevel()).setAmount(acctPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail11);
            // 医院负担金额
            BigDecimal hospPartAmt = clinicOrder2207Result.getHospPartAmt();
            PaymentRecDetail paymentRecDetail12 = new PaymentRecDetail();
            paymentRecDetail12.setReconciliationId(payment.getId()).setPayEnum(YbPayment.HOSP_PART_AMT.getValue())
                .setPayLevelEnum(YbPayment.HOSP_PART_AMT.getLevel()).setAmount(hospPartAmt)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail12);
            // 个人账户共济支付金额
            BigDecimal acctMulaidPay = clinicOrder2207Result.getAcctMulaidPay();
            PaymentRecDetail paymentRecDetail13 = new PaymentRecDetail();
            paymentRecDetail13.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getValue())
                .setPayLevelEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getLevel()).setAmount(acctMulaidPay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail13);
            // 全自费金额
            BigDecimal fulamtOwnpayAmt = clinicOrder2207Result.getFulamtOwnpayAmt();
            PaymentRecDetail paymentRecDetail14 = new PaymentRecDetail();
            paymentRecDetail14.setReconciliationId(payment.getId()).setPayEnum(YbPayment.FULAMT_OWNPAY_AMT.getValue())
                .setPayLevelEnum(YbPayment.FULAMT_OWNPAY_AMT.getLevel()).setAmount(fulamtOwnpayAmt)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail14);
            // 超限价自费费用
            BigDecimal overlmtSelfpay = clinicOrder2207Result.getOverlmtSelfpay();
            PaymentRecDetail paymentRecDetail15 = new PaymentRecDetail();
            paymentRecDetail15.setReconciliationId(payment.getId()).setPayEnum(YbPayment.OVERLMT_SELFPAY.getValue())
                .setPayLevelEnum(YbPayment.OVERLMT_SELFPAY.getLevel()).setAmount(overlmtSelfpay)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail15);
            BigDecimal balc = clinicOrder2207Result.getBalc();
            PaymentRecDetail paymentRecDetail16 = new PaymentRecDetail();
            paymentRecDetail16.setReconciliationId(payment.getId()).setPayEnum(YbPayment.BALC.getValue())
                .setPayLevelEnum(YbPayment.BALC.getLevel()).setAmount(balc).setResultEnum(PaymentResult.PAID.getValue())
                .setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail16);
            BigDecimal preselfpayAmt = clinicOrder2207Result.getPreselfpayAmt();
            PaymentRecDetail paymentRecDetail17 = new PaymentRecDetail();
            paymentRecDetail17.setReconciliationId(payment.getId()).setPayEnum(YbPayment.PRESELFPAY_AMT.getValue())
                .setPayLevelEnum(YbPayment.PRESELFPAY_AMT.getLevel()).setAmount(preselfpayAmt)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail17);
            BigDecimal inscpScpAmt = clinicOrder2207Result.getInscpScpAmt();
            PaymentRecDetail paymentRecDetail18 = new PaymentRecDetail();
            paymentRecDetail18.setReconciliationId(payment.getId()).setPayEnum(YbPayment.INSCP_SCP_AMT.getValue())
                .setPayLevelEnum(YbPayment.INSCP_SCP_AMT.getLevel()).setAmount(inscpScpAmt)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail18);
            BigDecimal actPayDedc = clinicOrder2207Result.getActPayDedc();
            PaymentRecDetail paymentRecDetail19 = new PaymentRecDetail();
            paymentRecDetail19.setReconciliationId(payment.getId()).setPayEnum(YbPayment.ACT_PAY_DEDC.getValue())
                .setPayLevelEnum(YbPayment.ACT_PAY_DEDC.getLevel()).setAmount(actPayDedc)
                .setResultEnum(PaymentResult.PAID.getValue()).setAccountId(clinicOrder2207Result.getAccountId());
            paymentRecDetails.add(paymentRecDetail19);
        }
        // 由于多次付款整合为一个payment，所以每次医保结算的基金详细支付需要去医保的表中查询
        // for (Clinic2206FundPaymentResult clinic2206FundPaymentResult : clinicOrder2207Result.getSetldetail()) {
        // PaymentRecDetail detail = new PaymentRecDetail();
        // YbPayment ybPayment = YbPayment.getByValue(Integer.parseInt(clinic2206FundPaymentResult.getFundPayType()));
        // detail.setReconciliationId(payment.getId()).setAccountId(paymentDetails.get(0).getAccountId())
        // .setAccountCode(AccountEnum).setPayEnum(ybPayment.getValue()).setPayLevelEnum(ybPayment.getLevel())
        // .setAmount(new BigDecimal(clinic2206FundPaymentResult.getFundPayamt().toString()))
        // .setResultEnum(PaymentResult.PAID.getValue());
        // paymentRecDetails.add(detail);
        // }
        paymentRecDetailService.saveBatch(paymentRecDetails);
    }

    @Override
    public R<?> regPrePay(OutpatientRegistrationAddParam outpatientRegistrationAddParam) {

        // // 患者ID
        // Long patientId = outpatientRegistrationAddParam.getEncounterFormData().getPatientId();
        // // 服务项目ID
        // Long serviceTypeId = outpatientRegistrationAddParam.getEncounterFormData().getServiceTypeId();
        // // 校验是否重复挂号(不包括退号)
        // Integer num = outpatientRegistrationAppMapper.getNumByPatientIdAndOrganizationId(patientId, serviceTypeId,
        // EncounterStatus.CANCELLED.getValue());
        // if (num > 0) {
        // return R.fail(null, "当日已挂号此服务");
        // }

        Patient patient = iPatientService.getById(outpatientRegistrationAddParam.getEncounterFormData().getPatientId());
        if (patient == null) {
            throw new ServiceException("未查询到患者信息");
        }
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss.SSS+08");

        // 获取当前日期时间的 0 点
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        String startOfDayStr = startOfDay.format(formatter);

        // 转换为 Date 类型
        Date startOfDayDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());

        // 获取第二天日期时间的 0 点
        LocalDateTime startOfNextDay = startOfDay.plusDays(1);
        String startOfNextDayStr = startOfNextDay.format(formatter);

        // 转换为 Date 类型
        Date startOfNextDayDate = Date.from(startOfNextDay.atZone(ZoneId.systemDefault()).toInstant());

        // 查询科室信息
        Organization organization =
            iOrganizationService.getById(outpatientRegistrationAddParam.getEncounterFormData().getOrganizationId());
        if (organization == null) {
            throw new ServiceException("未查询到科室信息");
        }

        // 主治医生查询
        Practitioner doctor = null;
        if (outpatientRegistrationAddParam.getEncounterParticipantFormData().getPractitionerId() == null) {
            doctor = iPractitionerService.getById(organization.getDefDoctorId());
        } else {
            doctor = iPractitionerService
                .getById(outpatientRegistrationAddParam.getEncounterParticipantFormData().getPractitionerId());
        }

        if (doctor == null) {
            throw new ServiceException("医保挂号未选择医生，并且没配置默认挂号医生");
        }

        // 校验当天是否已经医保挂号，若有则冲销
        List<ClinicReg> list =
            iRegService.list(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtCertNo, patient.getIdCard())
                .between(ClinicReg::getBegntime, startOfDayDate, startOfNextDayDate));
        for (ClinicReg clinicReg : list) {
            if (organization.getYbNo().equals(clinicReg.getDeptCode())
                && doctor.getYbNo().equals(clinicReg.getDeptCode())) {
                ybManager.cancelRegById(clinicReg.getId());
            }
        }

        Clinic2206OrderOutput clinic2206OrderResult;
        if ("1".equals(SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH))
            && !"0000".equals(outpatientRegistrationAddParam.getAccountFormData().getContractNo())) {
            OutpatientRegistrationModel model = new OutpatientRegistrationModel();
            model.setMdtrtCertType(outpatientRegistrationAddParam.getYbMdtrtCertType());
            model.setBusiCardInfo(outpatientRegistrationAddParam.getBusiCardInfo());
            model.setPsnSetlWay(outpatientRegistrationAddParam.getYbPsnSetlWay());
            model.setTotalPrice(outpatientRegistrationAddParam.getChargeItemFormData().getTotalPrice());
            // 进入医保挂号及预结算流程
            clinic2206OrderResult = ybManager.getPreSettleInfo(model, patient, organization, doctor);
        } else {
            // 自费预结算流程
            clinic2206OrderResult = preSettleByZfForReg(outpatientRegistrationAddParam);
        }
        return R.ok(clinic2206OrderResult);
    }

    private Clinic2206OrderOutput preSettleByZfForReg(OutpatientRegistrationAddParam outpatientRegistrationAddParam) {
        // BigDecimal sumAmount = BigDecimal.ZERO;
        ChargeItem chargeItem = new ChargeItem();
        BeanUtils.copyProperties(outpatientRegistrationAddParam.getChargeItemFormData(), chargeItem);
        chargeItem.setContextEnum(ChargeItemContext.REGISTER.getValue());// 挂号

        Clinic2206OrderOutput clinic2206OrderResult = this.initClinic2206OrderOutput();
        return clinic2206OrderResult
            .setChrgBchno(assignSeqUtil.getSeqByDay(AssignSeqEnum.SF_CLINIC_ORDER.getPrefix(), 12))
            .setMedfeeSumamt(chargeItem.getTotalPrice()).setFulamtOwnpayAmt(chargeItem.getTotalPrice())
            .setPsnPartAmt(chargeItem.getTotalPrice()).setPsnCashPay(chargeItem.getTotalPrice())
            .setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.ENCOUNTER_NUM.getPrefix(), 8));
    }

    @Override
    public R<?> regPay(OutpatientRegistrationSettleParam outpatientRegistrationSettleParam, String chrgBchno,
        List<PaymentDetailDto> paymentDetails) {

        OutpatientRegistrationAddParam outpatientRegistrationAddParam =
            outpatientRegistrationSettleParam.getOutpatientRegistrationAddParam();
        ChargeItem chargeItem = new ChargeItem();
        BeanUtils.copyProperties(outpatientRegistrationAddParam.getChargeItemFormData(), chargeItem);
        chargeItem.setContextEnum(ChargeItemContext.REGISTER.getValue());// 挂号

        YbPsnSetlWay finCategory = YbPsnSetlWay.getByValue(outpatientRegistrationAddParam.getYbPsnSetlWay());
        if (finCategory == null) {
            throw new ServiceException("请选择收费方式");
        }

        // 就诊管理-表单数据
        EncounterFormData encounterFormData = outpatientRegistrationAddParam.getEncounterFormData();
        // 就诊位置管理-表单数据
        // EncounterLocationFormData encounterLocationFormData =
        // outpatientRegistrationAddParam.getEncounterLocationFormData();
        // 就诊参数者管理-表单数据
        EncounterParticipantFormData encounterParticipantFormData =
            outpatientRegistrationAddParam.getEncounterParticipantFormData();
        // 就诊账户管理-表单数据
        AccountFormData accountFormData = outpatientRegistrationAddParam.getAccountFormData();
        // 费用项管理-表单数据
        ChargeItemFormData chargeItemFormData = outpatientRegistrationAddParam.getChargeItemFormData();

        // 患者ID
        Long patientId = encounterFormData.getPatientId();
        // 服务项目ID
        Long serviceTypeId = encounterFormData.getServiceTypeId();
        // 校验是否重复挂号(不包括退号)
        Integer num = outpatientRegistrationAppMapper.getNumByPatientIdAndOrganizationId(patientId, serviceTypeId,
            EncounterStatus.CANCELLED.getValue());
        if (num > 0) {
            return R.fail(null, "当日已挂号此服务");
        }

        com.openhis.financial.model.PaymentResult paymentResult;
        if ("1".equals(SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH))
            && !"0000".equals(outpatientRegistrationAddParam.getAccountFormData().getContractNo())) {
            // 医保结算
            paymentResult = ybManager.settle(chrgBchno);
        } else {
            // 自费结算
            paymentResult = new com.openhis.financial.model.PaymentResult();
            paymentResult.setChrgBchno(chrgBchno).setMedfeeSumamt(chargeItem.getTotalPrice())
                .setFulamtOwnpayAmt(chargeItem.getTotalPrice()).setPsnPartAmt(chargeItem.getTotalPrice())
                .setPsnCashPay(chargeItem.getTotalPrice()).setSetlId("0000");
        }

        // 生成payment
        // 处理时间
        Date setlTime = paymentResult.getSetlTime() == null ? new Date() : paymentResult.getSetlTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(setlTime);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        Date futureTime = calendar.getTime();

        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();

        // 保存就诊信息
        Encounter encounter = new Encounter();
        BeanUtils.copyProperties(encounterFormData, encounter);
        encounter.setBusNo(outpatientRegistrationSettleParam.getBusNo());
        // 就诊ID
        Long encounterId = iEncounterService.saveEncounterByRegister(encounter);
        // 保存就诊位置信息
        // 挂号时不选Location了
        // encounterLocationFormData.setEncounterId(encounterId);
        // EncounterLocation encounterLocation = new EncounterLocation();
        // BeanUtils.copyProperties(encounterLocationFormData, encounterLocation);
        // iEncounterLocationService.saveEncounterLocationByRegister(encounterLocation);
        // 保存就诊参数者信息 , 选了参与者才保存
        if (encounterParticipantFormData.getPractitionerId() != null) {
            encounterParticipantFormData.setEncounterId(encounterId);
            EncounterParticipant encounterParticipant = new EncounterParticipant();
            BeanUtils.copyProperties(encounterParticipantFormData, encounterParticipant);
            encounterParticipant.setTypeCode(ParticipantType.REGISTRATION_DOCTOR.getCode()); // 挂号医生
            iEncounterParticipantService.saveEncounterParticipantByRegister(encounterParticipant);
        }
        // 保存就诊账户信息
        accountFormData.setEncounterId(encounterId);
        Account account = new Account();
        BeanUtils.copyProperties(accountFormData, account);
        account.setTypeCode(outpatientRegistrationAddParam.getYbMdtrtCertType() == null
            ? YbMdtrtCertType.MDTRT_CERT_TYPE02.getValue() : outpatientRegistrationAddParam.getYbMdtrtCertType());
        // 账户ID
        Long accountId = iAccountService.saveAccountByRegister(account);
        // 2025/05/23 如果是医保挂号，创建两个ACCOUNT
        if (!"0000".equals(outpatientRegistrationAddParam.getAccountFormData().getContractNo())) {
            Account accountZf = new Account();
            BeanUtils.copyProperties(accountFormData, accountZf);
            accountZf.setContractNo("0000").setEncounterFlag(0)
                .setTypeCode(outpatientRegistrationAddParam.getYbMdtrtCertType() == null
                    ? YbMdtrtCertType.MDTRT_CERT_TYPE02.getValue()
                    : outpatientRegistrationAddParam.getYbMdtrtCertType());
            iAccountService.save(accountZf);
        }
        // 保存挂号费
        chargeItem.setEncounterId(encounterId);
        chargeItem.setAccountId(accountId);
        chargeItem.setEntererId(SecurityUtils.getLoginUser().getPractitionerId());
        chargeItem.setQuantityValue(1);
        iChargeItemService.saveChargeItemByRegister(chargeItem);
        // 处理诊疗费并返回对应的收费项目id集合用于医保结算
        List<String> chargeItemIdList = this.handleActivityPrice(encounter, account.getId());
        chargeItemIdList.add(chargeItem.getId().toString());

        String paymentNo;
        if(StringUtils.isEmpty(paymentResult.getPaymentNo())){
            paymentNo = paymentResult.getPaymentNo();
        }else{
            // 统一生成业务流水
            paymentNo = assignSeqUtil.getSeqByDay(AssignSeqEnum.PAYMENT_NO.getPrefix(), 20);
        }

        // 新增支付信息
        PaymentReconciliation payment = new PaymentReconciliation();
        payment.setStatusEnum(PaymentStatus.SUCCESS.getValue()).setPaymentNo(paymentNo)
            .setYbSettleIds(paymentResult.getSetlId()).setPaymentEnum(PaymentType.PAY.getValue())
            .setPaymentReconciliationId(outpatientRegistrationAddParam.getEncounterFormData().getPatientId())
            .setPatientId(outpatientRegistrationAddParam.getEncounterFormData().getPatientId())
            .setKindEnum(PaymentKind.OUTPATIENT_CLINIC.getValue()).setEntererId(practitionerId)
            .setPractitionerId(practitionerId).setOutcomeEnum(PaymentOutcome.PARTIAL.getCode()).setLocationId(-99l)
            .setExpirationDate(futureTime).setBillDate(setlTime).setPrintCount(0)
            .setChargeItemIds(chargeItemIdList.stream().map(String::valueOf).collect(Collectors.joining(",")))
            .setTenderedAmount(chargeItem.getTotalPrice()).setDisplayAmount(paymentResult.getPsnPartAmt())
            .setReturnedAmount(new BigDecimal("0.0")).setEncounterId(encounter.getId());
        // 保存付款信息
        paymentReconciliationService.save(payment);
        // 保存付款详情
        this.savePaymentDetail(paymentResult, payment, paymentDetails, accountId);
        // 更改chargeItem的状态
        List<Long> chargeItemIds =
            Arrays.stream(payment.getChargeItemIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
        chargeItemService.updatePaymentStatus(chargeItemIds, ChargeItemStatus.BILLED.getValue());
        // 生成发票信息
        Invoice invoice = new Invoice();
        invoice.setPatientId(encounter.getPatientId()).setStatusEnum(InvoiceStatus.DRAFT)
            .setReconciliationId(payment.getId()).setTypeCode(InvoiceType.ISSUING_INVOICES.getValue())
            .setChargeItemIds(payment.getChargeItemIds().toString());
        iInvoiceService.save(invoice);
        return R.ok(payment, "收费成功");
    }

    /**
     * 处理诊疗费
     *
     * @param encounter 就诊信息
     * @param accountId 就诊账号id
     */
    private List<String> handleActivityPrice(Encounter encounter, Long accountId) {
        // 号源id
        String serviceTypeId = String.valueOf(encounter.getServiceTypeId());
        // 挂号对应科室id
        Long organizationId = encounter.getOrganizationId();
        // 诊疗定义id集合
        List<ActivityDeviceDto> tmpActivityList = outpatientRegistrationAppMapper.getTmpActivityList(serviceTypeId,
            CommonConstants.TableName.WOR_ACTIVITY_DEFINITION);
        AdviceBaseDto adviceBaseDto;
        ServiceRequest serviceRequest;
        ChargeItem chargeItem;
        List<String> chargeItemIdList = new ArrayList<>();
        // 当前时间
        Date curDate = new Date();
        for (ActivityDeviceDto activityDeviceDto : tmpActivityList) {
            adviceBaseDto = new AdviceBaseDto();
            Integer quantity = activityDeviceDto.getQuantity(); // 请求数量
            adviceBaseDto.setAdviceDefinitionId(activityDeviceDto.getDevActId());
            // 对应的诊疗医嘱信息
            AdviceBaseDto activityAdviceBaseDto = iDoctorStationAdviceAppService
                .getAdviceBaseInfo(adviceBaseDto, null, null, null, organizationId, 1, 1, Whether.NO.getValue())
                .getRecords().get(0);
            // 价格信息
            if (activityAdviceBaseDto != null) {
                // 费用定价
                AdvicePriceDto advicePriceDto = activityAdviceBaseDto.getPriceList().get(0);
                if (advicePriceDto != null) {
                    // 生成诊疗请求
                    serviceRequest = new ServiceRequest();
                    serviceRequest.setStatusEnum(RequestStatus.COMPLETED.getValue());// 请求状态,默认已完成
                    serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
                    serviceRequest.setQuantity(quantity); // 请求数量
                    serviceRequest.setUnitCode(activityAdviceBaseDto.getUnitCode()); // 请求单位编码
                    serviceRequest.setCategoryEnum(EncounterClass.AMB.getValue()); // 请求类型,默认-门诊
                    serviceRequest.setActivityId(activityAdviceBaseDto.getAdviceDefinitionId());// 诊疗定义id
                    serviceRequest.setPatientId(encounter.getPatientId()); // 患者
                    serviceRequest.setRequesterId(SecurityUtils.getLoginUser().getPractitionerId()); // 开方医生
                    serviceRequest.setEncounterId(encounter.getId()); // 就诊id
                    serviceRequest.setAuthoredTime(curDate); // 请求签发时间
                    serviceRequest.setOrgId(organizationId); // 执行科室
                    iServiceRequestService.save(serviceRequest);

                    // 生成账单
                    chargeItem = new ChargeItem();
                    chargeItem.setGenerateSourceEnum(ChargeItemGenerateSource.MEDICAL_ORDER_BINDING.getValue());
                    chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 待收费
                    chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(serviceRequest.getBusNo()));
                    chargeItem.setPatientId(encounter.getPatientId()); // 患者
                    chargeItem.setContextEnum(ChargeItemContext.ACTIVITY.getValue()); // 类型
                    chargeItem.setEncounterId(encounter.getId()); // 就诊id
                    chargeItem.setDefinitionId(advicePriceDto.getDefinitionId()); // 费用定价ID
                    chargeItem.setEntererId(SecurityUtils.getLoginUser().getPractitionerId());// 开立人ID
                    chargeItem.setEnteredDate(curDate); // 开立时间
                    chargeItem.setServiceTable(CommonConstants.TableName.WOR_SERVICE_REQUEST);// 医疗服务类型
                    chargeItem.setServiceId(serviceRequest.getId()); // 医疗服务ID
                    chargeItem.setProductTable(activityAdviceBaseDto.getAdviceTableName());// 产品所在表
                    chargeItem.setProductId(activityAdviceBaseDto.getAdviceDefinitionId());// 收费项id
                    chargeItem.setAccountId(accountId);// 关联账户ID

                    chargeItem.setQuantityValue(quantity); // 数量
                    chargeItem.setQuantityUnit(activityAdviceBaseDto.getUnitCode()); // 单位
                    chargeItem.setUnitPrice(advicePriceDto.getPrice()); // 单价
                    // 计算总价,保留4位小数
                    BigDecimal qty = new BigDecimal(quantity);
                    chargeItem.setTotalPrice(qty.multiply(advicePriceDto.getPrice()).setScale(4, RoundingMode.HALF_UP)); // 总价

                    iChargeItemService.save(chargeItem);
                    chargeItemIdList.add(chargeItem.getId().toString());
                }

            }
        }
        return chargeItemIdList;
    }

    private List<ChargeItem> getChargeItems(List<Long> chargeItemIds) {
        return chargeItemService.list(new LambdaQueryWrapper<ChargeItem>().in(ChargeItem::getId, chargeItemIds));
    }

    private PaymentedItemModel ConverterToPaymenItemModel(List<EncounterDiagnosis> diagList, ChargeItem chargeItem) {
        PaymentedItemModel piModel = new PaymentedItemModel();
        piModel.setBusNo(chargeItem.getBusNo());

        EncounterDiagnosis encounterDiagnosis;
        Optional<EncounterDiagnosis> optional =
            diagList.stream().filter(e -> e.getId().equals(chargeItem.getEncounterDiagnosisId())).findFirst();

        if (optional.isPresent()) {
            encounterDiagnosis = optional.get();
        } else {
            throw new ServiceException(
                "诊断信息与收费项的诊断信息未对应,错误信息：费用项" + chargeItem.getBusNo() + chargeItem.getEncounterDiagnosisId());
        }
        piModel.setMedType(encounterDiagnosis.getMedTypeCode());
        piModel.setTotalPrice(chargeItem.getTotalPrice());
        piModel.setEncounterId(chargeItem.getEncounterId());
        piModel.setChargeItemId(chargeItem.getId());
        return piModel;
    }

    @Override
    public R<?> unPrePayment(Long paymentId) {

        PaymentReconciliation byId = iPaymentReconciliationService.getById(paymentId);

        List<PaymentRecDetail> paymentRecDetails = iPaymentRecDetailService
            .list(new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId, paymentId));
        if (paymentRecDetails.isEmpty() || byId == null) {
            throw new ServiceException("未查询到收费详情");
        }
        List<String> payTransNoList =
            paymentRecDetails.stream().map(PaymentRecDetail::getPayTransNo).distinct().collect(Collectors.toList());

        for (String s : payTransNoList) {
            ybManager.unPreSettle(byId.getEncounterId(), s);
        }
        return R.ok("取消预结算");
    }
}
