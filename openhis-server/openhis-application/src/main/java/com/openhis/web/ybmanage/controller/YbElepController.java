/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.controller;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.ybmanage.dto.VeriPrescriptionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.ybmanage.service.IYbEleBaseService;
import com.openhis.web.ybmanage.service.IYbEleHttpService;
import com.openhis.ybelep.domain.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 医保电子处方业务接口
 *
 * @author yuxj
 * @date 2025-04-17
 */
@RestController
@RequestMapping("/ybelep-request")
public class YbElepController {

    @Autowired
    IYbEleBaseService ybEleBaseService;

    @Autowired
    IYbEleHttpService ybEleHttpService;

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
    @GetMapping(value = "/get-PrescriptionInfo")
    public R<?> getVeriPrescriptionInfo(VeriPrescriptionParam veriPrescriptionParam,
                                        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return ybEleBaseService.getVeriPrescriptionInfo(veriPrescriptionParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 医保电子处方查看
     *
     * @param prescriptionNo 处方号
     * @return 处方详细信息
     */
    @GetMapping(value = "/get-PrescriptionDetailInfo")
    public R<?> getPrescriptionDetailInfo(String prescriptionNo) {
        return ybEleBaseService.getPrescriptionDetailInfo(prescriptionNo);
    }

    /**
     * 医保电子处方状态更新（上传）
     *
     * @param prescriptionNo 处方号
     * @return 处方信息
     */
    @PutMapping(value = "/uploadPrescriptionStatus")
    public R<?> uploadPrescriptionStatus(String prescriptionNo) {
        return ybEleBaseService.uploadPrescriptionStatus(prescriptionNo);
    }

    /**
     * 医保电子处方拒绝上传
     *
     * @param prescriptionNo 处方号
     * @return 处方信息
     */
    @PutMapping(value = "/refusePrescriptionStatus")
    public R<?> refusePrescriptionStatus(String prescriptionNo) {
        return ybEleBaseService.refusePrescriptionStatus(prescriptionNo);
    }

    /**
     * 医保电子处方状态更新（撤销）
     *
     * @param prescriptionNo 处方号
     * @param quashReason    撤销原因
     * @return 处方信息
     */
    @PutMapping(value = "/quashPrescriptionStatus")
    public R<?> quashPrescriptionStatus(String prescriptionNo, String quashReason) {
        return ybEleBaseService.quashPrescriptionStatus(prescriptionNo, quashReason);
    }

    /**
     * 电子处方上传预核验
     *
     * @param prescriptionNo 处方信息
     * @param authNo         电子凭证线上身份核验流水号
     * @param ecToken        电子凭证令牌
     * @param tenantId       租户Id
     * @return
     */
    @GetMapping("/pre-verification")
    public R<?> preVerification(String prescriptionNo, String ecToken, String authNo, Integer tenantId) {
        PreCheckPrescription pcp = ybEleBaseService.makePreCheckPrescription(prescriptionNo, tenantId);
        ElepVeriPrescriptionOutput pcpResult = ybEleHttpService.preCheck(pcp);
        // 入参和出参 保存
        if (pcp != null) {
            pcp.getMdtrtinfo().setPrescriptionNo(prescriptionNo);
            ybEleBaseService.savePreCheckPrescription(pcp);
        }
        if (pcpResult != null) {
            pcpResult.setPrescriptionNo(prescriptionNo);
            ybEleBaseService.saveEleVeriPrescriptionOutput(pcpResult);
        }

        return R.ok(pcpResult);
    }

    /**
     * 电子处方医保电子签名
     *
     * @param hiRxno         医保处方编号
     * @param practitionerId 审方药师Id
     * @param checkDate      审方时间
     * @param tenantId       租户Id
     * @return
     */
    @GetMapping("/pre-signature")
    public R<?> eleSignature(String hiRxno, Long practitionerId,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date checkDate, Integer tenantId) {

        ElepSignatureInput eleSignature =
                ybEleBaseService.makeEleSignature(hiRxno, practitionerId, checkDate, tenantId);
        ElepSignatureOutput esResult = ybEleHttpService.eleSign(eleSignature);

        // 入参和出参都保存
        if (eleSignature != null) {
            eleSignature.setHiRxno(hiRxno);
            ybEleBaseService.saveEleSignature(eleSignature);
        }
        // 入参和出参都保存
        if (esResult != null) {
            esResult.setHiRxno(hiRxno);
            ybEleBaseService.saveEleSignatureOutput(esResult);
        }
        return R.ok(esResult);
    }

    /**
     * 电子处方上传
     *
     * @param hiRxno         医保处方编号
     * @param practitionerId 审方药师Id
     * @param checkDate      审方时间
     * @param tenantId       租户Id
     * @return
     */
    @GetMapping("/pre-upload")
    public R<?> uploadElePrescription(String hiRxno, Long practitionerId,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date checkDate, Integer tenantId) {
         ElepUploadInput eleUploadInput =
                ybEleBaseService.makeEleUploadInput(hiRxno, practitionerId, checkDate, tenantId);
        ElepUploadOutput euResult = ybEleHttpService.uploadElePre(eleUploadInput);

        // 入参和出参都保存
        if (eleUploadInput != null) {
            ybEleBaseService.saveEleUploadInput(eleUploadInput);
        }
        // 入参和出参都保存
        if (euResult != null) {
            ybEleBaseService.saveEleUploadOutput(euResult);
        }
        return R.ok(euResult);
    }

    /**
     * 电子处方撤销
     *
     * @param hiRxno         医保处方编号
     * @param practitionerId 撤销医师Id
     * @param description    撤销原因
     * @param revokeDate     撤销时间
     * @param tenantId       租户Id
     * @return
     */
    @GetMapping("/pre-revoke")
    public R<?> revokePrescription(String hiRxno, Long practitionerId, String description,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date revokeDate, Integer tenantId) {

        ElepRevokeInput eleRevokeInput =
                ybEleBaseService.makeEleRevokeInput(hiRxno, practitionerId, description, revokeDate, tenantId);  // 入参和出参都保存
        ElepRevokeOutput ereResult = ybEleHttpService.revokePre(eleRevokeInput);
        if (eleRevokeInput != null) {
            ybEleBaseService.saveEleRevokeInput(eleRevokeInput);
        }
        // 入参和出参都保存
        if (ereResult != null) {
            ybEleBaseService.saveEleRevokeOutput(ereResult);
        }
        return R.ok(ereResult);
    }

    /**
     * 电子处方信息查询
     *
     * @param hiRxno 医保处方编号
     * @return
     */
    @GetMapping("/pre-query")
    public R<?> queryPrescription(String hiRxno) {

        ElepQuerPrescriptionInput eleQueryPreResultInput = ybEleBaseService.makeEleQueryPrescriptionInput(hiRxno);
        QueryPrescription emrResult = ybEleHttpService.queryPre(eleQueryPreResultInput);
        // 入参和出参都保存
        if (eleQueryPreResultInput != null) {
            ybEleBaseService.saveEleQueryPrescriptionInput(eleQueryPreResultInput);
        }
        // 入参和出参都保存
        if (emrResult != null) {
            ybEleBaseService.saveEleMedResultOut(emrResult);
        }
        return R.ok(emrResult);
    }

    /**
     * 电子处方取药结果查询
     *
     * @param hiRxno 医保处方编号
     * @return
     */
    @GetMapping("/med-query")
    public R<?> queryMedPrescription(String hiRxno) {

        ElepMedresultInput eleMedInput = ybEleBaseService.makeEleMedResultInput(hiRxno);  // 入参和出参都保存
        MedicationResultInquiry medResult = ybEleHttpService.queryMedPre(eleMedInput);
        if (eleMedInput != null) {
            ybEleBaseService.saveEleMedResultInput(eleMedInput);
        }       // 入参和出参都保存
        if (medResult != null) {
            ybEleBaseService.saveMedicationResultInquiry(medResult);
        }
        return R.ok(medResult);
    }
}
