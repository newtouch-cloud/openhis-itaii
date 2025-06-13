package com.openhis.web.reportmanage.appservice;

import com.core.common.core.domain.R;

import java.util.List;

/**
 * 院内处方和医嘱报告打印 接口
 *
 * @author liuhr
 * @date 2025/5/6
 */
public interface IPrintReportAppService {

    /**
     * 打印——处置单
     *
     * @param encounterId 患者就诊Id
     * @return 处置单信息
     */
    R<?> disposalPrint(Long encounterId);

    /**
     * 打印——检查申请单
     *
     * @param encounterId 患者就诊Id
     * @return 检查申请单信息
     */
    R<?> checkApplicationPrint(Long encounterId);

    /**
     * 打印——检验申请单
     *
     * @param encounterId 患者就诊Id
     * @return 检验申请单信息
     */
    R<?> inspectionApplicationPrint(Long encounterId);

    /**
     * 打印——处方单
     *
     * @param prescriptionNo 院内处方编号
     * @param encounterId    患者就诊Id
     * @return 处方单信息
     */
    R<?> prescriptionPrint(String prescriptionNo, Long encounterId);

    /**
     * 打印——护士输液的瓶签
     *
     * @param serviceId   服务请求id
     * @param encounterId 就诊id
     * @param groupId     药品组id
     * @return 输液的瓶签
     */
    R<?> bottleLabelPrint(Long serviceId, Long encounterId, Long groupId);

    /**
     * 打印——护士输液的瓶签 批量
     *
     * @return 输液的瓶签
     */
    R<?> bottleLabelBatchPrint(List<Long> serviceIds,
                               List<Long> encounterIds);

    /**
     * 更新打印次数——护士输液的瓶签的打印次数
     *
     * @param serviceId 服务请求id
     * @param num       打印次数
     * @return 更新结果
     */
    R<?> updatePrintCount(Long serviceId, Integer num);

}
