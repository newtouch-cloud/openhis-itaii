package com.openhis.web.doctorstation.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.enums.ybenums.YbRxItemTypeCode;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.doctorstation.appservice.IDoctorStationElepPrescriptionService;
import com.openhis.web.doctorstation.mapper.DoctorStationElepPrescriptionMapper;
import com.openhis.workflow.domain.ElepMedicationRequest;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.IElepMedicationRequestService;
import com.openhis.ybcatalog.domain.CatalogDrugInfo;
import com.openhis.ybcatalog.service.ICatalogDrugInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.doctorstation.dto.*;

/**
 * 医生站-电子处方 应用实现类
 */
@Service
public class DoctorStationElepPrescriptionServiceImpl implements IDoctorStationElepPrescriptionService {
    @Autowired
    ICatalogDrugInfoService catalogDrugInfoService;
    @Autowired
    DoctorStationElepPrescriptionMapper elepPrescriptionMapper;
    @Autowired
    IElepMedicationRequestService elepMedicationRequestService;
    @Resource
    IPractitionerService practitionerService;
    @Resource
    private AssignSeqUtil assignSeqUtil;

    /**
     * 电子处方下拉框
     *
     * @return 下拉框信息
     */
    @Override
    public R<?> elepPrescriptionInit() {

        ElepPrescriptionInitDto initDto = new ElepPrescriptionInitDto();

        // 处方类别
        List<ElepPrescriptionInitDto.commonStatusOption> rxTypeCodeListOptions = Stream.of(YbRxItemTypeCode.values())
            .map(prescriptionType -> new ElepPrescriptionInitDto.commonStatusOption(prescriptionType.getValue(),
                prescriptionType.getDescription()))
            .collect(Collectors.toList());

        initDto.setRxTypeCodeListOptions(rxTypeCodeListOptions);
        return R.ok(initDto);
    }

    /**
     * 获取药品信息
     *
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param searchKey 模糊查询关键字
     * @return 药品信息
     */
    @Override
    public R<?> getAllMedicationInfo(String searchKey, Integer pageNo, Integer pageSize) {
        IPage<CatalogDrugInfo> medicationInfo = catalogDrugInfoService.selectCatalogDrugInfo(pageNo, pageSize, searchKey);
        return R.ok(medicationInfo);
    }

    /**
     * 获取处方信息
     *
     * @param patientId 患者id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 药品信息
     */
    @Override
    public R<?> getPrescriptionInfo(Long patientId, Integer pageNo, Integer pageSize) {
        // 处方信息查询
        IPage<ElepPrescriptionInfoDto> prescriptionInfo =
            elepPrescriptionMapper.selectElepPrescriptionInfo(new Page<>(pageNo, pageSize), patientId);
        // 状态转换
        prescriptionInfo.getRecords().forEach(infoDto -> {
            infoDto.setStatusEnum_enumText(EnumUtils.getInfoByValue(RequestStatus.class, infoDto.getStatusEnum()));
            infoDto.setRxTypeCode_enumText(EnumUtils.getInfoByValue(PrescriptionType.class, infoDto.getRxTypeCode()));
        });
        return R.ok(prescriptionInfo);
    }

    /**
     * 获取药品信息
     *
     * @param prescriptionNo 处方号
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 药品详细信息
     */
    @Override
    public R<?> getMedicationInfo(String prescriptionNo, Integer pageNo, Integer pageSize) {
        // 药品详细查询
        IPage<ElepMedicationInfoDto> medicationInfo =
            elepPrescriptionMapper.selectMedicationInfo(new Page<>(pageNo, pageSize), prescriptionNo);
        return R.ok(medicationInfo);
    }

    /**
     * 获取处方编号
     *
     * @return 初始化信息
     */
    @Override
    public R<?> prescriptionNoInit() {
        // 单据号
        String prescriptionNo = assignSeqUtil.getSeqByDay(AssignSeqEnum.ELEP_MEDICATION_NO.getPrefix(), 10);

        return R.ok(prescriptionNo);
    }

    /**
     * 保存处方
     *
     * @param prescriptionInfo 处方信息
     * @return 执行结果
     */
    @Override
    public R<?> savePrescriptionInfo(ElepPrescriptionInfoParam prescriptionInfo) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());
        // 获取当前时间
        Date now = DateUtils.getNowDate();

        // 根据处方号查询处方信息
        List<ElepMedicationRequest> requestList = elepMedicationRequestService.selectElepMedicationRequestByPrescriptionNo(prescriptionInfo.getPrescriptionNo());
        if (!requestList.isEmpty()) {
            List<Integer> idList = requestList.stream().map(ElepMedicationRequest::getId).collect(Collectors.toList());
            // 处方信息删除
            elepMedicationRequestService.removeByIds(idList);
        }

        // 搜索更表需要信息
        ElepPrescriptionInfoParam info = elepPrescriptionMapper.selectSaveInfo(prescriptionInfo.getEncounterId());
       if(info.getConditionId() == null){
           return R.fail("请先新增主诊断");
       }

        // 设置处方信息
        List<ElepMedicationRequest> elepMedicationRequestList = new ArrayList<>();
        ElepMedicationRequest elepMedicationRequest;
        for (ElepMedicationInfoDto item : prescriptionInfo.getMedicationInfoList()) {
            elepMedicationRequest = new ElepMedicationRequest();
            elepMedicationRequest
                // ID
                .setId(null)
                // 医院内部处方编号
                .setPrescriptionNo(prescriptionInfo.getPrescriptionNo())
                // 医院id
                .setOrganizationId(info.getOrganizationId())
                // 门诊/住院病历号
                .setIptOtpNo(info.getIptOtpNo())
                // 科室病区
                .setDepartmentWard(info.getDepartmentWard())
                // 医保类型
                .setInsuranceEnum(info.getInsuranceEnum())
                // 开具日期
                .setIssueTime(now)
                // 开具科室
                .setOrgId(user.getOrgId())
                // 患者
                .setPatientId(prescriptionInfo.getPatientId())
                // 就诊id
                .setEncounterId(prescriptionInfo.getEncounterId())
                // 诊断id
                .setConditionId(info.getConditionId())
                // 有效天数
                .setValidityDays(prescriptionInfo.getValidityDays())
                // 药品定义id
                .setMedicationId(item.getMedicationId())
                // 药品剂量
                .setMedDosage(item.getMedDosage())
                // 药品剂量单位
                .setMedDosageUnitCode(item.getMedDosageUnitCode())
                // 药品频率
                .setMedFrequency(item.getMedFrequency())
                // 药品途径
                .setMedRoute(item.getMedRoute())
                // 请求数量
                .setQuantity(item.getQuantity())
                // 请求单位
                .setUnitCode(item.getUnitCode())
                // 开方医师
                .setPrescribingDrId(user.getId())
                // 处方状态
                .setStatusEnum(RequestStatus.DRAFT.getValue())
                // 延长原因
                .setExtensionReason(prescriptionInfo.getExtensionReason())
                // 处方类别
                .setRxTypeCode(item.getRxTypeCode())
                // 支持用药信息
                .setSupportInfo(item.getSupportInfo())
                // 服药时间(开始)
                .setEffectiveDoseStart(item.getEffectiveDoseStart())
                // 服药时间(结束)
                .setEffectiveDoseEnd(item.getEffectiveDoseEnd())
                // 给药间隔
                .setDispenseInterval(item.getDispenseInterval())
                // 单次发药数
                .setDispensePerQuantity(item.getDispensePerQuantity())
                // 每次发药供应天数
                .setDispensePerDuration(item.getDispensePerDuration());
            elepMedicationRequestList.add(elepMedicationRequest);
        }

        // 保存处方
        boolean flg = elepMedicationRequestService.saveBatch(elepMedicationRequestList);
        if (!flg) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"电子处方"}));
    }

    /**
     * 修改处方
     *
     * @param prescriptionInfo 处方信息
     * @return 执行结果
     */
    @Override
    public R<?> updatePrescriptionInfo(ElepPrescriptionInfoParam prescriptionInfo) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());
        // 获取当前时间
        Date now = DateUtils.getNowDate();

        // 设置处方信息
        ElepMedicationRequest elepMedicationRequest;
        List<ElepMedicationRequest> elepMedicationRequestList;

        // 修改处方信息
        if (prescriptionInfo.getId() == null) {
            elepMedicationRequestList = elepMedicationRequestService
                .selectElepMedicationRequestByPrescriptionNo(prescriptionInfo.getPrescriptionNo());
            // 判断信息是否存在
            if (elepMedicationRequestList == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
            }
            for (ElepMedicationRequest item : elepMedicationRequestList) {
                item
                    // 开具日期
                    .setIssueTime(now)
                    // 开具科室
                    .setOrgId(user.getOrgId())
                    // 开方医师
                    .setPrescribingDrId(user.getId())
                    // 有效天数
                    .setValidityDays(prescriptionInfo.getValidityDays())
                    // 延长原因
                    .setExtensionReason(prescriptionInfo.getExtensionReason())
                    // 处方类别
                    .setRxTypeCode(prescriptionInfo.getRxTypeCode());
            }
            // 修改处方
            boolean flg = elepMedicationRequestService.updateBatchById(elepMedicationRequestList);
            if (!flg) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
            }
        }
        // 修改药品信息
        else {
            elepMedicationRequest = elepMedicationRequestService.getById(prescriptionInfo.getId());
            // 判断信息是否存在
            if (elepMedicationRequest == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
            }
            elepMedicationRequest
                // 开具日期
                .setIssueTime(now)
                // 开具科室
                .setOrgId(user.getOrgId())
                // 开方医师
                .setPrescribingDrId(user.getId())
                // 药品定义id
                .setMedicationId(prescriptionInfo.getMedicationId())
                // 药品剂量
                .setMedDosage(prescriptionInfo.getMedDosage())
                // 药品剂量单位
                .setMedDosageUnitCode(prescriptionInfo.getMedDosageUnitCode())
                // 药品频率
                .setMedFrequency(prescriptionInfo.getMedFrequency())
                // 药品途径
                .setMedRoute(prescriptionInfo.getMedRoute())
                // 请求数量
                .setQuantity(prescriptionInfo.getQuantity())
                // 请求单位
                .setUnitCode(prescriptionInfo.getUnitCode())
                // 支持用药信息
                .setSupportInfo(prescriptionInfo.getSupportInfo())
                // 服药时间(开始)
                .setEffectiveDoseStart(prescriptionInfo.getEffectiveDoseStart())
                // 服药时间(结束)
                .setEffectiveDoseEnd(prescriptionInfo.getEffectiveDoseEnd())
                // 给药间隔
                .setDispenseInterval(prescriptionInfo.getDispenseInterval())
                // 单次发药数
                .setDispensePerQuantity(prescriptionInfo.getDispensePerQuantity())
                // 每次发药供应天数
                .setDispensePerDuration(prescriptionInfo.getDispensePerDuration());

            // 修改处方
            boolean flg = elepMedicationRequestService.updateById(elepMedicationRequest);
            if (!flg) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
            }
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"电子处方"}));
    }

    /**
     * 删除处方
     *
     * @param deletePrescriptionInfoParam 删除处方信息
     * @return 执行结果
     */
    @Override
    public R<?> deletePrescriptionInfo(DeletePrescriptionInfoParam deletePrescriptionInfoParam) {

        // 设置处方信息
        List<ElepMedicationRequest> elepMedicationRequestList;

        if (deletePrescriptionInfoParam.getIdList().isEmpty()) {
            elepMedicationRequestList = elepMedicationRequestService
                .selectElepMedicationRequestByPrescriptionNoList(deletePrescriptionInfoParam.getPrescriptionNoList());
            // 判断信息是否存在
            if (elepMedicationRequestList == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
            }
            // 删除处方
            boolean flg = elepMedicationRequestService
                .deleteElepMedicationRequestByPrescriptionNo(deletePrescriptionInfoParam.getPrescriptionNoList());
            if (!flg) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
            }
        } else {
            elepMedicationRequestList =
                elepMedicationRequestService.selectElepMedicationRequestById(deletePrescriptionInfoParam.getIdList());
            // 判断信息是否存在
            if (elepMedicationRequestList == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
            }
            // 删除处方
            boolean flg = elepMedicationRequestService.removeBatchByIds(elepMedicationRequestList);
            if (!flg) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
            }
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"电子处方"}));
    }

    /**
     * 签发处方
     *
     * @param prescriptionNoList 处方号
     * @return 药品详细信息
     */
    @Override
    public R<?> issuancePrescription(List<String> prescriptionNoList) {
        // 搜索处方信息
        List<ElepMedicationRequest> elepMedicationRequestList =
            elepMedicationRequestService.selectElepMedicationRequestByPrescriptionNoList(prescriptionNoList);
        // 判断处方是否存在
        if (elepMedicationRequestList == null) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        for (ElepMedicationRequest item : elepMedicationRequestList) {
            // 处方状态
            item.setStatusEnum(RequestStatus.ACTIVE.getValue());
        }
        // 签发处方
        boolean flg = elepMedicationRequestService.updateBatchById(elepMedicationRequestList);
        if (!flg) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"电子处方"}));
    }
}
