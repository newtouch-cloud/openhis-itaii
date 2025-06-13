/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.service;

import java.util.Date;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.ybmanage.dto.VeriPrescriptionParam;
import com.openhis.ybelep.domain.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author yuxj
 * @date 2025-04-17
 */
public interface IYbEleBaseService {

    /**
     * 医保电子处方查询
     *
     * @param veriPrescriptionParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求数据
     * @return 处方信息
     */
    R<?> getVeriPrescriptionInfo(VeriPrescriptionParam veriPrescriptionParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 医保电子处方查看
     *
     * @param prescriptionNo 处方号
     * @return 处方详细信息
     */
    R<?> getPrescriptionDetailInfo(String prescriptionNo);

    /**
     * 医保电子处方状态更新（上传）
     *
     * @param prescriptionNo 处方号
     * @return 处方信息
     */
    R<?> uploadPrescriptionStatus(String prescriptionNo);

    /**
     * 医保电子处方拒绝上传
     *
     * @param prescriptionNo 处方号
     * @return 处方信息
     */
    R<?> refusePrescriptionStatus(String prescriptionNo);

    /**
     * 医保电子处方状态更新（撤销）
     *
     * @param prescriptionNo 处方号
     * @param quashReason 撤销原因
     * @return 处方信息
     */
    R<?> quashPrescriptionStatus(String prescriptionNo,String quashReason);

    /**
     * 做成电子处方上传预核验入参信息
     *
     * @param prescriptionNo 处方号
     * @param tenantId 租户Id
     * @return 处方信息
     */
    PreCheckPrescription makePreCheckPrescription(String prescriptionNo,
        Integer tenantId);

    /**
     * 电子处方上传预核验信息保存
     *
     * @param pcpResult 电子处方上传预核验信息
     * @return 电子处方上传预核验信息
     */
    void savePreCheckPrescription(PreCheckPrescription pcpResult);

    /**
     * 电子处方上传预核验响应出参信息保存
     *
     * @param pcpResult 电子处方上传预核验响应出参信息
     * @return
     */
    void saveEleVeriPrescriptionOutput(ElepVeriPrescriptionOutput pcpResult);

    /**
     * 做成电子处方医保电子签名入参
     *
     * @param hiRxno 医保处方编号
     * @param practitionerId 审方药师Id
     * @param checkDate 审方时间
     * @param tenantId 租户Id
     * @return 电子处方医保电子签名入参
     */
    ElepSignatureInput makeEleSignature(String hiRxno, Long practitionerId, Date checkDate, Integer tenantId);

    /**
     * 保存电子处方医保电子签名入参
     *
     * @param eSign 电子处方医保电子签名入参信息
     * @return
     */
    void saveEleSignature(ElepSignatureInput eSign);

    /**
     * 保存电子处方医保电子签名响应出参
     *
     * @param esResult 电子处方医保电子签名响应出参信息
     * @return
     */
    void saveEleSignatureOutput(ElepSignatureOutput esResult);

    /**
     * 做成电子处方上传入参
     *
     * @param hiRxno 医保处方编号
     * @param practitionerId 审方药师Id
     * @param checkDate 审方时间
     * @param tenantId 租户Id
     * @return 电子处方上传入参
     */
    ElepUploadInput makeEleUploadInput(String hiRxno, Long practitionerId, Date checkDate, Integer tenantId);

    /**
     * 保存电子处方上传入参
     *
     * @param eleUploadInput 电子处方上传入参信息
     * @return
     */
    void saveEleUploadInput(ElepUploadInput eleUploadInput);

    /**
     * 保存电子处方上传响应出参
     *
     * @param euResult 电子处方上传响应出参信息
     * @return
     */
    void saveEleUploadOutput(ElepUploadOutput euResult);

    /**
     * 做成电子处方撤销入参
     *
     * @param hiRxno 医保处方编号
     * @param practitionerId 撤销药师Id
     * @param description 撤销原因
     * @param checkDate 撤销时间
     * @param tenantId 租户Id
     * @return 电子处方撤销入参
     */
    ElepRevokeInput makeEleRevokeInput(String hiRxno, Long practitionerId,String description, Date checkDate, Integer tenantId);

    /**
     * 保存电子处方撤销入参
     *
     * @param eleRevokeInput 电子处方撤销入参信息
     * @return
     */
    void saveEleRevokeInput(ElepRevokeInput eleRevokeInput);

    /**
     * 保存电子处方撤销响应出参
     *
     * @param ereResult 电子处方撤销响应出参信息
     * @return
     */
    void saveEleRevokeOutput(ElepRevokeOutput ereResult);

    /**
     * 做成电子处方信息查询入参
     *
     * @param hiRxno 医保处方编号
     * @return 电子处方信息查询入参
     */
    ElepQuerPrescriptionInput makeEleQueryPrescriptionInput(String hiRxno);

    /**
     * 保存电子处方信息查询入参
     *
     * @param eleQueryPreInput 电子处方信息查询入参信息
     * @return
     */
    void saveEleQueryPrescriptionInput(ElepQuerPrescriptionInput eleQueryPreInput);

    /**
     * 保存电子处方信息查询响应出参信息
     *
     * @param emrResult 电子处方信息查询响应出参信息
     * @return
     */
    void saveEleMedResultOut(QueryPrescription emrResult);

    /**
     * 做成电子处方取药结果查询入参
     *
     * @param hiRxno 医保处方编号
     * @return 电子处方取药结果查询入参
     */
    ElepMedresultInput makeEleMedResultInput(String hiRxno);

    /**
     * 保存电子处方取药结果查询入参
     *
     * @param eleMedInput 电子处方取药结果查询入参信息
     * @return
     */
    void saveEleMedResultInput(ElepMedresultInput eleMedInput);

    /**
     * 保存电子处方取药结果查询响应出参
     *
     * @param medResInquiry 电子处方取药结果查询响应出参信息
     * @return
     */
    void saveMedicationResultInquiry(MedicationResultInquiry medResInquiry);

}
