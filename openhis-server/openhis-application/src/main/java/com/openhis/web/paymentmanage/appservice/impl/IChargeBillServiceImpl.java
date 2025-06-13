package com.openhis.web.paymentmanage.appservice.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.exception.ServiceException;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.*;
import com.openhis.clinical.domain.Condition;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.clinical.service.IConditionService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.YbCommonConstants;
import com.openhis.common.enums.DelFlag;
import com.openhis.common.enums.InvoiceStatus;
import com.openhis.common.enums.PaymentType;
import com.openhis.common.enums.Whether;
import com.openhis.common.enums.ybenums.YbMedChrgItmType;
import com.openhis.common.enums.ybenums.YbPayment;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.service.IContractService;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.domain.MedicationDefinition;
import com.openhis.medication.service.IMedicationDefinitionService;
import com.openhis.web.paymentmanage.appservice.IChargeBillService;
import com.openhis.web.paymentmanage.dto.ReportVo;
import com.openhis.web.paymentmanage.dto.ReturnBillVO;
import com.openhis.web.paymentmanage.mapper.ChargeBillMapper;
import com.openhis.web.paymentmanage.dto.ChargeItemDetailVO;
import com.openhis.workflow.domain.ActivityDefinition;
import com.openhis.workflow.service.IActivityDefinitionService;
import com.openhis.yb.domain.ClinicSettle;
import com.openhis.yb.domain.ClinicUnSettle;
import com.openhis.yb.domain.InfoPerson;
import com.openhis.yb.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IChargeBillServiceImpl implements IChargeBillService {

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
    private IPatientService iPatientService;
    @Autowired
    private IChargeItemDefinitionService iChargeItemDefinitionService;
    @Autowired
    private IPerinfoService iPerinfoService;
    @Autowired
    private IEncounterDiagnosisService iEncounterDiagnosisService;
    @Autowired
    private IConditionDefinitionService iConditionDefinitionService;
    @Autowired
    private IConditionService iConditionService;
    @Autowired
    private ChargeBillMapper chargeBillMapper;
    @Autowired
    private IMedicationDefinitionService iMedicationDefinitionService;
    @Autowired
    private IDeviceDefinitionService iDeviceDefinitionService;
    @Autowired
    private IActivityDefinitionService iActivityDefinitionService;
    @Autowired
    private IPractitionerService iPractitionerService;
    @Autowired
    private IHealthcareServiceService iHealthcareServiceService;
    @Autowired
    private IClinicSettleService iClinicSettleService;
    @Autowired
    private IClinicUnRegService iClinicUnRegService;
    @Autowired
    private IRegService iRegService;
    @Autowired
    private IClinicUnSettleService iClinicUnSettleService;


    @Override
    public Map getDetail(Long paymentId) {

        Map<String, Object> map = new HashMap<>();
        //查询
        PaymentReconciliation paymentReconciliation = paymentReconciliationService.getById(paymentId);
        if(paymentReconciliation==null){
            throw new ServiceException("未查询到付款信息");
        }
        map.put("paymentId",paymentReconciliation.getPaymentNo());//结算id
        map.put("paymentAmount",paymentReconciliation.getTenderedAmount());//应收金额
        Practitioner practitioner = iPractitionerService.getById(paymentReconciliation.getEntererId());
        map.put("paymentAmount",practitioner==null?"":practitioner.getName());//收费员
        map.put("chargeTime",paymentReconciliation.getBillDate());//收费时间
        Patient patient = iPatientService.getById(paymentReconciliation.getPatientId());
        if(patient==null){
            throw new ServiceException("未查询到患者信息");
        }
        map.put("patientName",patient.getName());//患者姓名

        map.put("sex",patient.getGenderEnum());//性别
        map.put("idCardNo",patient.getIdCard());//身份证号
        map.put("birthDay",patient.getBirthDate());//出生日期

        List<PaymentRecDetail> paymentRecDetails = paymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId, paymentId));
        paymentRecDetails.stream()
                .collect(Collectors.groupingBy(
                        PaymentRecDetail::getPayEnum, // 替换为实际的分类属性getter方法
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                PaymentRecDetail::getAmount, // 替换为实际的amount getter方法
                                BigDecimal::add
                        )
                ));
        if(paymentRecDetails.isEmpty()){
            throw new ServiceException("未查询到付款信息");
        }
        map.put("detail",paymentRecDetails);//支付详细

        Invoice invoice = iInvoiceService.getOne(new LambdaQueryWrapper<Invoice>().eq(Invoice::getReconciliationId, paymentId).eq(Invoice::getStatusEnum, InvoiceStatus.ISSUED.getValue()).orderByDesc(Invoice::getCreateTime).last(YbCommonConstants.sqlConst.LIMIT1));
        if(invoice==null){
            throw new ServiceException("未查询到发票信息");
        }
        map.put("invoiceNo",invoice.getBillNo());//支付详细
        map.put("pictureUrl",invoice.getPictureUrl());//图片
        List<Long> chargeItemIdList = Arrays.stream(paymentReconciliation.getChargeItemIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<ChargeItem> chargeItemList = chargeItemService.list(new LambdaQueryWrapper<ChargeItem>().in(ChargeItem::getId, chargeItemIdList));
        List<ChargeItemDetailVO> chargeItemDetailList = new ArrayList<>();
        ChargeItemDetailVO chargeItemDetailVO;
        for (ChargeItem chargeItem : chargeItemList) {
            chargeItemDetailVO = new ChargeItemDetailVO();
            BeanUtils.copyProperties(chargeItem,chargeItemDetailVO);
            if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(chargeItem.getProductTable())) {
                MedicationDefinition medication = iMedicationDefinitionService.getById(chargeItem.getProductId());
                chargeItemDetailVO.setDirClass(medication.getYbNo()).setChargeItemName(medication.getName())
                        .setTotalPrice(chargeItem.getTotalPrice()).setQuantityUnit(chargeItem.getQuantityUnit())
                        .setQuantityValue(chargeItem.getQuantityValue());
            } else if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(chargeItem.getProductTable())) {
                DeviceDefinition device = iDeviceDefinitionService.getById(chargeItem.getProductId());
                chargeItemDetailVO.setDirClass(device.getYbNo()).setChargeItemName(device.getName())
                        .setTotalPrice(chargeItem.getTotalPrice()).setQuantityUnit(chargeItem.getQuantityUnit())
                        .setQuantityValue(chargeItem.getQuantityValue());
            } else if(CommonConstants.TableName.WOR_ACTIVITY_DEFINITION.equals(chargeItem.getProductTable())){
                ActivityDefinition activity = iActivityDefinitionService.getById(chargeItem.getProductId());
                chargeItemDetailVO.setDirClass(activity.getYbNo()).setChargeItemName(activity.getName())
                        .setTotalPrice(chargeItem.getTotalPrice()).setQuantityUnit(chargeItem.getQuantityUnit())
                        .setQuantityValue(chargeItem.getQuantityValue());
            }else{
                HealthcareService healthcareService = iHealthcareServiceService.getById(chargeItem.getServiceId());
                chargeItemDetailVO.setDirClass(healthcareService.getYbNo()).setChargeItemName(healthcareService.getName())
                        .setTotalPrice(chargeItem.getTotalPrice()).setQuantityUnit(chargeItem.getQuantityUnit())
                        .setQuantityValue(chargeItem.getQuantityValue());
            }
            chargeItemDetailList.add(chargeItemDetailVO);
        }
        map.put("chargeItem",chargeItemDetailList);//收费项

        if(chargeItemList.isEmpty()){
            throw new ServiceException("未查询到收费项");
        }
        Encounter encounter = iEncounterService.getById(paymentReconciliation.getEncounterId());
        if(encounter==null){
            throw new ServiceException("未查询到就诊信息");
        }
        map.put("classEnum",encounter.getYbClassEnum());//门诊/住院
        map.put("regNo",encounter.getBusNo());//门诊号

        Account account = iAccountService.getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, encounter.getId()).eq(Account::getEncounterFlag,Whether.YES.getValue()));
        if(account==null){
            throw new ServiceException("未查询到就诊信息");
        }

        InfoPerson perinfo = iPerinfoService.getOne(new LambdaQueryWrapper<InfoPerson>()
                .eq(InfoPerson::getCertno, patient.getIdCard()).eq(InfoPerson::getTenantId, patient.getTenantId())
                .orderByDesc(InfoPerson::getCreateTime).last(YbCommonConstants.sqlConst.LIMIT1));
        if(perinfo!=null){
            map.put("personType",perinfo.getInsutype());//人员类别：职工医保，居民医保等；如果为空显示自费或其他
            map.put("insuplcAdmdvs",perinfo.getInsuplcAdmdvs());//患者参保地区划
        }

        Contract contract = contractService.getOne(new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, account.getContractNo()));
        if(contract==null){
            throw new ServiceException("未查询到合同信息");
        }
        //查询主诊断信息
        EncounterDiagnosis encounterDiagnosis = iEncounterDiagnosisService.getOne(new LambdaQueryWrapper<EncounterDiagnosis>().eq(EncounterDiagnosis::getEncounterId, encounter.getId()).eq(EncounterDiagnosis::getMaindiseFlag, Whether.YES.getValue()).eq(EncounterDiagnosis::getDeleteFlag, DelFlag.NO.getCode()).orderByDesc(EncounterDiagnosis::getDiagSrtNo).last(YbCommonConstants.sqlConst.LIMIT1));

        if(encounterDiagnosis!=null){
            Condition condition = iConditionService.getById(encounterDiagnosis.getConditionId());
            if(condition!=null){
                ConditionDefinition conditionDefinition = iConditionDefinitionService.getOne(new LambdaQueryWrapper<ConditionDefinition>().eq(ConditionDefinition::getId, condition.getDefinitionId()));
                if(conditionDefinition!=null){
                    map.put("conditionDefinition",conditionDefinition.getName());//诊断名
                }
            }
        }

        BigDecimal sum01 = BigDecimal.ZERO;
        BigDecimal sum02 = BigDecimal.ZERO;
        BigDecimal sum03 = BigDecimal.ZERO;
        BigDecimal sum04 = BigDecimal.ZERO;
        BigDecimal sum05 = BigDecimal.ZERO;
        BigDecimal sum06 = BigDecimal.ZERO;
        BigDecimal sum07 = BigDecimal.ZERO;
        BigDecimal sum08 = BigDecimal.ZERO;
        BigDecimal sum09 = BigDecimal.ZERO;
        BigDecimal sum10 = BigDecimal.ZERO;
        BigDecimal sum11 = BigDecimal.ZERO;
        BigDecimal sum12 = BigDecimal.ZERO;
        BigDecimal sum13 = BigDecimal.ZERO;
        BigDecimal sum14 = BigDecimal.ZERO;

        for (ChargeItem chargeItem : chargeItemList) {

            Long definitionId = chargeItem.getDefinitionId();

            ChargeItemDefinition chargeItemDefinition = iChargeItemDefinitionService.getById(definitionId);

            YbMedChrgItmType medChrgItmType = YbMedChrgItmType.getByCode(Integer.parseInt(chargeItemDefinition.getYbType()));

            switch (medChrgItmType){
                case BED_FEE:
                    sum01 = sum01.add(chargeItem.getTotalPrice());
                    break;
                case DIAGNOSTIC_FEE:
                    sum02 = sum02.add(chargeItem.getTotalPrice());
                    break;
                case CHECK_FEE:
                    sum03 = sum03.add(chargeItem.getTotalPrice());
                    break;
                case DIAGNOSTIC_TEST_FEE:
                    sum04 = sum04.add(chargeItem.getTotalPrice());
                    break;
                case MEDICAL_EXPENSE_FEE:
                    sum05 = sum05.add(chargeItem.getTotalPrice());
                    break;
                case OPERATION_FEE:
                    sum06 = sum06.add(chargeItem.getTotalPrice());
                    break;
                case NURSING_FEE:
                    sum07 = sum07.add(chargeItem.getTotalPrice());
                    break;
                case SANITARY_MATERIALS_FEE:
                    sum08 = sum08.add(chargeItem.getTotalPrice());
                    break;
                case WEST_MEDICINE:
                    sum09 = sum09.add(chargeItem.getTotalPrice());
                    break;
                case CHINESE_MEDICINE_SLICES_FEE:
                    sum10 = sum10.add(chargeItem.getTotalPrice());
                    break;
                case CHINESE_MEDICINE_FEE:
                    sum11 = sum11.add(chargeItem.getTotalPrice());
                    break;
                case GENERAL_CONSULTATION_FEE:
                    sum12 = sum12.add(chargeItem.getTotalPrice());
                    break;
                case REGISTRATION_FEE:
                    sum13 = sum13.add(chargeItem.getTotalPrice());
                    break;
                default:
                    sum14 = sum14.add(chargeItem.getTotalPrice());
                    break;
            }
        }
        map.put("BED_FEE",sum01);//床位费
        map.put("DIAGNOSTIC_FEE",sum02);//诊察费
        map.put("CHECK_FEE",sum03);//检查费
        map.put("DIAGNOSTIC_TEST_FEE",sum04);//化验费
        map.put("MEDICAL_EXPENSE_FEE",sum05);//治疗费
        map.put("OPERATION_FEE",sum06);//手术费
        map.put("NURSING_FEE",sum07);//护理费
        map.put("SANITARY_MATERIALS_FEE",sum08);//卫生材料费
        map.put("WEST_MEDICINE",sum09);//西药费
        map.put("CHINESE_MEDICINE_SLICES_FEE",sum10);//中药饮片费
        map.put("CHINESE_MEDICINE_FEE",sum11);//中成药费
        map.put("GENERAL_CONSULTATION_FEE",sum12);//一般诊疗费
        map.put("REGISTRATION_FEE",sum13);//挂号费
        map.put("OTHER_FEE",sum14);//其他费用

        //String ybSwitch = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH);
        JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();
        String fixmedinsName = optionJson.getString(CommonConstants.Option.FIXMEDINS_NAME);
        String fixmedinsCode = optionJson.getString(CommonConstants.Option.FIXMEDINS_CODE);

        map.put("fixmedinsName",fixmedinsName);
        map.put("fixmedinsCode",fixmedinsCode);

        return map;
    }


    /**
     * 农大版本
     * @param reportVo
     * @return
     */
    @Override
    public Map getTotal(String startTime, String endTime, Long entererId) {

        Map<String, Object> map = new HashMap<>();

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date startDate = DateUtils.parseDate(startTime);
        Date endDate = DateUtils.parseDate(endTime);

        //查询所有的支付
        LambdaQueryWrapper<PaymentReconciliation> queryWrapper = new LambdaQueryWrapper<PaymentReconciliation>().between(PaymentReconciliation::getBillDate, startDate, endDate).eq(PaymentReconciliation::getDeleteFlag,DelFlag.NO.getCode());
        if(entererId!=null){
            queryWrapper.eq(PaymentReconciliation::getEntererId,entererId);
        }
        List<PaymentReconciliation> paymentReconciliationList = paymentReconciliationService.list(queryWrapper);
        if(paymentReconciliationList.isEmpty()){
            throw new ServiceException("该时间段内未收费");
        }
        //查询所有的支付详情
        List<Long> paymentIdList = paymentReconciliationList.stream().map(PaymentReconciliation::getId).collect(Collectors.toList());
        List<PaymentRecDetail> paymentDetails = paymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>().in(PaymentRecDetail::getReconciliationId, paymentIdList).eq(PaymentRecDetail::getDeleteFlag, DelFlag.NO.getCode()));
        if(paymentDetails.isEmpty()){
            throw new ServiceException("该时间段内未收费");
        }
        Map<Long, List<PaymentRecDetail>> paymentDetailsKV = paymentDetails.stream().collect(Collectors.groupingBy(PaymentRecDetail::getReconciliationId));

        //查询所有的收费项
        List<String> chargeItemIdStrs = paymentReconciliationList.stream().map(PaymentReconciliation::getChargeItemIds).collect(Collectors.toList());
        List<Long> chargeItemIds = new ArrayList<>();
        for (String chargeItemIdStr : chargeItemIdStrs) {
            List<String> strings = Arrays.asList(chargeItemIdStr.split(","));
            for (String string : strings) {
                Long id = Long.parseLong(string);
                chargeItemIds.add(id);
            }
        }
        List<ChargeItem> chargeItemList = chargeItemService.list(new LambdaQueryWrapper<ChargeItem>().in(ChargeItem::getId, chargeItemIds).eq(ChargeItem::getDeleteFlag,DelFlag.NO.getCode()));
        if(paymentDetails.isEmpty()){
            throw new ServiceException("该时间段内未收费");
        }

        //查询收费定义列表
        List<Long>  chargeItemDefinitionIdList= chargeItemList.stream().map(ChargeItem::getDefinitionId).collect(Collectors.toList());
        List<ChargeItemDefinition> chargeItemDefinitions = iChargeItemDefinitionService.listByIds(chargeItemDefinitionIdList);
        Map<Long, List<ChargeItemDefinition>> chargeItemDefKV = chargeItemDefinitions.stream().collect(Collectors.groupingBy(ChargeItemDefinition::getId));

        //查询医保结算id
        List<String> settleIds = new ArrayList<>();
        for (PaymentReconciliation paymentReconciliation : paymentReconciliationList) {
            if(!StringUtils.isEmpty(paymentReconciliation.getYbSettleIds())){
                List<String> strings = Arrays.asList(paymentReconciliation.getYbSettleIds().split(","));
                settleIds.addAll(strings);
            }
        }
        List<ClinicSettle> clinicSettle = new ArrayList<>();
        if (!settleIds.isEmpty()){
            clinicSettle = iClinicSettleService.getBySettleIds(settleIds);
            //List<String> collect = clinicSettle.stream().map(ClinicSettle::getInsutype).collect(Collectors.toList());
        }

        BigDecimal cashSum = BigDecimal.ZERO;//现金总数 = rmbCashSum + vxCashSum + aliCashSum + uniCashSum
        BigDecimal rmbCashSum = BigDecimal.ZERO;//现金总数
        BigDecimal vxCashSum = BigDecimal.ZERO;//现金总数（）
        BigDecimal aliCashSum = BigDecimal.ZERO;//现金总数（）
        BigDecimal uniCashSum = BigDecimal.ZERO;//现金总数（）
        BigDecimal vxSum = BigDecimal.ZERO;//微信总数
        BigDecimal aliSum = BigDecimal.ZERO;//支付宝总数
        BigDecimal uniSum = BigDecimal.ZERO;//银行卡总数
        BigDecimal tcSum = BigDecimal.ZERO;//基本统筹
        BigDecimal zhSum = BigDecimal.ZERO;//医保账户
        BigDecimal fundSum = BigDecimal.ZERO;//基金支总额
        //农大版本只查询收款员，日期，现金多少钱，挂号费多少钱
        for (PaymentRecDetail paymentDetail : paymentDetails) {
            if(YbPayment.SELF_CASH_PAY.getValue().equals(paymentDetail.getPayEnum())){
                cashSum = cashSum.add(paymentDetail.getAmount());
            }
            if(YbPayment.SELF_CASH_VALUE.getValue().equals(paymentDetail.getPayEnum())){
                rmbCashSum = rmbCashSum.add(paymentDetail.getAmount());
            }
            if(YbPayment.SELF_CASH_VX_VALUE.getValue().equals(paymentDetail.getPayEnum())){
                vxCashSum = vxCashSum.add(paymentDetail.getAmount());
            }
            if(YbPayment.SELF_CASH_ALI_VALUE.getValue().equals(paymentDetail.getPayEnum())){
                aliCashSum = aliCashSum.add(paymentDetail.getAmount());
            }
            if(YbPayment.SELF_CASH_UNION_VALUE.getValue().equals(paymentDetail.getPayEnum())){
                uniCashSum = uniCashSum.add(paymentDetail.getAmount());
            }
            if(YbPayment.YB_TC_FUND_AMOUNT.getValue().equals(paymentDetail.getPayEnum())){
                tcSum = tcSum.add(paymentDetail.getAmount());
            }
            if(YbPayment.YB_FUND_PAY.getValue().equals(paymentDetail.getPayEnum())){
                fundSum = fundSum.add(paymentDetail.getAmount());
            }
            if(YbPayment.SELF_YB_ZH_PAY.getValue().equals(paymentDetail.getPayEnum())){
                zhSum = zhSum.add(paymentDetail.getAmount());
            }
        }

        //处理退费单
        List<ReturnBillVO> returnList = new ArrayList<>();
        List<PaymentReconciliation> returnPaymentList = new ArrayList<>();
        ReturnBillVO returnBillVO;
        for (PaymentReconciliation paymentReconciliation : paymentReconciliationList) {
            if(PaymentType.UN_PAY.getValue().equals(paymentReconciliation.getPaymentEnum())){
                returnPaymentList.add(paymentReconciliation);
            }
        }
        if(!returnPaymentList.isEmpty()) {
            List<Long> returnIds = returnPaymentList.stream().map(PaymentReconciliation::getId).collect(Collectors.toList());

            List<Invoice> invoiceList = iInvoiceService.list(new LambdaUpdateWrapper<Invoice>().in(Invoice::getReconciliationId, returnIds).eq(Invoice::getDeleteFlag, DelFlag.NO.getCode()));
            if (!invoiceList.isEmpty()) {
                Map<Long, List<Invoice>> invoiceKV = invoiceList.stream().collect(Collectors.groupingBy(Invoice::getReconciliationId));
                for (PaymentReconciliation paymentReconciliation : returnPaymentList) {
                    returnBillVO = new ReturnBillVO();
                    returnBillVO.setTotalAmount(paymentReconciliation.getTenderedAmount());
                    List<PaymentRecDetail> paymentRecDetails = paymentDetailsKV.get(paymentReconciliation.getId());
                    for (PaymentRecDetail paymentRecDetail : paymentRecDetails) {
                        if (YbPayment.SELF_CASH_PAY.getValue().equals(paymentRecDetail.getPayEnum())) {
                            returnBillVO.setPaidAmount(paymentRecDetail.getAmount());
                        }
                    }

                    Optional<Invoice> max = invoiceKV.get(paymentReconciliation.getId()).stream().max(Comparator.comparing(Invoice::getCreateTime));

                    if (max.isPresent()) {
                        returnBillVO.setInvoiceNo(max.get().getBillNo());
                    } else {
                        returnBillVO.setInvoiceNo("");
                    }

                    returnBillVO.setPaymentNo(paymentReconciliation.getPaymentNo());
                    returnList.add(returnBillVO);
                }
                map.put("returnList", returnList);//退费单
            }
        }

        map.put("cashSum",cashSum);//现金支付
        map.put("rmbCashSum",rmbCashSum);//现金支付
        map.put("vxCashSum",vxCashSum);//现金支付（）
        map.put("aliCashSum",aliCashSum);//现金支付（）
        map.put("uniCashSum",uniCashSum);//现金支付（）
        map.put("tcSum",tcSum);//基本统筹
        map.put("zhSum",zhSum);//账户支付
        map.put("fundSum",fundSum);//基金支付总额


        BigDecimal sum01 = BigDecimal.ZERO;
        BigDecimal sum02 = BigDecimal.ZERO;
        BigDecimal sum03 = BigDecimal.ZERO;
        BigDecimal sum04 = BigDecimal.ZERO;
        BigDecimal sum05 = BigDecimal.ZERO;
        BigDecimal sum06 = BigDecimal.ZERO;
        BigDecimal sum07 = BigDecimal.ZERO;
        BigDecimal sum08 = BigDecimal.ZERO;
        BigDecimal sum09 = BigDecimal.ZERO;
        BigDecimal sum10 = BigDecimal.ZERO;
        BigDecimal sum11 = BigDecimal.ZERO;
        BigDecimal sum12 = BigDecimal.ZERO;
        BigDecimal sum13 = BigDecimal.ZERO;
        BigDecimal sum14 = BigDecimal.ZERO;

        for (ChargeItem chargeItem : chargeItemList) {

            Long definitionId = chargeItem.getDefinitionId();

            ChargeItemDefinition chargeItemDefinition = chargeItemDefKV.get(definitionId).get(0);

            YbMedChrgItmType medChrgItmType = YbMedChrgItmType.getByCode(Integer.parseInt(chargeItemDefinition.getYbType()));

            switch (medChrgItmType){
                case BED_FEE:
                    sum01 = sum01.add(chargeItem.getTotalPrice());
                    break;
                case DIAGNOSTIC_FEE:
                    sum02 = sum02.add(chargeItem.getTotalPrice());
                    break;
                case CHECK_FEE:
                    sum03 = sum03.add(chargeItem.getTotalPrice());
                    break;
                case DIAGNOSTIC_TEST_FEE:
                    sum04 = sum04.add(chargeItem.getTotalPrice());
                    break;
                case MEDICAL_EXPENSE_FEE:
                    sum05 = sum05.add(chargeItem.getTotalPrice());
                    break;
                case OPERATION_FEE:
                    sum06 = sum06.add(chargeItem.getTotalPrice());
                    break;
                case NURSING_FEE:
                    sum07 = sum07.add(chargeItem.getTotalPrice());
                    break;
                case SANITARY_MATERIALS_FEE:
                    sum08 = sum08.add(chargeItem.getTotalPrice());
                    break;
                case WEST_MEDICINE:
                    sum09 = sum09.add(chargeItem.getTotalPrice());
                    break;
                case CHINESE_MEDICINE_SLICES_FEE:
                    sum10 = sum10.add(chargeItem.getTotalPrice());
                    break;
                case CHINESE_MEDICINE_FEE:
                    sum11 = sum11.add(chargeItem.getTotalPrice());
                    break;
                case GENERAL_CONSULTATION_FEE:
                    sum12 = sum12.add(chargeItem.getTotalPrice());
                    break;
                case REGISTRATION_FEE:
                    sum13 = sum13.add(chargeItem.getTotalPrice());
                    break;
                default:
                    sum14 = sum14.add(chargeItem.getTotalPrice());
                    break;
            }
        }
        map.put("BED_FEE",sum01);//床位费
        map.put("DIAGNOSTIC_FEE",sum02);//诊察费
        map.put("CHECK_FEE",sum03);//检查费
        map.put("DIAGNOSTIC_TEST_FEE",sum04);//化验费
        map.put("MEDICAL_EXPENSE_FEE",sum05);//治疗费
        map.put("OPERATION_FEE",sum06);//手术费
        map.put("NURSING_FEE",sum07);//护理费
        map.put("SANITARY_MATERIALS_FEE",sum08);//卫生材料费
        map.put("WEST_MEDICINE",sum09);//西药费
        map.put("CHINESE_MEDICINE_SLICES_FEE",sum10);//中药饮片费
        map.put("CHINESE_MEDICINE_FEE",sum11);//中成药费
        map.put("GENERAL_CONSULTATION_FEE",sum12);//一般诊疗费
        map.put("REGISTRATION_FEE",sum13);//挂号费
        map.put("OTHER_FEE",sum14);//其他费用

        //String ybSwitch = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH);
        JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();
        String fixmedinsName = optionJson.getString(CommonConstants.Option.FIXMEDINS_NAME);
        String fixmedinsCode = optionJson.getString(CommonConstants.Option.FIXMEDINS_CODE);

        map.put("fixmedinsName",fixmedinsName);
        map.put("fixmedinsCode",fixmedinsCode);

        return map;
    }
}
