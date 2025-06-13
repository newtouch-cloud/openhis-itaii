package com.openhis.financial.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.PaymentStatus;
import com.openhis.common.enums.ybenums.YbMdtrtCertType;
import com.openhis.common.enums.ybenums.YbMedType;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.mapper.PaymentReconciliationMapper;
import com.openhis.financial.model.PaymentResult;
import com.openhis.financial.model.PaymentedItemModel;
import com.openhis.financial.model.PrePaymentResult;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.yb.domain.ClinicSettle;
import com.openhis.yb.dto.Clinic2206OrderOutput;
import com.openhis.yb.dto.ClinicReg2201Output;
import com.openhis.yb.model.Clinic2207OrderModel;
import com.openhis.yb.service.YbManager;

/**
 * 付款管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PaymentReconciliationServiceImpl extends ServiceImpl<PaymentReconciliationMapper, PaymentReconciliation>
    implements IPaymentReconciliationService {
    @Resource
    private AssignSeqUtil assignSeqUtil;
    @Autowired
    private PaymentReconciliationMapper paymentReconciliationMapper;
    @Autowired
    private YbManager ybManager;

    /**
     * 根据支付id获取对应收费项目的id列表
     *
     * @param paymentIdList 支付id列表
     * @return 收费项目的id列表
     */
    @Override
    public List<Long> getChargeItemIdListByPayment(List<Long> paymentIdList) {

        // 根据支付id获取支付信息
        List<PaymentReconciliation> paymentReconciliationList =
            paymentReconciliationMapper.selectList(new LambdaQueryWrapper<PaymentReconciliation>()
                .select(PaymentReconciliation::getChargeItemIds).in(PaymentReconciliation::getId, paymentIdList));
        if (paymentReconciliationList.isEmpty()) {
            return null;
        }
        // 拆解所有的chargeItemId，拼装成一个集合
        List<String> chargeItemIdList = paymentReconciliationList.stream().map(PaymentReconciliation::getChargeItemIds)
            .collect(Collectors.toList());
        List<Long> chargeItemIds = new ArrayList<>();
        for (String chargeItemId : chargeItemIdList) {
            if (StringUtils.isNotEmpty(chargeItemId)) {
                chargeItemIds.addAll(Arrays
                    .stream(
                        chargeItemId.replaceAll("\\[", "").replaceAll("\\]", "").split(CommonConstants.Common.COMMA))
                    .map(Long::parseLong).collect(Collectors.toList()));
            }
        }
        // 将收费项目集合转换成列表
        return chargeItemIds;
    }

    /**
     * 更新付款状态：退款中
     *
     * @param paymentIdList 支付id列表
     */
    @Override
    public void updateRefundingStatus(List<Long> paymentIdList) {
        baseMapper.update(new PaymentReconciliation().setStatusEnum(PaymentStatus.REFUNDING.getValue()),
            new LambdaUpdateWrapper<PaymentReconciliation>().in(PaymentReconciliation::getId, paymentIdList));
    }

    /**
     * // 对chargeItem分付款方式 2025/05/23 ChargeItem分完付款方式之后，医保支付的ChargeItem还要分特慢病和普通门诊，特慢病等支付与常规门诊支付传参不一样
     */
    @Override
    public PrePaymentResult prePayment(YbMdtrtCertType ybMdtrtCertType, String busiCardInfo, String contractBusNo,
        List<PaymentedItemModel> paymentedItemList) {

        PrePaymentResult prePaymentResult = null;
        String ybSwitchFlag = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH);
        if (ybSwitchFlag.equals("0")) {
            contractBusNo = "0000";
        }
        switch (contractBusNo) {
            case "0000":
                prePaymentResult = new PrePaymentResult();
                BigDecimal sumAmount = BigDecimal.ZERO;
                for (PaymentedItemModel item : paymentedItemList) {
                    sumAmount = sumAmount.add(item.getTotalPrice());
                }
                prePaymentResult.setMedfeeSumamt(sumAmount);
                prePaymentResult.setFulamtOwnpayAmt(sumAmount);
                prePaymentResult.setPsnPartAmt(sumAmount);
                prePaymentResult.setPsnCashPay(sumAmount);
                prePaymentResult.setChrgBchno("0000");
                break;
            case "229900":// 吉林省医保
            case "220199":// 长春市医保
                Map<String, List<PaymentedItemModel>> collect =
                    paymentedItemList.stream().collect(Collectors.groupingBy(PaymentedItemModel::getMedType));
                PrePaymentResult result;
                for (Map.Entry<String, List<PaymentedItemModel>> medTypeKV : collect.entrySet()) {
                    Clinic2206OrderOutput clinic2206OrderOutput = null;
                    ClinicReg2201Output reg2201Output = null;
                    if (!medTypeKV.getKey().equals(YbMedType.GENERAL_OUTPATIENT.getValue()) && "1".equals(
                        SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH))) {
                        // 不是普通门诊就诊类型，补充挂号信息
                        reg2201Output = ybManager.createRegWithMedType(ybMdtrtCertType, busiCardInfo,
                            medTypeKV.getValue().get(0).getEncounterId(),
                            YbMedType.getByValue(medTypeKV.getValue().get(0).getMedType()));
                        reg2201Output.setMedType(medTypeKV.getKey());//2025/06/05慢病挂号后上传2206信息报错，2201返回值没有medType，此处更正赋值
                    } else {
                        // 从数据库里取reg2201
                        reg2201Output =
                            ybManager.getClinicRegByEncounterId(medTypeKV.getValue().get(0).getEncounterId());
                    }
                    // 调用预结算
                    clinic2206OrderOutput = ybManager.preSettle(medTypeKV.getValue().get(0).getEncounterId(),
                        ybMdtrtCertType, busiCardInfo, reg2201Output, "01", medTypeKV.getValue().stream()
                            .map(PaymentedItemModel::getChargeItemId).collect(Collectors.toList()));
                    prePaymentResult = new PrePaymentResult();
                    BeanUtils.copyProperties(clinic2206OrderOutput, prePaymentResult);
                }
                break;
            default:
                break;
        }
        return prePaymentResult;
    }

    @Override
    public PaymentResult settle(String payTransNo, YbMdtrtCertType ybMdtrtCertType, String busiCardInfo,
        Integer minpacuntDrugTracCnt, Integer mcsTracCnt) {
        PaymentResult paymentResult = null;
        if (!"0000".equals(payTransNo)) {
            // 医保支付 todo：支付的详细信息在于结算中已经存好了，但是还没有存储卡余额
            Clinic2207OrderModel clinic2207OrderModel =
                ybManager.settle(payTransNo, busiCardInfo, ybMdtrtCertType, minpacuntDrugTracCnt, mcsTracCnt);
            paymentResult = new PaymentResult();
            BeanUtils.copyProperties(clinic2207OrderModel, paymentResult);
        } else {
            //自费返回new实体
            paymentResult = new PaymentResult();
        }
        return paymentResult;
    }

    @Override
    public void updatePaymentStatusById(Long id, PaymentStatus success) {
        paymentReconciliationMapper.update(new PaymentReconciliation().setStatusEnum(success.getValue()),
            new LambdaQueryWrapper<PaymentReconciliation>().eq(PaymentReconciliation::getId, id));
    }

    @Override
    public void updatePaymentStatusAndSettleIdsById(Long id, PaymentStatus success, List<String> settleId) {
        if (settleId.isEmpty()) {
            paymentReconciliationMapper.update(new PaymentReconciliation().setStatusEnum(success.getValue()),
                new LambdaQueryWrapper<PaymentReconciliation>().eq(PaymentReconciliation::getId, id));
        } else {
            paymentReconciliationMapper.update(
                new PaymentReconciliation().setStatusEnum(success.getValue())
                    .setYbSettleIds(String.join(",", settleId)),
                new LambdaQueryWrapper<PaymentReconciliation>().eq(PaymentReconciliation::getId, id));
        }
    }

    /**
     * 初始化一个医保结算信息
     *
     * @return 预结算的参数
     */
    private ClinicSettle initClinic2206OrderResult() {
        ClinicSettle clinicSettle = new ClinicSettle();
        return clinicSettle.setMedfeeSumamt(new BigDecimal("0.0")).setFulamtOwnpayAmt(new BigDecimal("0.0"))
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
}
