/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.IEncounterService;
import com.openhis.administration.service.IPatientService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.PrescriptionType;
import com.openhis.common.enums.RequestStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.ybmanage.dto.VeriPrescriptionDetailInfoDto;
import com.openhis.web.ybmanage.dto.VeriPrescriptionInfoDto;
import com.openhis.web.ybmanage.dto.VeriPrescriptionParam;
import com.openhis.web.ybmanage.mapper.YbElepMapper;
import com.openhis.workflow.domain.ElepMedicationRequest;
import com.openhis.workflow.service.IElepMedicationRequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.openhis.web.ybmanage.service.IYbEleBaseService;
import com.openhis.web.ybmanage.util.YbEleParamBuilderUtil;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.service.IRegService;
import com.openhis.ybelep.domain.*;
import com.openhis.ybelep.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 医保表的增删改查接口
 *
 * @author yuxj
 * @date 2025-04-17
 */
@Service
public class YbElepBaseServiceImpl implements IYbEleBaseService {

    @Autowired
    IRegService iRegService;
    @Autowired
    YbEleParamBuilderUtil ybEleUtil;
    @Autowired
    IElepVeriPrescriptionInfoService eleVeriPrescriptionInfoService;
    @Autowired
    IElepVeriPrescriptionDetailService eleVeriPrescriptionDetailService;
    @Autowired
    IElepVeriVisitInfoService eleVeriVisitInfoService;
    @Autowired
    IElepVeriDiagnosisInfoService eleVeriDiagnosisInfoService;
    @Autowired
    IElepVeriPrescriptionOutputService elePresOutputService;
    @Autowired
    IElepSignatureInputService eleSignInfoService;
    @Autowired
    IElepSignatureOutputService eleSignOutService;
    @Autowired
    IElepUploadInputService eleUploadInputService;
    @Autowired
    IElepUploadOutputService eleUploadOutputService;
    @Autowired
    IElepRevokeInputService eleRevokeInputService;
    @Autowired
    IElepRevokeOutputService eleRevokeOutputService;
    @Autowired
    IElepMedresultInputService eleMedResultInputService;
    @Autowired
    IElepQuerPrescriptionInputService eleQueryPreInputService;
    @Autowired
    IElepMedresultInfoService eleMedResInfoService;
    @Autowired
    IElepMedresultInputService eleMedResInputService;
    @Autowired
    IElepMedresultDetailService eleMedDetailService;
    @Autowired
    YbElepMapper ybElepMapper;
    @Autowired
    IElepMedicationRequestService elepMedicationRequestService;
    @Autowired
    IElepQuerPrescriptionInfoService eleQuerPrescriptionInfoService;
    @Autowired
    IElepQuerPrescriptionDetailService eleQuerPrescriptionDetailService;
    @Autowired
    IElepQuerVisitInfoService eleQuerVisitInfoService;
    @Autowired
    IElepQuerDiagnosisInfoService eleQuerDiagnosisInfoService;
    @Resource
    IPractitionerService practitionerService;
    @Resource
    IEncounterService encounterService;
    @Resource
    IPatientService patientService;

    /**
     * 医保电子处方查询
     *
     * @param veriPrescriptionParam 查询条件
     * @param searchKey             模糊查询关键字
     * @param pageNo                当前页
     * @param pageSize              每页多少条
     * @param request               请求数据
     * @return 处方信息
     */
    @Override
    public R<?> getVeriPrescriptionInfo(VeriPrescriptionParam veriPrescriptionParam, String searchKey, Integer pageNo,
                                        Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<VeriPrescriptionParam> queryWrapper =
                HisQueryUtils.buildQueryWrapper(veriPrescriptionParam, searchKey,
                        new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientName, CommonConstants.FieldName.IptOtpNo)),
                        request);

        // 医保电子处方查询
        IPage<VeriPrescriptionInfoDto> veriPrescriptionInfo =
                ybElepMapper.getVeriPrescriptionInfo(new Page<>(pageNo, pageSize), queryWrapper);
        // 状态转换
        veriPrescriptionInfo.getRecords().forEach(prescriptionInfoDto -> {
            prescriptionInfoDto.setStatusEnum_enumText(
                    EnumUtils.getInfoByValue(RequestStatus.class, prescriptionInfoDto.getStatusEnum()));
        });

        return R.ok(veriPrescriptionInfo);
    }

    /**
     * 医保电子处方查看
     *
     * @param prescriptionNo 处方号
     * @return 处方详细信息
     */
    @Override
    public R<?> getPrescriptionDetailInfo(String prescriptionNo) {
        // 处方详细查询
        List<VeriPrescriptionDetailInfoDto> prescriptionDetailInfo =
                ybElepMapper.getVeriPrescriptionDetailInfo(prescriptionNo);
        // 状态转换
        for (VeriPrescriptionDetailInfoDto item : prescriptionDetailInfo) {
            item.setStatusEnum_enumText(EnumUtils.getInfoByValue(RequestStatus.class, item.getStatusEnum()));
            item.setRxTypeCode_enumText(EnumUtils.getInfoByValue(PrescriptionType.class, item.getRxTypeCode()));
        }
        return R.ok(prescriptionDetailInfo);
    }

    /**
     * 医保电子处方状态更新(上传)
     *
     * @param prescriptionNo 处方号
     * @return 处方信息
     */
    @Override
    public R<?> uploadPrescriptionStatus(String prescriptionNo) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 获取医保处方号
        ElepVeriPrescriptionOutput prescriptionOutput =
                elePresOutputService.getOne(new LambdaQueryWrapper<ElepVeriPrescriptionOutput>()
                        .eq(ElepVeriPrescriptionOutput::getPrescriptionNo, prescriptionNo));
        if (prescriptionOutput == null) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
        }
        // 获取电子处方信息
        List<ElepMedicationRequest> elepMedicationRequest =
                elepMedicationRequestService.selectElepMedicationRequestByPrescriptionNo(prescriptionNo);
        if (elepMedicationRequest == null) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
        }
        boolean flg;
        // 更改处方信息
        for (ElepMedicationRequest item : elepMedicationRequest) {
            // 医保处方编号
            item.setHiRxno(prescriptionOutput.getHiRxno());
            // 电子处方追溯码
            item.setRxTraceCode(prescriptionOutput.getRxTraceCode());
            // 处方状态
            item.setStatusEnum(RequestStatus.COMPLETED.getValue());
            // 审核药师
            item.setReviewDrId(user.getId());
            // 审核时间
            item.setReviewTime(now);
            flg = elepMedicationRequestService.updateById(item);
            if (!flg) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 医保电子处方拒绝上传
     *
     * @param prescriptionNo 处方号
     * @return 处方信息
     */
    @Override
    public R<?> refusePrescriptionStatus(String prescriptionNo) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 获取电子处方信息
        List<ElepMedicationRequest> elepMedicationRequest =
                elepMedicationRequestService.selectElepMedicationRequestByPrescriptionNo(prescriptionNo);
        if (elepMedicationRequest == null) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
        }
        boolean flg;
        // 更改处方信息
        for (ElepMedicationRequest item : elepMedicationRequest) {
            // 处方状态
            item.setStatusEnum(RequestStatus.ENDED.getValue());
            flg = elepMedicationRequestService.updateById(item);
            if (!flg) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 医保电子处方状态更新（撤销）
     *
     * @param prescriptionNo 处方号
     * @param quashReason    撤销原因
     * @return 处方信息
     */
    @Override
    public R<?> quashPrescriptionStatus(String prescriptionNo, String quashReason) {
        // 获取当前登陆用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 获取电子处方信息
        List<ElepMedicationRequest> elepMedicationRequest =
                elepMedicationRequestService.selectElepMedicationRequestByPrescriptionNo(prescriptionNo);
        if (elepMedicationRequest == null) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
        }
        boolean flg;
        // 更改处方信息
        for (ElepMedicationRequest item : elepMedicationRequest) {
            // 处方状态
            item.setStatusEnum(RequestStatus.CANCELLED.getValue());
            // 撤销药师
            item.setQuashDrId(user.getId());
            // 撤销时间
            item.setQuashTime(now);
            // 撤销原因
            item.setQuashReason(quashReason);
            flg = elepMedicationRequestService.updateById(item);
            if (!flg) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 做成电子处方上传预核验入参信息
     *
     * @param prescriptionNo 处方号
     * @param tenantId       租户Id
     * @return 处方信息
     */
    @Override
    public PreCheckPrescription makePreCheckPrescription(String prescriptionNo,
                                                         Integer tenantId) {

        // 获取药品请求信息(处方表)
        List<ElepMedicationRequest> medicationRequestList = elepMedicationRequestService.list(
                new LambdaQueryWrapper<ElepMedicationRequest>().eq(ElepMedicationRequest::getPrescriptionNo, prescriptionNo)
                        .eq(ElepMedicationRequest::getTenantId, tenantId));
        if (medicationRequestList == null) {
            return null;
        }
        ElepMedicationRequest medicationRequest = medicationRequestList.get(0);

        // 就诊信息
        Encounter encounter = encounterService.getById(medicationRequest.getEncounterId());
        //患者信息
        Patient patient = patientService.getById(encounter.getPatientId());
        // 获取医保挂号信息     todo  clinicReg可能改成一对多
        ClinicReg clinicReg = iRegService.getByBusNo(encounter.getBusNo());
        if (clinicReg == null) {
            return null;
        }

        // 获取处方信息
        PreCheckPrescription prescriptionInfo =
                ybEleUtil.getEleVeriPrescriptionInfo(medicationRequest, patient, tenantId);
        // 获取处方明细信息
        List<ElepVeriPrescriptionDetail> rxdrugdetail =
                ybEleUtil.getEleVeriPrescriptionDetail(prescriptionNo, tenantId);

        // 获取就诊信息和诊断信息
        PreCheckPrescription eleVisAndDisInfo =
                ybEleUtil.getEleVeriVisitAndDiagnosisInfo(medicationRequest, clinicReg, tenantId);

        // 电子处方上传预核验实体赋值

        prescriptionInfo.setRxdrugdetail(rxdrugdetail)
                .setMdtrtinfo(eleVisAndDisInfo.getMdtrtinfo()).setDiseinfo(eleVisAndDisInfo.getDiseinfo());

        return prescriptionInfo;
    }

    /**
     * 电子处方上传预核验信息保存
     *
     * @param pcpResult 电子处方上传预核验信息
     * @return
     */
    @Override
    public void savePreCheckPrescription(PreCheckPrescription pcpResult) {

        //todo dto和表的字段类型不一致
        ElepVeriPrescriptionInfo prescriptionInfo = new ElepVeriPrescriptionInfo();
        BeanUtils.copyProperties(pcpResult, prescriptionInfo);

        // 保存处方信息
        eleVeriPrescriptionInfoService.save(prescriptionInfo);
        // 保存处方明细信息
        eleVeriPrescriptionDetailService.saveBatch(pcpResult.getRxdrugdetail());
        // 保存就诊信息
        eleVeriVisitInfoService.save(pcpResult.getMdtrtinfo());
        // 保存诊断信息
        eleVeriDiagnosisInfoService.save(pcpResult.getDiseinfo());
    }


    /**
     * 电子处方上传预核验响应出参信息保存
     *
     * @param pcpResult 电子处方上传预核验响应出参信息
     * @return
     */
    @Override
    public void saveEleVeriPrescriptionOutput(ElepVeriPrescriptionOutput pcpResult) {
        elePresOutputService.save(pcpResult);
    }

    /**
     * 做成电子处方医保电子签名入参
     *
     * @param hiRxno         医保处方编号
     * @param practitionerId 审方药师Id
     * @param checkDate      审方时间
     * @param tenantId       租户Id
     * @return 处方信息
     */
    @Override
    public ElepSignatureInput makeEleSignature(String hiRxno, Long practitionerId, Date checkDate, Integer tenantId) {

        ElepVeriPrescriptionOutput pcpResult = elePresOutputService.getOne(
                new LambdaQueryWrapper<ElepVeriPrescriptionOutput>().eq(ElepVeriPrescriptionOutput::getHiRxno, hiRxno));
        if (pcpResult == null) {
            return null;
        }
        return ybEleUtil.getEleSignatureInput(pcpResult, practitionerId, checkDate, tenantId);
    }

    /**
     * 保存电子处方医保电子签名入参
     *
     * @param eSign 电子处方医保电子签名入参信息
     * @return
     */
    @Override
    public void saveEleSignature(ElepSignatureInput eSign) {
        eleSignInfoService.save(eSign);
    }

    /**
     * 保存电子处方医保电子签名响应出参
     *
     * @param esResult 电子处方医保电子签名响应出参信息
     * @return
     */
    @Override
    public void saveEleSignatureOutput(ElepSignatureOutput esResult) {
        eleSignOutService.save(esResult);
    }

    /**
     * 做成电子处方上传入参
     *
     * @param hiRxno         医保处方编号
     * @param practitionerId 审方药师Id
     * @param checkDate      审方时间
     * @param tenantId       租户Id
     * @return 电子处方上传入参
     */
    @Override
    public ElepUploadInput makeEleUploadInput(String hiRxno, Long practitionerId, Date checkDate, Integer tenantId) {

        ElepVeriPrescriptionOutput pcpResult = elePresOutputService.getOne(
                new LambdaQueryWrapper<ElepVeriPrescriptionOutput>().eq(ElepVeriPrescriptionOutput::getHiRxno, hiRxno));

        ElepSignatureOutput esResult = eleSignOutService
                .getOne(new LambdaQueryWrapper<ElepSignatureOutput>().eq(ElepSignatureOutput::getHiRxno, hiRxno));

        if (pcpResult == null || esResult == null) {
            return null;
        }
        return ybEleUtil.getEleUploadInput(pcpResult, esResult, practitionerId, checkDate, tenantId);
    }

    /**
     * 保存电子处方上传入参
     *
     * @param eleUploadInput 电子处方上传入参信息
     * @return
     */
    @Override
    public void saveEleUploadInput(ElepUploadInput eleUploadInput) {
        eleUploadInputService.save(eleUploadInput);
    }

    /**
     * 保存电子处方上传响应出参
     *
     * @param euResult 电子处方上传响应出参信息
     * @return
     */
    @Override
    public void saveEleUploadOutput(ElepUploadOutput euResult) {
        eleUploadOutputService.save(euResult);
    }

    /**
     * 做成电子处方撤销入参
     *
     * @param hiRxno         医保处方编号
     * @param practitionerId 撤销药师Id
     * @param description    撤销原因
     * @param revokeDate     撤销时间
     * @param tenantId       租户Id
     * @return 电子处方撤销入参
     */
    @Override
    public ElepRevokeInput makeEleRevokeInput(String hiRxno, Long practitionerId, String description, Date revokeDate,
                                              Integer tenantId) {

        ElepVeriPrescriptionOutput pcpResult = elePresOutputService.getOne(
                new LambdaQueryWrapper<ElepVeriPrescriptionOutput>().eq(ElepVeriPrescriptionOutput::getHiRxno, hiRxno));
        if (pcpResult == null) {
            return null;
        }
        ElepUploadOutput euResult = eleUploadOutputService
                .getOne(new LambdaQueryWrapper<ElepUploadOutput>().eq(ElepUploadOutput::getHiRxno, pcpResult.getHiRxno()));

        if (euResult == null) {
            return null;
        }

        return ybEleUtil.getElepRevokeInput(pcpResult, euResult, practitionerId, description, revokeDate, tenantId);
    }

    /**
     * 保存电子处方撤销入参
     *
     * @param eleRevokeInput 电子处方撤销入参信息
     * @return
     */
    @Override
    public void saveEleRevokeInput(ElepRevokeInput eleRevokeInput) {
        eleRevokeInputService.save(eleRevokeInput);
    }

    /**
     * 保存电子处方撤销响应出参
     *
     * @param ereResult 电子处方撤销响应出参信息
     * @return
     */
    @Override
    public void saveEleRevokeOutput(ElepRevokeOutput ereResult) {

        eleRevokeOutputService.save(ereResult);
    }

    /**
     * 做成电子处方信息查询入参
     *
     * @param hiRxno 医保处方编号
     * @return 电子处方信息查询入参
     */
    @Override
    public ElepQuerPrescriptionInput makeEleQueryPrescriptionInput(String hiRxno) {

        ElepVeriPrescriptionOutput pcpResult = elePresOutputService.getOne(
                new LambdaQueryWrapper<ElepVeriPrescriptionOutput>().eq(ElepVeriPrescriptionOutput::getHiRxno, hiRxno));

        if (pcpResult == null) {
            return null;
        }
        return ybEleUtil.getEleQueryPrescriptionInput(pcpResult);
    }

    /**
     * 保存电子处方信息查询入参
     *
     * @param eleQueryPreInput 电子处方信息查询入参信息
     * @return
     */
    @Override
    public void saveEleQueryPrescriptionInput(ElepQuerPrescriptionInput eleQueryPreInput) {
        eleQueryPreInputService.save(eleQueryPreInput);
    }

    /**
     * 保存电子处方信息查询响应出参信息
     *
     * @param emrResult 电子处方信息查询响应出参信息
     * @return
     */
    @Override
    public void saveEleMedResultOut(QueryPrescription emrResult) {

        ElepQuerPrescriptionInfo prescriptionInfo = new ElepQuerPrescriptionInfo();
        BeanUtils.copyProperties(emrResult, prescriptionInfo);

        // 保存处方信息
        eleQuerPrescriptionInfoService.save(prescriptionInfo);
        // 保存处方明细信息
        eleQuerPrescriptionDetailService.saveBatch(emrResult.getRxDetlList());
        // 保存就诊信息
        eleQuerVisitInfoService.save(emrResult.getRxOtpinfo());
        // 保存诊断信息
        eleQuerDiagnosisInfoService.saveBatch(emrResult.getRxDiseList());

    }

    /**
     * 做成电子处方取药结果查询入参
     *
     * @param hiRxno 医保处方编号
     * @return 电子处方取药结果查询入参
     */
    @Override
    public ElepMedresultInput makeEleMedResultInput(String hiRxno) {

        ElepVeriPrescriptionOutput pcpResult = elePresOutputService.getOne(
                new LambdaQueryWrapper<ElepVeriPrescriptionOutput>().eq(ElepVeriPrescriptionOutput::getHiRxno, hiRxno));

        if (pcpResult == null ) {
            return null;
        }

        return ybEleUtil.getEleMedResultInput(pcpResult);
    }

    /**
     * 保存电子处方取药结果查询入参
     *
     * @param eleMedInput 电子处方取药结果查询入参信息
     * @return
     */
    @Override
    public void saveEleMedResultInput(ElepMedresultInput eleMedInput) {
        eleMedResInputService.save(eleMedInput);
    }

    /**
     * 保存电子处方取药结果查询响应出参
     *
     * @param medResInquiry 电子处方取药结果查询响应出参信息
     * @return
     */
    @Override
    public void saveMedicationResultInquiry(MedicationResultInquiry medResInquiry) {
        ElepMedresultInfo medresultInfo = new ElepMedresultInfo();
        BeanUtils.copyProperties(medResInquiry, medresultInfo);

        // 保存处方信息
        eleMedResInfoService.save(medresultInfo);
        eleMedDetailService.saveBatch(medResInquiry.getSeltdelts());
    }

}
