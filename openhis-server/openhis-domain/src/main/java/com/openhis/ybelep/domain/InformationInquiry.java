package com.openhis.ybelep.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InformationInquiry {

    // 处方信息
    private ElepQuerPrescriptionInfo prescriptionInfo;

    // 处方明细信息
    private List<ElepQuerPrescriptionDetail> rxDetlList;

    // 就诊信息
    private List<ElepQuerVisitInfo> rxOtpinfo;

    // 诊断信息
    private List<ElepQuerDiagnosisInfo> rxDiseList;

}