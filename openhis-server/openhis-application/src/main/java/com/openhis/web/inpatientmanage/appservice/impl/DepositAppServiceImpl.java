package com.openhis.web.inpatientmanage.appservice.impl;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.redis.RedisCache;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Account;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.domain.Invoice;
import com.openhis.administration.service.IAccountService;
import com.openhis.administration.service.IEncounterService;
import com.openhis.administration.service.IInvoiceService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.enums.ybenums.YbPayment;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.web.inpatientmanage.appservice.IDepositAppService;
import com.openhis.web.inpatientmanage.dto.DepositDetailDto;
import com.openhis.web.inpatientmanage.dto.DepositInitPageDto;
import com.openhis.web.inpatientmanage.dto.DepositSearchParam;
import com.openhis.web.inpatientmanage.mapper.DepositMapper;
import com.openhis.web.paymentmanage.dto.PaymentDetailDto;
import com.openhis.web.paymentmanage.dto.PaymentDto;

/**
 * 预交金管理 实现类
 *
 * @author gyy
 * @since 2025/05/19
 */
@Service
public class DepositAppServiceImpl implements IDepositAppService {

    @Resource
    private DepositMapper depositMapper;

    @Resource
    private IEncounterService iEncounterService;

    @Resource
    private AssignSeqUtil assignSeqUtil;

    @Resource
    private RedisCache redisCache;

    @Resource
    private IPaymentReconciliationService paymentReconciliationService;

    @Resource
    private IInvoiceService iInvoiceService;

    @Resource
    private IPaymentRecDetailService paymentRecDetailService;

    @Resource
    private IAccountService iAccountService;

    /**
     * 获取预交金信息初期数据(病人信息)列表
     *
     * @return 预交金信息初期数据列表
     */
    @Override
    public R<?> getDepositInfoInit() {
        DepositInitPageDto initDto = new DepositInitPageDto();
        return R.ok(initDto);
    }

    /**
     * 获取预交金信息 分页显示
     *
     * @param depositSearchParam 查询参数
     * @param searchKey 模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 患者预交金信息
     */
    @Override
    public R<?> getDepositInfoPage(DepositSearchParam depositSearchParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<DepositDetailDto> queryWrapper = HisQueryUtils.buildQueryWrapper(depositSearchParam, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientId)), request);

        // 分页查询,查询患者预交金
        IPage<DepositDetailDto> depositInfoPage = depositMapper.getPage(new Page<>(pageNo, pageSize),
            EncounterClass.IMP.getValue(), LocationForm.BED.getValue(), queryWrapper);

        depositInfoPage.getRecords().forEach(e -> {
            // 性别枚举类回显赋值
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAgeString(AgeCalculatorUtil.getAge(e.getBirthDate()));
            // 支付状态
            e.setPaymentEnum_enumText(EnumUtils.getInfoByValue(PaymentStatus.class, e.getPaymentEnum()));
            // 票据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(ChargeItemStatus.class, e.getStatusEnum()));
        });

        // 返回【患者预交金信息列表DTO】分页
        return R.ok(depositInfoPage);
    }

    /**
     * 付款
     *
     * @param paymentDto 入参
     * @return 结果
     */
    @Override
    public R<?> savePayment(PaymentDto paymentDto) {
        // 收款员id
        Long enterId = SecurityUtils.getLoginUser().getPractitionerId();

        // 查询就诊信息（获取就诊ID）
        Encounter encounter = iEncounterService
            .getOne(new LambdaQueryWrapper<Encounter>().eq(Encounter::getId, paymentDto.getEncounterId()));

        // 就诊账户管理只取回来一条数据？
        //List<Account> accountList = iAccountService.list(new LambdaQueryWrapper<Account>()
        //   .in(Account::getId, paymentDto.getPatientId()).eq(Account::getEncounterId, paymentDto.getEncounterId()));
        Account account = new Account();
        Long accountId = null;
        boolean updateAccountSuccess = true;
        //if (accountList.size() != 0) {
        //    account.setBalanceAmount(accountList.get(0).getBalanceAmount().add(paymentDto.getDisplayAmount()))
        //        .setPatientId(paymentDto.getPatientId()).setEncounterId(encounter.getId());
        //    // 更新就诊账户管理
        //    updateAccountSuccess = iAccountService.saveOrUpdateAccount(account);
            // 账户ID
        //    accountId = accountList.get(0).getId();
        //} else {
        //    // 插入就诊账户管理
        //    account.setTypeCode(AccountType.PERSONAL_CASH_ACCOUNT.getInfo()).setPatientId(paymentDto.getPatientId())
        //        .setEncounterId(encounter.getId()).setBalanceAmount(paymentDto.getDisplayAmount());
        //    // 账户ID
        //    accountId = iAccountService.saveAccountByRegister(account);
        //}

        // 结算
        // 统一生成业务流水
        String paymentNo = assignSeqUtil.getSeqByDay(AssignSeqEnum.PAYMENT_NO.getPrefix(), 20);

        // 获取预结算时的收费批次号
        String chrgBchno = redisCache.getCacheObject("PRE-SETTLE:PRE_SETTLE_" + paymentDto.getId());
        if (chrgBchno == null) {
            throw new ServiceException("请重新进行预交费");
        }
        // 处理时间
        Date setlTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(setlTime);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        Date futureTime = calendar.getTime();

        // 新增支付信息
        PaymentReconciliation payment = new PaymentReconciliation();
        payment.setStatusEnum(PaymentStatus.SUCCESS.getValue()).setPaymentNo(paymentNo)
            .setPaymentEnum(PaymentType.PAY.getValue()).setPaymentReconciliationId(encounter.getPatientId())
            .setKindEnum(PaymentKind.HOSPITAL_DEPOSIT.getValue()).setEntererId(enterId)
            .setPatientId(encounter.getPatientId()).setPractitionerId(enterId)
            .setOutcomeEnum(PaymentOutcome.PARTIAL.getCode()).setLocationId(-99l).setExpirationDate(futureTime)
            //.setBillDate(setlTime).setPrintCount(0).setTenderedAmount(paymentDto.getTenderedAmount())
            .setEncounterId(encounter.getId());
        // 保存付款信息
        paymentReconciliationService.save(payment);
        // 保存付款详情
        this.savePaymentDetail(accountId, payment, paymentDto.getPaymentDetails());
        // 生成发票信息
        Invoice invoice = new Invoice();
        invoice.setPatientId(encounter.getPatientId()).setStatusEnum(InvoiceStatus.DRAFT)
            .setReconciliationId(payment.getId()).setStatusEnum(InvoiceStatus.ISSUED)// 待结算
            .setTypeCode(InvoiceType.ISSUING_INVOICES.getValue());
        iInvoiceService.save(invoice);

        if (updateAccountSuccess) {
            return R.ok(payment, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"预收金"}));
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
    }

    /**
     * 保存付款详情
     *
     * @param payment 付款实体
     * @param paymentDetails 付款详情
     */
    private void savePaymentDetail(long accountId, PaymentReconciliation payment,
        List<PaymentDetailDto> paymentDetails) {
        // 保存付款详情
        List<PaymentRecDetail> paymentRecDetails = new ArrayList<>();
        PaymentRecDetail paymentRecDetail;
        // 处理时间
        Date setlTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(setlTime);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        Date futureTime = calendar.getTime();

        for (PaymentDetailDto paymentDetail : paymentDetails) {
            paymentRecDetail = new PaymentRecDetail();
            if (YbPayment.SELF_CASH_VX_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_VX_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_VX_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue()).setPayTransDate(futureTime).setAccountId(accountId)
                    .setAfterBalance(paymentDetail.getAmount());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_ALI_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_ALI_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_ALI_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue()).setPayTransDate(futureTime).setAccountId(accountId)
                    .setAfterBalance(paymentDetail.getAmount());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_UNION_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId())
                    .setPayEnum(YbPayment.SELF_CASH_UNION_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_UNION_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue()).setPayTransDate(futureTime).setAccountId(accountId)
                    .setAfterBalance(paymentDetail.getAmount());
                paymentRecDetails.add(paymentRecDetail);
            }
            if (YbPayment.SELF_CASH_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail = new PaymentRecDetail();
                paymentRecDetail.setReconciliationId(payment.getId()).setPayEnum(YbPayment.SELF_CASH_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue()).setPayTransDate(futureTime).setAccountId(accountId)
                    .setAfterBalance(paymentDetail.getAmount());
                paymentRecDetails.add(paymentRecDetail);
            }
        }
        paymentRecDetailService.saveBatch(paymentRecDetails);
    }
}
