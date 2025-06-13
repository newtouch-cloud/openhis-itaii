/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.appservice.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.*;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.ybenums.YbEncounterClass;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhis.common.enums.*;
import com.openhis.common.enums.ybenums.YbPayment;
import com.openhis.config.HttpConfig;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.web.paymentmanage.appservice.IEleInvoiceService;
import com.openhis.web.paymentmanage.dto.*;
import com.openhis.web.paymentmanage.mapper.EleInvoiceMapper;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.service.IClinicSettleService;
import com.openhis.yb.service.IRegService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.core.common.core.domain.R;
import com.openhis.common.constant.PromptMsgConstant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 电子发票接口Service
 *
 * @author yuxj
 * @date 2025-04-22
 */
@Component
@Slf4j
public class EleInvoiceServiceImpl implements IEleInvoiceService {

    @Resource
    EleInvoiceMapper eleInvoiceMapper;
    @Resource
    IInvoiceService invoiceService;
    @Resource
    IPractitionerService practitionerService;

    @Resource
    private AssignSeqUtil assignSeqUtil;
    @Resource
    IRegService regService;
    @Resource
    IClinicSettleService clinicSettleService;
    @Resource
    IPaymentReconciliationService paymentReconciliationService;
    @Resource
    IPaymentRecDetailService paymentRecDetailService;
    @Resource
    IChargeItemService chargeItemService;
    @Resource
    IEncounterService encounterService;

    @Autowired
    private HttpConfig httpConfig;
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    /**
     * 电子票据补开接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return
     */
    public R<?> invoiceReissue(Long paymentId, Long encounterId) {
        // 获取付款信息
        PaymentReconciliation paymentReconciliation = paymentReconciliationService.getById(paymentId);
        List<Long> idList = Arrays.stream(paymentReconciliation.getChargeItemIds().split(","))
            .map(String::trim)  // 去除可能的空格
            .map(Long::parseLong)  // 转换为 Long
            .collect(Collectors.toList());
        // 获取费用项信息
        List<ChargeItem> chargeItem = chargeItemService.getChargeItemInfo(idList);

        for (ChargeItem item : chargeItem) {
            // 号源表
            if (Objects.equals(item.getServiceTable(), CommonConstants.TableName.ADM_HEALTHCARE_SERVICE)) {
                //挂号发票
                return R.ok(YbEncounterClass.REG.getValue());
            }
        }
        //获取就诊信息
        Encounter encounter = encounterService.getById(encounterId);
        if (Objects.equals(encounter.getClassEnum(), EncounterClass.AMB.getValue())) {
            //门诊发票
            return R.ok(YbEncounterClass.AMB.getValue());
        } else if (Objects.equals(encounter.getClassEnum(), EncounterClass.IMP.getValue())) {
            //住院发票
            return R.ok(YbEncounterClass.IMP.getValue());
        }

        return R.ok("0");
    }

    /**
     * 医疗挂号电子票据开具接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return 返回值
     */
    public R<?> invoiceRegMake(Long paymentId, Long encounterId) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());

        // 定义输入格式（带时区）
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        DecimalFormat df = new DecimalFormat();
        df.setMaximumIntegerDigits(16);
        df.setMinimumIntegerDigits(1); // 至少显示1位整数
        df.setMaximumFractionDigits(6); // 最多显示6位小数
        df.setGroupingUsed(false); // 不使用千位分隔符
        try {
            // 关键校验：检查必填参数encounterId是否存在，缺失则直接返回错误
            if (encounterId == null || encounterId == 0) {
                return R.fail(PromptMsgConstant.Common.M00013);
            }

            // 获取患者信息
            EleInvoicePatientInfoDto patientInfo = eleInvoiceMapper.getPatientInfo(encounterId,
                EncounterClass.AMB.getValue(), OrganizationClass.CLINIC.getValue());
            // 获取付款信息
            EleInvoicePaymentInfoDto paymentInfo = eleInvoiceMapper.getPaymentInfo(paymentId, encounterId,
                YbPayment.SELF_YB_ZH_PAY.getValue(), YbPayment.SELF_CASH_PAY.getValue(),
                YbPayment.SELF_CASH_VX_VALUE.getValue(), YbPayment.SELF_CASH_ALI_VALUE.getValue(),
                YbPayment.SELF_CASH_UNION_VALUE.getValue(), YbPayment.YB_FUND_PAY.getValue(),
                YbPayment.OTHER_PAY.getValue(), YbPayment.SELF_YB_ZH_GJ_VALUE.getValue());
            // 医保结算记录
            InvoiceBaseInfoDto clinicSettle = this.getClinicSettleByPaymentId(paymentInfo.getPaymentId());

            // 业务状态校验
            if (paymentInfo.getPaymentStatus() == null
                || paymentInfo.getPaymentStatus().equals(PaymentStatus.DRAFT.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00002);
            }
            if (!paymentInfo.getPaymentStatus().equals(PaymentStatus.SUCCESS.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00006);
            }
            if (paymentInfo.getInvoiceStatus() != null
                && paymentInfo.getInvoiceStatus().equals(InvoiceStatus.ISSUED.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00003);
            }

            // --------------------请求业务参数 data--------------------START
            JSONObject bill = commomSet(patientInfo, paymentInfo, clinicSettle);

            // ------票据信息------
            // busType 业务标识 String 20 是 06，标识挂号
            bill.put("busType", "06");
            // checker 票据复核人 String 100 是
            bill.put("checker", user.getName());

            // ------就诊信息------
            // patientCategory 就诊科室名称 String 50 是
            bill.put("patientCategory", patientInfo.getPatientCategory());
            // patientCategoryCode 就诊科室编码 String 50 是
            bill.put("patientCategoryCode", patientInfo.getPatientCategoryCode());

            // ------费用------
            // 个人现金支付
            clinicSettle.setPsnCashPay(clinicSettle.getPsnCashPay() == null ? ZERO : clinicSettle.getPsnCashPay());

            // otherInfo 其它扩展信息列表 JSONArray 不限 是
            JSONArray otherInfos = new JSONArray();
            // 公务员医疗补助资金支出:1.00;企业补充医疗保险基金支出:1.00;居民大病保险资金支出:1.00;职工大额医疗费用补助基金支出:1.00;医疗救助基金支出:1.00;医院负担金额:1.00;其他支出:1.00
            JSONObject otherInfo = new JSONObject();
            otherInfo.put("infoNo", "1");
            otherInfo.put("infoName", "公务员医疗补助资金支出");
            otherInfo.put("infoValue", clinicSettle.getHifesPay() == null ? ZERO : clinicSettle.getHifesPay());
            otherInfos.add(otherInfo);
            bill.put("otherInfo", otherInfos);

            // ------项目------
            // chargeDetail 收费项目明细 JSONArray 不限 是
            // 获取收费项目明细
            // 医疗收费项目类别 13：挂号费
            List<String> ybTypeList = new ArrayList<>(Arrays.asList("13"));
            // 付款账单集合
            List<Long> chargeItemIds = new ArrayList<>();
            String[] parts = paymentInfo.getChargeItemIds().split(",");
            for (String part : parts) {
                chargeItemIds.add(Long.parseLong(part.trim())); // trim() 去除空格
            } // todo 若挂号绑诊察的话，方法需要改
            List<EleInvoiceChargeDetailDto> chargeItems = eleInvoiceMapper.getChargeDetail(encounterId, ybTypeList,
                "med_chrgitm_type", ContrastTypeEnum.INVOICE_CLINIC.getValue(), chargeItemIds);

            JSONArray chargeDetails = new JSONArray();
            Integer sortNo = 1;
            for (EleInvoiceChargeDetailDto detail : chargeItems) {
                JSONObject chargeDetail = new JSONObject();
                // sortNo 序号 Integer 不限 是
                chargeDetail.put("sortNo", sortNo);
                // chargeCode 收费项目代码 String 50 是
                chargeDetail.put("chargeCode", detail.getChargeCode());
                // chargeName 收费项目名称 String 100 是
                chargeDetail.put("chargeName", detail.getChargeName());
                // unit 计量单位
                chargeDetail.put("unit", "项");
                // std 收费标准 Number 14,2 是
                BigDecimal std =
                    detail.getAmt().divide(new BigDecimal(detail.getNumber().toString()), 2, RoundingMode.HALF_UP);
                // 精确表示，避免科学计数法
                chargeDetail.put("std", df.format(std));
                // number 数量 Number 14,2 是
                chargeDetail.put("number", detail.getNumber());
                // amt 金额 Number 14,2 是
                chargeDetail.put("amt", df.format(detail.getAmt()));
                // selfAmt 自费金额 Number 14,2 是
                chargeDetail.put("selfAmt", ZERO);
                sortNo++;
                chargeDetails.add(chargeDetail);
            }
            bill.put("chargeDetail", chargeDetails);

            // listDetail 清单项目明细 JSONArray 不限 是
            JSONArray listDetails = new JSONArray();
            // 获取清单项目明细
            List<EleInvoiceListDetailDto> details = eleInvoiceMapper.getRegListDetail(encounterId, ybTypeList,
                "med_chrgitm_type", ContrastTypeEnum.INVOICE_CLINIC.getValue(), chargeItemIds);

            for (EleInvoiceListDetailDto detail : details) {
                JSONObject listDetail = new JSONObject();
                // listDetailNo 明细流水号 String 60 否
                listDetail.put("listDetailNo", detail.getListDetailNo());
                // chargeCode 收费项目代码 String 50 是
                if (detail.getChargeCode() == null) {
                    listDetail.put("chargeCode", "YLMZ11");// 挂号费
                } else {
                    listDetail.put("chargeCode", detail.getChargeCode());
                }
                // chargeName 收费项目名称 String 100 是
                listDetail.put("chargeName", detail.getChargeName());
                // code 药品编码 String 30 是
                listDetail.put("code", detail.getCode() == null ? detail.getYbCode() : detail.getCode());
                // name 药品名称 String 100 是
                listDetail.put("name", detail.getName());
                // unit 计量单位 String 20 是
                listDetail.put("unit", "次");
                // std 单价 Number 14,4 是
                listDetail.put("std", df.format(detail.getStd()));
                // number 数量 Number 14,2 是
                listDetail.put("number", detail.getNumber());
                // amt 金额 Number 14,4 是
                listDetail.put("amt", df.format(detail.getAmt()));
                // selfAmt 自费金额 Number 14,4 是
                listDetail.put("selfAmt", ZERO);
                // remark 备注 1 甲类 2 乙类 3 自费 todo 暂时取不到
                // listDetail.put("remark", detail.getMedicalCareType());
                listDetails.add(listDetail);
            }
            bill.put("listDetail", listDetails);
            // --------------------请求业务参数 data--------------------END

            // 初始化返回结果
            JSONObject redata = new JSONObject();
            String redata64;
            String srcdata;
            String srcmsg;

            System.out.println(JSON.toJSONString(bill));
            JSONObject resobj = PreInvoicePost(bill, "api/medical/invEBillRegistration");
            if (resobj.getBooleanValue("success")) {
                JSONObject rejson = resobj.getJSONObject("result");

                if (rejson.getString("result").equals("S0000")) {
                    // 返回结果内容
                    redata64 = rejson.getString("message").toString();
                    srcdata = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    redata = JSONObject.parseObject(srcdata);

                    // 获取发票管理表信息
                    Invoice invoice = invoiceService.getById(paymentInfo.getInvoiceId());
                    // 状态
                    invoice.setStatusEnum(InvoiceStatus.ISSUED);
                    // 开票员
                    invoice.setInvoicingStaffId(user.getUserId());
                    // 电子票据代码
                    invoice.setBillBatchCode(redata.getString("billBatchCode"));
                    // 电子票据号码
                    invoice.setBillNo(redata.getString("billNo"));
                    // 电子校验码
                    invoice.setRandom(redata.getString("random"));
                    // 电子票据生成时间
                    invoice.setBillCreateTime(redata.getString("createTime"));
                    // 电子票据二维码图片数据
                    invoice.setBillQrCode(redata.getString("billQRCode"));
                    // 电子票据H5页面URL
                    invoice.setPictureUrl(redata.getString("pictureUrl"));
                    // 电子票据外网H5页面URL
                    invoice.setPictureNetUrl(redata.getString("pictureNetUrl"));
                    // 票据营业日期
                    invoice.setBillBusDate(inputFormat.parse(redata.getString("billBusDate")));

                    boolean flg = invoiceService.updateById(invoice);
                    if (!flg) {
                        return R.fail(PromptMsgConstant.Common.M00011);
                    }

                    return R.ok(invoice,
                        MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"电子发票做成"}));
                } else {
                    redata.put("result", rejson.getString("result"));
                    redata64 = rejson.getString("message").toString();
                    srcmsg = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    return R.fail(srcmsg);
                }
            } else {
                return R.fail(resobj.getString("msg"));
            }
        } catch (Exception ex) {
            try {
                return R.fail(ex.getCause().getMessage());
            } catch (Exception ex1) {
                return R.fail(ex.getMessage());
            }
        }
    }

    /**
     * 医疗门诊电子票据开具接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return 返回值
     */
    public R<?> invoiceMZMake(Long paymentId, Long encounterId) {

        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());

        // 定义输入格式（带时区）
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        DecimalFormat df = new DecimalFormat();
        df.setMaximumIntegerDigits(16);
        df.setMinimumIntegerDigits(1); // 至少显示1位整数
        df.setMaximumFractionDigits(6); // 最多显示6位小数
        df.setGroupingUsed(false); // 不使用千位分隔符

        try {
            // 关键校验：检查必填参数encounterId是否存在，缺失则直接返回错误
            if (encounterId == null || encounterId == 0) {
                return R.fail(PromptMsgConstant.Common.M00013);
            }

            // 获取患者信息
            EleInvoicePatientInfoDto patientInfo = eleInvoiceMapper.getPatientInfo(encounterId,
                EncounterClass.AMB.getValue(), OrganizationClass.CLINIC.getValue());
            // 获取付款信息
            EleInvoicePaymentInfoDto paymentInfo = eleInvoiceMapper.getPaymentInfo(paymentId, encounterId,
                YbPayment.SELF_YB_ZH_PAY.getValue(), YbPayment.SELF_CASH_PAY.getValue(),
                YbPayment.SELF_CASH_VX_VALUE.getValue(), YbPayment.SELF_CASH_ALI_VALUE.getValue(),
                YbPayment.SELF_CASH_UNION_VALUE.getValue(), YbPayment.YB_FUND_PAY.getValue(),
                YbPayment.OTHER_PAY.getValue(), YbPayment.SELF_YB_ZH_GJ_VALUE.getValue());
            // 医保结算记录
            InvoiceBaseInfoDto clinicSettle = this.getClinicSettleByPaymentId(paymentInfo.getPaymentId());

            // 业务状态校验
            if (paymentInfo.getPaymentStatus() == null
                || paymentInfo.getPaymentStatus().equals(PaymentStatus.DRAFT.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00002);
            }
            if (!paymentInfo.getPaymentStatus().equals(PaymentStatus.SUCCESS.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00006);
            }
            if (paymentInfo.getInvoiceStatus() != null
                && paymentInfo.getInvoiceStatus().equals(InvoiceStatus.ISSUED.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00003);
            }

            // --------------------请求业务参数 data--------------------START
            JSONObject bill = commomSet(patientInfo, paymentInfo, clinicSettle);

            // ------票据信息------
            // busType 业务标识 String 20 是 直接填写业务系统内部编码值，由医疗平台配置对照，例如：附录5 业务标识列表
            // 值：02，标识门诊
            bill.put("busType", "02");

            // ------就诊信息------

            // patientCategory 就诊科室 String 60 是
            bill.put("patientCategory", patientInfo.getPatientCategory());
            // patientCategoryCode 就诊科室编码 String 60 是
            bill.put("patientCategoryCode", patientInfo.getPatientCategoryCode());
            // patientNo 患者就诊编号 String 30 是 患者每次就诊一次就生成的一个新的编号。（患者登记号）
            bill.put("patientNo", encounterId);
            // caseNumber 病历号 String 50 是
            bill.put("caseNumber", patientInfo.getCaseNumber());

            // ------费用------
            // 医保统筹基金支付
            clinicSettle.setHifpPay(clinicSettle.getHifpPay() == null ? ZERO : clinicSettle.getHifpPay());
            // 个人现金支付
            clinicSettle.setPsnCashPay(clinicSettle.getPsnCashPay() == null ? ZERO : clinicSettle.getPsnCashPay());
            // 医保报销总金额
            clinicSettle
                .setFundPaySumamt(clinicSettle.getFundPaySumamt() == null ? ZERO : clinicSettle.getFundPaySumamt());

            // otherfundPay 其它医保支付
            bill.put("otherfundPay",
                String.format("%.2f", clinicSettle.getFundPaySumamt().subtract(clinicSettle.getHifpPay())));
            // ownAcBalance 个人账户余额 Number 14,2 否
            bill.put("ownAcBalance", clinicSettle.getBalc());
            // balancedNumber 医保结算号 String 100 否 HIS和医保实时结算时，医保生成的唯一业务流水号
            bill.put("balancedNumber", clinicSettle.getSetlId() == null ? "" : clinicSettle.getSetlId());
            // otherInfo 其它扩展信息列表 JSONArray 不限 是 填写信息需要在电子票据上单独显示的其它扩展信息（未知信息）
            // 具体传值说明详见A-3,JSON格式列表
            JSONArray otherInfos = new JSONArray();
            // 公务员医疗补助资金支出:1.00;企业补充医疗保险基金支出:1.00;居民大病保险资金支出:1.00;职工大额医疗费用补助基金支出:1.00;医疗救助基金支出:1.00;医院负担金额:1.00;其他支出:1.00
            JSONObject otherInfo = new JSONObject();
            otherInfo.put("infoNo", "1");
            otherInfo.put("infoName", "公务员医疗补助资金支出");
            otherInfo.put("infoValue", clinicSettle.getCvlservPay() == null ? ZERO : clinicSettle.getCvlservPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "2");
            otherInfo.put("infoName", "企业补充医疗保险基金支出");
            otherInfo.put("infoValue", clinicSettle.getHifesPay() == null ? ZERO : clinicSettle.getHifesPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "3");
            otherInfo.put("infoName", "居民大病保险资金支出");
            otherInfo.put("infoValue", clinicSettle.getHifmiPay() == null ? ZERO : clinicSettle.getHifmiPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "4");
            otherInfo.put("infoName", "职工大额医疗费用补助基金支出");
            otherInfo.put("infoValue", clinicSettle.getHifobPay() == null ? ZERO : clinicSettle.getHifobPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "5");
            otherInfo.put("infoName", "医疗救助基金支出");
            otherInfo.put("infoValue", clinicSettle.getMafPay() == null ? ZERO : clinicSettle.getMafPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "6");
            otherInfo.put("infoName", "医院负担金额");
            otherInfo.put("infoValue", clinicSettle.getHospPartAmt() == null ? ZERO : clinicSettle.getHospPartAmt());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "1");
            otherInfo.put("infoName", "其他支出");
            otherInfo.put("infoValue", clinicSettle.getOthPay() == null ? ZERO : clinicSettle.getOthPay());
            otherInfos.add(otherInfo);
            bill.put("otherInfo", otherInfos);

            // ------项目------
            // 医疗收费项目类别
            // 01 床位费,02 诊察费,03 检查费,04 化验费,05 治疗费,06 手术费,07 护理费,08 卫生材料费,09 西药费,10 中药饮片费,11 中成药费,12 一般诊疗费,13 挂号费,14 其他费
            List<String> ybTypeList = new ArrayList<>(
                Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "14"));
            // 付款账单集合
            List<Long> chargeItemIds = new ArrayList<>();
            String[] parts = paymentInfo.getChargeItemIds().split(",");
            for (String part : parts) {
                chargeItemIds.add(Long.parseLong(part.trim())); // trim() 去除空格
            }

            // 获取收费项目明细
            List<EleInvoiceChargeDetailDto> chargeItems = eleInvoiceMapper.getChargeDetail(encounterId, ybTypeList,
                "med_chrgitm_type", ContrastTypeEnum.INVOICE_CLINIC.getValue(), chargeItemIds);

            // chargeDetail 收费项目明细 JSONArray 不限 是 详见A-1,JSON格式列表
            JSONArray chargeDetails = new JSONArray();
            Integer sortNo = 1;
            for (EleInvoiceChargeDetailDto detail : chargeItems) {
                JSONObject chargeDetail = new JSONObject();
                // sortNo 序号 Integer 不限 是
                chargeDetail.put("sortNo", sortNo);
                // chargeCode 收费项目代码 String 50 是
                chargeDetail.put("chargeCode", detail.getChargeCode());
                // chargeName 收费项目名称 String 100 是
                chargeDetail.put("chargeName", detail.getChargeName());
                // unit 计量单位
                chargeDetail.put("unit", "项");
                // std 收费标准 Number 14,2 是
                BigDecimal std =
                    detail.getAmt().divide(new BigDecimal(detail.getNumber().toString()), 2, RoundingMode.HALF_UP);
                chargeDetail.put("std", df.format(std));
                // number 数量 Number 14,2 是
                chargeDetail.put("number", detail.getNumber());
                // amt 金额 Number 14,2 是
                chargeDetail.put("amt", df.format(detail.getAmt()));
                // // selfAmt 自费金额 Number 14,2 是
                chargeDetail.put("selfAmt", ZERO);
                sortNo++;
                chargeDetails.add(chargeDetail);
            }
            bill.put("chargeDetail", chargeDetails);
            // listDetail 清单项目明细 JSONArray 不限 是 详见A-2,JSON格式列表
            JSONArray listDetails = new JSONArray();

            // 获取清单项目明细
            List<EleInvoiceListDetailDto> details = eleInvoiceMapper.getListDetail(encounterId, ybTypeList,
                "med_chrgitm_type", ContrastTypeEnum.INVOICE_CLINIC.getValue(), chargeItemIds);

            for (EleInvoiceListDetailDto detail : details) {
                JSONObject listDetail = new JSONObject();
                // listDetailNo 明细流水号 String 60 否
                listDetail.put("listDetailNo", detail.getListDetailNo());
                // chargeCode 收费项目代码 String 50 是
                listDetail.put("chargeCode", detail.getChargeCode());
                // chargeName 收费项目名称 String 100 是
                listDetail.put("chargeName", detail.getChargeName());
                // code 药品编码 String 30 是
                listDetail.put("code", detail.getCode() == null ? detail.getYbCode() : detail.getCode());
                // name 药品名称 String 100 是
                listDetail.put("name", detail.getName());
                // unit 计量单位 String 20 是
                listDetail.put("unit", detail.getUnit());
                // std 单价 Number 14,4 是
                listDetail.put("std", df.format(detail.getStd()));
                // number 数量 Number 14,2 是
                listDetail.put("number", detail.getNumber());
                // amt 金额 Number 14,4 是
                listDetail.put("amt", df.format(detail.getAmt()));
                // // selfAmt 自费金额 Number 14,4 是
                listDetail.put("selfAmt", ZERO);
                // medicalCareType 医保药品分类 String 1 否
                listDetail.put("medicalCareType", detail.getMedicalCareType());
                // medCareItemCode 医保项目编码 String 100 否
                listDetail.put("medCareItemCode", detail.getYbCode() == null ? "" : detail.getYbCode());
                // medCareItemName 医保项目名称 String 100 否
                listDetail.put("medCareItemName", detail.getName() == null ? "" : detail.getName());

                listDetails.add(listDetail);
            }
            bill.put("listDetail", listDetails);

            // --------------------请求业务参数 data--------------------END
            JSONObject redata = new JSONObject();
            String redata64;
            String srcdata;
            String srcmsg;
            System.out.println(JSON.toJSONString(bill));
            JSONObject resobj = PreInvoicePost(bill, "api/medical/invoiceEBillOutpatient");
            if (resobj.getBooleanValue("success")) {
                JSONObject rejson = resobj.getJSONObject("result");
                if (rejson.getString("result").equals("S0000")) {
                    // 保存发票信息
                    redata64 = rejson.getString("message").toString();
                    srcdata = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    redata = JSONObject.parseObject(srcdata);

                    // 获取发票管理表信息
                    Invoice invoice = invoiceService.getById(paymentInfo.getInvoiceId());
                    // 状态
                    invoice.setStatusEnum(InvoiceStatus.ISSUED);
                    // 开票员
                    invoice.setInvoicingStaffId(user.getUserId());
                    // 电子票据代码
                    invoice.setBillBatchCode(redata.getString("billBatchCode"));
                    // 电子票据号码
                    invoice.setBillNo(redata.getString("billNo"));
                    // 电子校验码
                    invoice.setRandom(redata.getString("random"));
                    // 电子票据生成时间
                    invoice.setBillCreateTime(redata.getString("createTime"));
                    // 电子票据二维码图片数据
                    invoice.setBillQrCode(redata.getString("billQRCode"));
                    // 电子票据H5页面URL
                    invoice.setPictureUrl(redata.getString("pictureUrl"));
                    // 电子票据外网H5页面URL
                    invoice.setPictureNetUrl(redata.getString("pictureNetUrl"));
                    // 票据营业日期
                    invoice.setBillBusDate(inputFormat.parse(redata.getString("billBusDate")));

                    boolean flg = invoiceService.updateById(invoice);
                    if (!flg) {
                        return R.fail(PromptMsgConstant.Common.M00011);
                    }

                    return R.ok(invoice,
                        MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"电子发票做成"}));
                } else {
                    redata.put("result", rejson.getString("result"));
                    redata64 = rejson.getString("message").toString();
                    srcmsg = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    return R.fail(srcmsg);

                }
            } else {
                return R.fail(resobj.getString("msg"));
            }
        } catch (Exception ex) {
            try {
                return R.fail(ex.getCause().getMessage());
            } catch (Exception ex1) {
                return R.fail(ex.getMessage());
            }
        }
    }

    /**
     * 医疗住院电子票据开具接口
     *
     * @param paymentId 支付ID
     * @param encounterId 就诊ID
     * @return 返回值
     */
    public R<?> invoiceZYMake(Long paymentId, Long encounterId) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());

        // 日期格式化：定义多种时间格式，用于生成业务流水号、时间戳等。
        SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 定义输入格式（带时区）
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        DecimalFormat df = new DecimalFormat();
        df.setMaximumIntegerDigits(16);
        df.setMinimumIntegerDigits(1); // 至少显示1位整数
        df.setMaximumFractionDigits(6); // 最多显示6位小数
        df.setGroupingUsed(false); // 不使用千位分隔符

        try {
            // 关键校验：检查必填参数encounterId是否存在，缺失则直接返回错误
            if (encounterId == null || encounterId == 0) {
                return R.fail(PromptMsgConstant.Common.M00013);
            }

            // 获取患者信息
            EleInvoicePatientInfoDto patientInfo = eleInvoiceMapper.getPatientInfo(encounterId,
                EncounterClass.IMP.getValue(), OrganizationClass.INPATIENT.getValue());
            // 获取付款信息
            EleInvoicePaymentInfoDto paymentInfo = eleInvoiceMapper.getPaymentInfo(paymentId, encounterId,
                YbPayment.SELF_YB_ZH_PAY.getValue(), YbPayment.SELF_CASH_PAY.getValue(),
                YbPayment.SELF_CASH_VX_VALUE.getValue(), YbPayment.SELF_CASH_ALI_VALUE.getValue(),
                YbPayment.SELF_CASH_UNION_VALUE.getValue(), YbPayment.YB_FUND_PAY.getValue(),
                YbPayment.OTHER_PAY.getValue(), YbPayment.SELF_YB_ZH_GJ_VALUE.getValue());
            // 医保结算记录
            InvoiceBaseInfoDto clinicSettle = this.getClinicSettleByPaymentId(paymentInfo.getPaymentId());

            // 业务状态校验
            if (paymentInfo.getPaymentStatus() == null
                || paymentInfo.getPaymentStatus().equals(PaymentStatus.DRAFT.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00002);
            }
            if (!paymentInfo.getPaymentStatus().equals(PaymentStatus.SUCCESS.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00006);
            }
            if (paymentInfo.getInvoiceStatus() != null
                && paymentInfo.getInvoiceStatus().equals(InvoiceStatus.ISSUED.getValue())) {
                return R.fail(PromptMsgConstant.invoice.M00003);
            }

            // --------------------请求业务参数 data--------------------START
            JSONObject bill = commomSet(patientInfo, paymentInfo, clinicSettle);

            // ------票据信息------
            // busType 业务标识 String 20 是 直接填写业务系统内部编码值，由医疗平台配置对照，例如：附录5 业务标识列表
            // 值：01，标识住院
            bill.put("busType", "01");
            // checker 票据复核人 String 100 是
            bill.put("checker", user.getName());

            // ------就诊信息------

            // medCareAreaCode 医保行政区划码 String 6 否 医保结算时必须填写
            bill.put("medCareAreaCode", patientInfo.getMedCareAreaCode());
            // category 入院科室名称 String 50 是 如：入院科室不存在，填写出院科室
            bill.put("category", patientInfo.getCategory());
            // categoryCode 入院科室编码 String 50 是 如：入院科室不存在，填写出院科室
            bill.put("categoryCode", patientInfo.getCategoryCode());
            // leaveCategory 出院科室名称 String 50 是
            bill.put("leaveCategory", patientInfo.getLeaveCategory());
            // leaveCategoryCode 出院科室编码 String 50 是
            bill.put("leaveCategoryCode", patientInfo.getLeaveCategoryCode());
            // patientId 患者唯一ID String 50 否
            bill.put("patientId", patientInfo.getPayerId());
            // patientNo 患者就诊编号 String 30 否
            bill.put("patientNo", encounterId);
            // caseNumber 病历号 String 50 是
            bill.put("caseNumber", patientInfo.getCaseNumber());
            // inHospitalDate 住院日期 String 10 是 格式:yyyy-MM-dd
            bill.put("inHospitalDate", sdfday.format(sdf2.parse(patientInfo.getInHospitalDate())));
            // outHospitalDate 出院日期 String 10 是 格式:yyyy-MM-dd
            bill.put("outHospitalDate", sdfday.format(sdf2.parse(patientInfo.getOutHospitalDate())));

            // ------费用------
            // 医保统筹基金支付
            clinicSettle.setHifpPay(clinicSettle.getHifpPay() == null ? ZERO : clinicSettle.getHifpPay());
            // 个人现金支付
            clinicSettle.setPsnCashPay(clinicSettle.getPsnCashPay() == null ? ZERO : clinicSettle.getPsnCashPay());
            // 医保报销总金额
            clinicSettle
                .setFundPaySumamt(clinicSettle.getFundPaySumamt() == null ? ZERO : clinicSettle.getFundPaySumamt());
            // otherfundPay 其它医保支付 Number 14,2 是
            bill.put("otherfundPay",
                String.format("%.2f", clinicSettle.getFundPaySumamt().subtract(clinicSettle.getHifpPay())));
            // balancedNumber 医保结算号 String 100 否
            bill.put("balancedNumber", clinicSettle.getSetlId() == null ? "" : clinicSettle.getSetlId());
            // otherInfo 其它扩展信息列表 JSONArray 不限 是
            JSONArray otherInfos = new JSONArray();
            // 公务员医疗补助资金支出:1.00;企业补充医疗保险基金支出:1.00;居民大病保险资金支出:1.00;职工大额医疗费用补助基金支出:1.00;医疗救助基金支出:1.00;医院负担金额:1.00;其他支出:1.00
            JSONObject otherInfo = new JSONObject();
            otherInfo.put("infoNo", "1");
            otherInfo.put("infoName", "公务员医疗补助资金支出");
            otherInfo.put("infoValue", clinicSettle.getCvlservPay() == null ? ZERO : clinicSettle.getCvlservPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "2");
            otherInfo.put("infoName", "企业补充医疗保险基金支出");
            otherInfo.put("infoValue", clinicSettle.getHifesPay() == null ? ZERO : clinicSettle.getHifesPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "3");
            otherInfo.put("infoName", "居民大病保险资金支出");
            otherInfo.put("infoValue", clinicSettle.getHifmiPay() == null ? ZERO : clinicSettle.getHifmiPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "4");
            otherInfo.put("infoName", "职工大额医疗费用补助基金支出");
            otherInfo.put("infoValue", clinicSettle.getHifobPay() == null ? ZERO : clinicSettle.getHifobPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "5");
            otherInfo.put("infoName", "医疗救助基金支出");
            otherInfo.put("infoValue", clinicSettle.getMafPay() == null ? ZERO : clinicSettle.getMafPay());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "6");
            otherInfo.put("infoName", "医院负担金额");
            otherInfo.put("infoValue", clinicSettle.getHospPartAmt() == null ? ZERO : clinicSettle.getHospPartAmt());
            otherInfos.add(otherInfo);

            otherInfo = new JSONObject();
            otherInfo.put("infoNo", "1");
            otherInfo.put("infoName", "其他支出");
            otherInfo.put("infoValue", clinicSettle.getOthPay() == null ? ZERO : clinicSettle.getOthPay());
            otherInfos.add(otherInfo);
            bill.put("otherInfo", otherInfos);

            // ------项目------
            // 医疗收费项目类别
            // 01 床位费,02 诊察费,03 检查费,04 化验费,05 治疗费,06 手术费,07 护理费,08 卫生材料费,09 西药费,10 中药饮片费,11 中成药费,12 一般诊疗费,13 挂号费,14 其他费
            List<String> ybTypeList = new ArrayList<>(
                Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14"));
            // 付款账单集合
            List<Long> chargeItemIds = new ArrayList<>();
            String[] parts = paymentInfo.getChargeItemIds().split(",");
            for (String part : parts) {
                chargeItemIds.add(Long.parseLong(part.trim())); // trim() 去除空格
            }
            // 获取清单项目明细
            List<EleInvoiceListDetailDto> details = eleInvoiceMapper.getListDetail(encounterId, ybTypeList,
                "med_chrgitm_type", ContrastTypeEnum.INVOICE_CINPATIENT.getValue(), chargeItemIds);

            JSONArray listDetails = new JSONArray();

            for (EleInvoiceListDetailDto detail : details) {
                JSONObject listDetail = new JSONObject();
                // listDetailNo 明细流水号 String 60 否
                listDetail.put("listDetailNo", detail.getListDetailNo());
                // chargeCode 收费项目代码 String 50 是
                listDetail.put("chargeCode", detail.getChargeCode());
                // chargeName 收费项目名称 String 100 是
                listDetail.put("chargeName", detail.getChargeName());
                // code 药品编码 String 30 是
                listDetail.put("code", detail.getCode() == null ? detail.getYbCode() : detail.getCode());
                // name 药品名称 String 100 是
                listDetail.put("name", detail.getName());
                // unit 计量单位 String 20 是
                listDetail.put("unit", detail.getUnit());
                // std 单价 Number 14,4 是
                listDetail.put("std", df.format(detail.getStd()));
                // number 数量 Number 14,2 是
                listDetail.put("number", detail.getNumber());
                // amt 金额 Number 14,4 是
                listDetail.put("amt", df.format(detail.getAmt()));
                // // selfAmt 自费金额 Number 14,4 是
                listDetail.put("selfAmt", ZERO);
                // medicalCareType 医保药品分类 String 1 否
                listDetail.put("medicalCareType", detail.getMedicalCareType());
                // medCareItemCode 医保项目编码 String 100 否
                listDetail.put("medCareItemCode", detail.getYbCode() == null ? "" : detail.getYbCode());
                // medCareItemName 医保项目名称 String 100 否
                listDetail.put("medCareItemName", detail.getName() == null ? "" : detail.getName());

                listDetails.add(listDetail);
            }
            bill.put("listDetail", listDetails);

            // 获取收费项目明细
            List<EleInvoiceChargeDetailDto> chargeItems = eleInvoiceMapper.getChargeDetail(encounterId, ybTypeList,
                "med_chrgitm_type", ContrastTypeEnum.INVOICE_CINPATIENT.getValue(), chargeItemIds);

            // chargeDetail 收费项目明细 JSONArray 不限 是
            JSONArray chargeDetails = new JSONArray();

            Integer sortNo = 1;
            for (EleInvoiceChargeDetailDto detail : chargeItems) {
                JSONObject chargeDetail = new JSONObject();
                // sortNo 序号 Integer 不限 是
                chargeDetail.put("sortNo", sortNo);
                // chargeCode 收费项目代码 String 50 是
                chargeDetail.put("chargeCode", detail.getChargeCode());
                // chargeName 收费项目名称 String 100 是
                chargeDetail.put("chargeName", detail.getChargeName());
                // unit 计量单位
                chargeDetail.put("unit", "项");
                // std 收费标准 Number 14,2 是
                BigDecimal std =
                    detail.getAmt().divide(new BigDecimal(detail.getNumber().toString()), 2, RoundingMode.HALF_UP);
                // 精确表示，避免科学计数法
                chargeDetail.put("std", df.format(std));
                // number 数量 Number 14,2 是
                chargeDetail.put("number", detail.getNumber());
                // amt 金额 Number 14,2 是
                chargeDetail.put("amt", df.format(detail.getAmt()));
                // selfAmt 自费金额 Number 14,2 是
                chargeDetail.put("selfAmt", ZERO);
                sortNo++;
                chargeDetails.add(chargeDetail);
            }
            bill.put("chargeDetail", chargeDetails);

            // --------------------请求业务参数 data--------------------END

            JSONObject redata = new JSONObject();
            String redata64;
            String srcdata;
            String srcmsg;
            JSONObject resobj = PreInvoicePost(bill, "api/medical/invEBillHospitalized");
            if (resobj.getBooleanValue("success")) {
                JSONObject rejson = resobj.getJSONObject("result");
                if (rejson.getString("result").equals("S0000")) {
                    // 保存发票信息
                    redata64 = rejson.getString("message").toString();
                    srcdata = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    redata = JSONObject.parseObject(srcdata);

                    // 获取发票管理表信息
                    Invoice invoice = invoiceService.getById(paymentInfo.getInvoiceId());
                    // 状态
                    invoice.setStatusEnum(InvoiceStatus.ISSUED);
                    // 开票员
                    invoice.setInvoicingStaffId(user.getUserId());
                    // 电子票据代码
                    invoice.setBillBatchCode(redata.getString("billBatchCode"));
                    // 电子票据号码
                    invoice.setBillNo(redata.getString("billNo"));
                    // 电子校验码
                    invoice.setRandom(redata.getString("random"));
                    // 电子票据生成时间
                    invoice.setBillCreateTime(redata.getString("createTime"));
                    // 电子票据二维码图片数据
                    invoice.setBillQrCode(redata.getString("billQRCode"));
                    // 电子票据H5页面URL
                    invoice.setPictureUrl(redata.getString("pictureUrl"));
                    // 电子票据外网H5页面URL
                    invoice.setPictureNetUrl(redata.getString("pictureNetUrl"));
                    // 票据营业日期
                    invoice.setBillBusDate(inputFormat.parse(redata.getString("billBusDate")));

                    boolean flg = invoiceService.updateById(invoice);
                    if (!flg) {
                        return R.fail(PromptMsgConstant.Common.M00011);
                    }

                    return R.ok(invoice,
                        MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"电子发票做成"}));
                } else {
                    redata.put("result", rejson.getString("result"));
                    redata64 = rejson.getString("message").toString();
                    srcmsg = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    return R.fail(srcmsg);
                }
            } else {
                return R.fail(resobj.getString("msg"));
            }

        } catch (Exception ex) {
            try {
                return R.fail(ex.getCause().getMessage());
            } catch (Exception ex1) {
                return R.fail(ex.getMessage());
            }
        }
    }

    /**
     * 医疗电子票据开具共通赋值
     *
     * @param patientInfo 患者信息
     * @param paymentInfo 付款信息
     * @param clinicSettle 医保结算记录
     * @return 返回值
     */

    private JSONObject commomSet(EleInvoicePatientInfoDto patientInfo, EleInvoicePaymentInfoDto paymentInfo,
        InvoiceBaseInfoDto clinicSettle) throws ParseException {
        // 日期格式化：定义多种时间格式，用于生成业务流水号、时间戳等。
        SimpleDateFormat sdfday = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        // 定义输入格式（带时区）
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 设置时区

        DecimalFormat df = new DecimalFormat("#.00");
        df.setMaximumIntegerDigits(14);
        df.setMinimumIntegerDigits(1); // 至少显示1位整数

        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());

        JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();

        JSONObject bill = new JSONObject();

        // ------票据信息------
        // busNo 业务流水号 String 50 是
        bill.put("busNo", paymentInfo.getBusNo());
        // payer 患者姓名 String 100 是
        bill.put("payer", patientInfo.getPayer());
        // busDateTime 业务发生时间 String 17 是 格式：yyyyMMddHHmmssSSS
        //Date parse = inputFormat.parse(paymentInfo.getBillDate());
        //String format = sdf.format(parse);
        //ZonedDateTime parse1 = ZonedDateTime.parse(paymentInfo.getBillDate(), inputFormatter);
        //String format = parse1.format(outputFormatter);
        String replace = paymentInfo.getBillDate().replace("+08", "");
        Date date = DateUtils.parseDate(replace.split("\\.")[0]);

        bill.put("busDateTime", DateUtils.parseDateToStr("yyyyMMddHHmmssSSS",date));
        //bill.put("busDateTime", DateUtils.parseDate(paymentInfo.getBillDate(),"yyyyMMddHHmmssSSS"));
        // placeCode 开票点编码 String 50 是
        bill.put("placeCode", user.getKpdCode());
        // payee 收费员 String 50 是
        bill.put("payee", paymentInfo.getPayee());
        // author 票据编制人 String 100 是
        bill.put("author", user.getName());
        // totalAmt 开票总金额 Number 14,2 是
        paymentInfo.setTotalAmt(paymentInfo.getTotalAmt() == null ? ZERO : paymentInfo.getTotalAmt());
        bill.put("totalAmt", String.format("%.2f", paymentInfo.getTotalAmt()));

        // ------通知------
        // tel 患者手机号码 String 11 否
        bill.put("tel", patientInfo.getTel());
        // payerType 交款人类型 String 1 是 交款人类型：1 个人 2 单位
        bill.put("payerType", "2");
        // cardType 卡类型 String 10 是 直接填写业务系统内部编码值，由医疗平台配置对照
        // 例如：附录3：卡类型与证件类型列表
        bill.put("cardType", "1101");
        // cardNo 卡号 String 50 是 根据卡类型填写
        bill.put("cardNo", patientInfo.getCardNo());

        // ------就诊信息------
        // medicalInstitution 医疗机构类型 String 30 是 按照《医疗机构管理条例实施细则》和《卫生部关于修订<医疗机构管理条例实施细则>第三条有关内容的通知》确定的医疗卫生机构类别
        String medicalInstitution;
        switch (optionJson.getString("hospital_lv")) {
            case "01":
                medicalInstitution = "三级特等";
                break;
            case "02":
                medicalInstitution = "三级甲等";
                break;
            case "03":
                medicalInstitution = "三级乙等";
                break;
            case "04":
                medicalInstitution = "三级丙等";
                break;
            case "05":
                medicalInstitution = "二级甲等";
                break;
            case "06":
                medicalInstitution = "二级乙等";
                break;
            case "07":
                medicalInstitution = "二级丙等";
                break;
            case "08":
                medicalInstitution = "一级甲等";
                break;
            case "09":
                medicalInstitution = "一级乙等";
                break;
            case "10":
                medicalInstitution = "一级丙等";
                break;
            default:
                medicalInstitution = "无等级";
                break;
        }
        bill.put("medicalInstitution", medicalInstitution);
        // medCareInstitution 医保机构编码 String 60 否 医保机构的唯一编码
        bill.put("medCareInstitution", optionJson.getString(CommonConstants.Option.HOSPITAL_CODE));

        // 410 长期照护保险 //310 职工基本医疗保险
        // 320 公务员医疗补助 //330 大额医疗费用补助
        // 340 离休人员医疗保障 //350 一至六级残废军人医疗补助
        // 360 老红军医疗保障 //370 企业补充医疗保险
        // 380 新型农村合作医疗 //390 城乡居民基本医疗保险
        // 391 城镇居民基本医疗保险 //392 城乡居民大病医疗保险 //399 其他特殊人员医疗保障

        // 01 职工基本医疗保险 06 全公费
        // 02 居民基本医疗保险 07 全自费
        // 03 新型农村合作医疗 90 城乡居民医疗保险
        // 04 贫困救助 99 其他
        // 05 商业医疗保险
        String medCareTypeCode;
        String medicalCareType;
        // 医保 挂号退号表
        ClinicReg clinicReg = regService.getByBusNo(patientInfo.getEncounterBusNo());
        String xztype;
        if (clinicReg == null) {
            xztype = "07";
        } else {
            xztype = clinicReg.getInsutype();
            // medicalInsuranceID 患者医保编号 String 30 否 参保人在医保系统中的唯一标识(医保号)
            bill.put("medicalInsuranceID", clinicReg.getPsnNo() == null ? "" : clinicReg.getPsnNo());
        }

        switch (xztype) {
            case "310":
            case "320":
            case "330":
            case "370":
                medicalCareType = "职工基本医疗保险";
                medCareTypeCode = "01";
                break;
            case "340":
            case "360":
                medicalCareType = "全公费";
                medCareTypeCode = "06";
                break;
            case "390":
            case "392":
            case "391":
            case "380":
                medicalCareType = "居民基本医疗保险";
                medCareTypeCode = "02";
                break;
            case "350":
            case "410":
            case "399":
                medicalCareType = "其他";
                medCareTypeCode = "99";
                break;
            default:
                medicalCareType = "全自费";
                medCareTypeCode = "07";
                break;
        }
        // medCareTypeCode 医保类型编码 String 30 否
        bill.put("medCareTypeCode", medCareTypeCode);
        // medicalCareType 医保类型名称 String 60 是 由城镇职工基本医疗保险、城镇居民基本医疗保险、新型农村合作医疗、其它医疗保险等构成
        bill.put("medicalCareType", medicalCareType);
        // consultationDate 就诊日期 String 10 是 患者就医时间 格式:yyyyMMdd
        bill.put("consultationDate", sdfday.format(inputFormat.parse(patientInfo.getConsultationDate())));
        // sex 性别 String 4 是
        bill.put("sex", patientInfo.getGenderEnum_enumText());
        // age 年龄 String 10 是
        bill.put("age", AgeCalculatorUtil.getAge(patientInfo.getBirthDate()));

        // ------费用------
        // 个人账户支付
        clinicSettle.setAcctPay(clinicSettle.getAcctPay() == null ? ZERO : clinicSettle.getAcctPay());
        // 医保统筹基金支付
        clinicSettle.setHifpPay(clinicSettle.getHifpPay() == null ? ZERO : clinicSettle.getHifpPay());
        // 自费金额
        clinicSettle
            .setFulamtOwnpayAmt(clinicSettle.getFulamtOwnpayAmt() == null ? ZERO : clinicSettle.getFulamtOwnpayAmt());
        // 个人自负
        clinicSettle.setPsnPartAmt(clinicSettle.getPsnPartAmt() == null ? ZERO : clinicSettle.getPsnPartAmt());
        // 个人自付
        clinicSettle
            .setOverlmtSelfpay(clinicSettle.getOverlmtSelfpay() == null ? ZERO : clinicSettle.getOverlmtSelfpay());
        // 个人现金支付
        clinicSettle.setPsnCashPay(clinicSettle.getPsnCashPay() == null ? ZERO : clinicSettle.getPsnCashPay());
        // 医保报销总金额
        clinicSettle.setFundPaySumamt(clinicSettle.getFundPaySumamt() == null ? ZERO : clinicSettle.getFundPaySumamt());

        // accountPay 个人账户支付 Number 14,2 是
        bill.put("accountPay", df.format(clinicSettle.getAcctPay()));
        // fundPay 医保统筹基金支付 Number 14,2 是
        bill.put("fundPay", df.format(clinicSettle.getHifpPay()));
        // ownPay 自费金额 Number 14,2 是
        bill.put("ownPay", df.format(clinicSettle.getFulamtOwnpayAmt()));
        // selfConceitedAmt 个人自负 Number 14,2 是
        bill.put("selfConceitedAmt", df.format(clinicSettle.getPsnPartAmt()));
        // selfPayAmt 个人自付 Number 14,2 是
        bill.put("selfPayAmt", df.format(clinicSettle.getOverlmtSelfpay()));
        // selfCashPay 个人现金支付 Number 14,2 是
        bill.put("selfCashPay", df.format(clinicSettle.getPsnCashPay()));
        // reimbursementAmt 医保报销总金额 Number 14,2 是
        bill.put("reimbursementAmt", df.format(clinicSettle.getFundPaySumamt()));

        // payChannelDetail 交费渠道列表 JSONArray 不限 是
        JSONArray payChannelDetails = new JSONArray();
        JSONObject payChannelDetail;
        // 个人现金支付金额(微信)
        paymentInfo.setWxPayAmount(paymentInfo.getWxPayAmount() == null ? ZERO : paymentInfo.getWxPayAmount());
        // 个人现金支付金额(支付宝)
        paymentInfo.setAliPayAmount(paymentInfo.getAliPayAmount() == null ? ZERO : paymentInfo.getAliPayAmount());
        // 个人现金支付金额(银联)
        paymentInfo.setDebitPayAmount(paymentInfo.getAliPayAmount() == null ? ZERO : paymentInfo.getAliPayAmount());
        // 个人现金支付金额
        paymentInfo
            .setRmbCashPayAmount(paymentInfo.getRmbCashPayAmount() == null ? ZERO : paymentInfo.getRmbCashPayAmount());
        // 个人医保账户支付
        paymentInfo.setZhPayAmount(paymentInfo.getZhPayAmount() == null ? ZERO : paymentInfo.getZhPayAmount());
        // 基金支付总额
        paymentInfo
            .setYbFundPayAmount(paymentInfo.getYbFundPayAmount() == null ? ZERO : paymentInfo.getYbFundPayAmount());
        // 其他（如医院负担金额）
        paymentInfo.setOtherPayAmount(paymentInfo.getOtherPayAmount() == null ? ZERO : paymentInfo.getOtherPayAmount());
        // 账户共济支付金额
        paymentInfo
            .setAelfYbZhGjValue(paymentInfo.getAelfYbZhGjValue() == null ? ZERO : paymentInfo.getAelfYbZhGjValue());
        if (medCareTypeCode.equals("07")) {// 全自费
            if (paymentInfo.getWxPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "05");// 05 微信
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getWxPayAmount()));
                payChannelDetails.add(payChannelDetail);
            }
            if (paymentInfo.getAliPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "04");// 04 支付宝
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getAliPayAmount()));
                payChannelDetails.add(payChannelDetail);
            }
            if (paymentInfo.getDebitPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "07");// 07 卡支付
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getDebitPayAmount()));
                payChannelDetails.add(payChannelDetail);
            }
            if (paymentInfo.getRmbCashPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "02");// 02 现金
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getRmbCashPayAmount()));
                payChannelDetails.add(payChannelDetail);
            } else {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "02");// 02 现金
                payChannelDetail.put("payChannelValue",
                    df.format(paymentInfo.getTotalAmt().subtract(paymentInfo.getWxPayAmount())
                        .subtract(paymentInfo.getAliPayAmount()).subtract(paymentInfo.getDebitPayAmount())));
                payChannelDetails.add(payChannelDetail);
            }
        } else {
            // 医保
            payChannelDetail = new JSONObject();
            payChannelDetail.put("payChannelCode", "11");
            payChannelDetail.put("payChannelValue", df.format(paymentInfo.getYbFundPayAmount().add(paymentInfo
                .getOtherPayAmount().add(paymentInfo.getZhPayAmount()).add(paymentInfo.getAelfYbZhGjValue()))));
            payChannelDetails.add(payChannelDetail);

            // 现金
            if (paymentInfo.getWxPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "05");// 05 微信
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getWxPayAmount()));
                payChannelDetails.add(payChannelDetail);
            }
            if (paymentInfo.getAliPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "04");// 04 支付宝
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getAliPayAmount()));
                payChannelDetails.add(payChannelDetail);
            }
            if (paymentInfo.getDebitPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "07");// 07 卡支付
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getDebitPayAmount()));
                payChannelDetails.add(payChannelDetail);
            }
            if (paymentInfo.getRmbCashPayAmount().compareTo(ZERO) > 0) {
                payChannelDetail = new JSONObject();
                payChannelDetail.put("payChannelCode", "02");// 02 现金
                payChannelDetail.put("payChannelValue", df.format(paymentInfo.getRmbCashPayAmount()));
                payChannelDetails.add(payChannelDetail);
            } else {
                BigDecimal cashpay = clinicSettle.getPsnCashPay().subtract(paymentInfo.getWxPayAmount())
                    .subtract(paymentInfo.getAliPayAmount()).subtract(paymentInfo.getDebitPayAmount());
                if (cashpay.compareTo(ZERO) > 0) {
                    payChannelDetail = new JSONObject();
                    payChannelDetail.put("payChannelCode", "02");// 02 现金
                    payChannelDetail.put("payChannelValue", df.format(cashpay));
                    payChannelDetails.add(payChannelDetail);
                }
            }
        }
        bill.put("payChannelDetail", payChannelDetails);

        // ------其它------
        // isArrears 是否可流通 String 1 是 0-否、1-是（如欠费情况根据医院业务要求该票据是否可流通）
        bill.put("isArrears", "0");

        return bill;
    }

    /**
     * 医疗电子票据红冲接口
     *
     * @param paymentId 原付款id
     * @param reason 取消理由
     * @return 返回值
     */
    @RequestMapping("/invoiceWriteoff")
    @ResponseBody
    public R<?> invoiceWriteoff(Long paymentId, String reason) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        try {
            // 关键校验：检查必填参数invoiceId是否存在，缺失则直接返回错误
            if (paymentId == null || paymentId == 0) {
                return R.fail(PromptMsgConstant.invoice.M00004);
            }

            Invoice invoice =
                invoiceService.getOne(new LambdaQueryWrapper<Invoice>().eq(Invoice::getReconciliationId, paymentId));

            if (invoice.getStatusEnum() == null || invoice.getStatusEnum().equals(InvoiceStatus.DRAFT)) {
                return R.fail(PromptMsgConstant.invoice.M00007);
            }
            if (invoice.getStatusEnum().equals(InvoiceStatus.CANCELLED)) {
                return R.fail(PromptMsgConstant.invoice.M00005);
            }
            if (!invoice.getStatusEnum().equals(InvoiceStatus.ISSUED)) {
                return R.fail(PromptMsgConstant.invoice.M00007);
            }

            // 请求业务参数
            // data---------------------------------------------------------------------------------------------------------------------
            JSONObject bill = new JSONObject();
            // billBatchCode 电子票据代码 String 50 是
            bill.put("billBatchCode", invoice.getBillBatchCode());
            // billNo 电子票据号码 String 20 是
            bill.put("billNo", invoice.getBillNo());
            // reason 冲红原因 String 200 是
            bill.put("reason", reason);
            // operator 经办人 String 60 是
            bill.put("operator", user.getName());
            // busDateTime 业务发生时间 String 17 是 yyyyMMddHHmmssSSS
            bill.put("busDateTime", sdf.format(new Date()));
            // placeCode 开票点编码 String 50 是
            bill.put("placeCode", user.getKpdCode());

            JSONObject redata = new JSONObject();
            String redata64;
            String srcdata;
            String srcmsg;

            JSONObject resobj = PreInvoicePost(bill, "api/medical/writeOffEBill");
            if (resobj.getBooleanValue("success")) {
                JSONObject rejson = resobj.getJSONObject("result");
                if (rejson.getString("result").equals("S0000")) {
                    // 保存发票信息
                    redata64 = rejson.getString("message").toString();
                    srcdata = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    redata = JSONObject.parseObject(srcdata);

                    // 修改发票管理表数据
                    // 状态
                    invoice.setStatusEnum(InvoiceStatus.CANCELLED);

                    boolean flgUpdate = invoiceService.updateById(invoice);
                    if (!flgUpdate) {
                        return R.fail(PromptMsgConstant.Common.M00011);
                    }

                    // 向发票管理表中插入数据
                    Invoice invoiceAdd = new Invoice();
                    // 发票号
                    invoiceAdd.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.INVOICE_NUM.getPrefix(), 10));
                    // 患者ID
                    invoiceAdd.setPatientId(invoice.getPatientId());
                    // 状态
                    invoiceAdd.setStatusEnum(InvoiceStatus.CANCELLED);
                    // 类别
                    invoiceAdd.setTypeCode(invoice.getTypeCode());
                    // 取消原因
                    invoiceAdd.setCancelledReason(reason);
                    // 收费项
                    invoiceAdd.setChargeItemIds(invoice.getChargeItemIds());
                    // 发票净额总记
                    invoiceAdd.setTotalNet(invoice.getTotalNet());
                    // 发票总计金额
                    invoiceAdd.setTotalGross(invoice.getTotalGross());
                    // 付款详情
                    invoiceAdd.setPaymentTerms(invoice.getPaymentTerms());
                    // 开票员
                    invoiceAdd.setInvoicingStaffId(invoice.getInvoicingStaffId());
                    // 支付id
                    invoiceAdd.setReconciliationId(invoice.getReconciliationId());
                    // 电子票据代码 电子红票票据代码
                    invoiceAdd.setBillBatchCode(redata.getString("eScarletBillBatchCode"));
                    // 电子票据号码 电子红票票据号码
                    invoiceAdd.setBillNo(redata.getString("eScarletBillNo"));
                    // 电子校验码 电子红票校验码
                    invoiceAdd.setRandom(redata.getString("eScarletRandom"));
                    // 电子票据生成时间 电子红票生成时间
                    invoiceAdd.setBillCreateTime(redata.getString("createTime"));
                    // 电子票据二维码图片数据
                    invoiceAdd.setBillQrCode(redata.getString("billQRCode"));
                    // 电子票据H5页面URL
                    invoiceAdd.setPictureUrl(redata.getString("pictureUrl"));
                    // 电子票据外网H5页面URL
                    invoiceAdd.setPictureNetUrl(redata.getString("pictureNetUrl"));
                    // 票据营业日期
                    invoiceAdd.setBillBusDate(
                        redata.getString("billBusDate") == "" ? null : sdf.parse(redata.getString("billBusDate")));

                    Long invoiceIdAdd = invoiceService.addInvoice(invoiceAdd);
                    if (invoiceIdAdd == null) {
                        return R.fail(PromptMsgConstant.Common.M00011);
                    }

                    return R.ok(invoiceAdd,
                        MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"电子发票冲红"}));
                } else {
                    redata.put("result", rejson.getString("result").toString());
                    redata64 = rejson.getString("message").toString();
                    srcmsg = new String(Base64.getDecoder().decode(redata64), StandardCharsets.UTF_8);
                    return R.fail(srcmsg);
                }
            } else {
                return R.fail(resobj.getString("msg"));
            }
        } catch (Exception ex) {
            try {
                return R.fail(ex.getCause().getMessage());
            } catch (Exception ex1) {
                return R.fail(ex.getMessage());
            }
        }
    }

    /**
     * 发送请求
     *
     * @param bill 请求参数
     * @param endpoint 请求后缀url
     * @return 返回值
     */
    public static JSONObject PreInvoicePost(JSONObject bill, String endpoint) {

        JSONObject result = new JSONObject();
        // 获取当前租户的option信息
        JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();

        String baseUrl = optionJson.getString(CommonConstants.Option.URL);
        // 拼接成完整 URL（作为路径）
        String cleanUrl = baseUrl + "/" + endpoint; // 确保用 "/" 分隔
        String url = cleanUrl.trim().replaceAll("^\"|\"$", "") // 去除首尾引号
            .replaceAll("\\s+", "")// 去除首尾引号
            .replaceAll("\"", ""); // 去除中间引号

        String appID = optionJson.getString(CommonConstants.Option.APP_ID);
        String appKey = optionJson.getString(CommonConstants.Option.KEY);
        String data = bill.toJSONString();
        String version = "1.0";
        // 请求随机标识 noise
        String noise = UUID.randomUUID().toString();

        data = Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder str = new StringBuilder();
        str.append("appid=").append(appID);
        str.append("&data=").append(data);
        str.append("&noise=").append(noise);
        str.append("&key=").append(appKey);
        str.append("&version=").append(version);
        String sign = DigestUtils.md5Hex(str.toString().getBytes(Charset.forName("UTF-8"))).toUpperCase();

        Map<String, String> map = new HashMap<>();
        map.put("appid", appID);
        map.put("data", data);
        map.put("noise", noise);
        map.put("sign", sign);
        map.put("version", version);

        try {
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpClient client = HttpClients.createDefault();
            String respContent = null;
            // 请求参数转JOSN字符串
            StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(map), "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);

            if (resp.getStatusLine().getStatusCode() == 200) {
                String rev = EntityUtils.toString(resp.getEntity());
                // System.out.println("返回串--》"+rev);
                Map resultData = new ObjectMapper().readValue(rev, Map.class);
                String rdata = resultData.get("data").toString();
                String rnoise = resultData.get("noise").toString();
                // 1、拼接返回验签参数
                StringBuilder str1 = new StringBuilder();
                str1.append("appid=").append(appID);
                str1.append("&data=").append(rdata);
                str1.append("&noise=").append(rnoise);
                str1.append("&key=").append(appKey);
                str1.append("&version=").append(version);
                // 3.MD5加密 生成sign
                String rmd5 = DigestUtils.md5Hex(str1.toString().getBytes(Charset.forName("UTF-8"))).toUpperCase();
                String rsign = resultData.get("sign").toString();
                System.out.println("验签-》" + (StringUtils.equals(rsign, rmd5)));
                String busData =
                    new String(Base64.getDecoder().decode(resultData.get("data").toString()), StandardCharsets.UTF_8);
                System.out.println("返回业务数据--》" + busData);
                Map busDataMap = new ObjectMapper().readValue(busData, Map.class);
                System.out
                    .println("业务信息解密--》" + new String(Base64.getDecoder().decode(busDataMap.get("message").toString()),
                        StandardCharsets.UTF_8));

                JSONObject resobj = JSONObject.parseObject(busData);
                result.put("success", true);
                result.put("result", resobj);
            } else {
                result.put("msg", "web响应失败!");
                result.put("success", false);
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("success", false);
        }
        return result;

    }

    /**
     * 通过paymentId获取医保结算实体
     *
     * @param paymentId 付款id
     * @return 查询结果
     */
    private InvoiceBaseInfoDto getClinicSettleByPaymentId(Long paymentId) {

        PaymentReconciliation paymentReconciliation = paymentReconciliationService.getById(paymentId);
        List<PaymentRecDetail> details = paymentRecDetailService
            .list(new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId, paymentId));

        InvoiceBaseInfoDto invoiceBaseInfoDto = initInvoiceBaseInfoDto();

        for (PaymentRecDetail detail : details) {
            // 现金
            if (YbPayment.SELF_CASH_PAY.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setPsnCashPay(detail.getAmount());
            }
            // 公务员补助
            if (YbPayment.YB_BC_GWY_BZ_VALUE.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setCvlservPay(detail.getAmount());
            }
            // 个人账户
            if (YbPayment.SELF_YB_ZH_PAY.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setAcctPay(detail.getAmount());
            }
            // 基本统筹
            if (YbPayment.YB_TC_FUND_AMOUNT.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setHifpPay(detail.getAmount());
            }
            // 全自费金额
            if (YbPayment.FULAMT_OWNPAY_AMT.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setPsnPartAmt(detail.getAmount());
            }
            // 个人负担总金额
            if (YbPayment.SELF_PAY.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setFulamtOwnpayAmt(detail.getAmount());
            }
            // 基金支付总额
            if (YbPayment.YB_FUND_PAY.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setFundPaySumamt(detail.getAmount());
            }
            // 个人医保账户支付
            if (YbPayment.SELF_YB_ZH_PAY.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setAcctPay(detail.getAmount());
            }
            // 基本医保统筹基金支出
            if (YbPayment.YB_TC_FUND_AMOUNT.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setHifpPay(detail.getAmount());
            }
            // 超限价自费费用
            if (YbPayment.OVERLMT_SELFPAY.getValue().equals(detail.getPayEnum())) {
                invoiceBaseInfoDto.setOverlmtSelfpay(detail.getAmount());
            }
        }
        return invoiceBaseInfoDto;
    }

    /**
     * 初始化发票参数
     *
     * @return 发票参数
     */
    private InvoiceBaseInfoDto initInvoiceBaseInfoDto() {
        InvoiceBaseInfoDto clinicSettle = new InvoiceBaseInfoDto();
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

    @Override
    public Invoice getInvoiceById(Long invoiceId) {
        return invoiceService.getById(invoiceId);
    }
}
