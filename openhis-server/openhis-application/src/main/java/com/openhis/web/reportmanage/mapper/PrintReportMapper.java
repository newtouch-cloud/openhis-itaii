package com.openhis.web.reportmanage.mapper;

import com.openhis.web.reportmanage.dto.BottleLabelDto;
import com.openhis.web.reportmanage.dto.CkInspAppDto;
import com.openhis.web.reportmanage.dto.DisposalDto;
import com.openhis.web.reportmanage.dto.PrescriptionPrintDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处方和医嘱报告打印 mapper
 *
 * @author liuhr
 * @date 2025-05-07
 */
@Repository
public interface PrintReportMapper {

    /**
     * 获取处置单
     *
     * @param encounterId 患者就诊Id
     * @return 处置单
     */
    List<DisposalDto> getDisposalList(@Param("encounterId") Long encounterId);

    /**
     * 打印——检验,检查申请单
     *
     * @param encounterId  患者就诊Id
     * @param categoryCode 诊疗类型
     * @return 检验, 检查申请单信息
     */
    List<CkInspAppDto> getCheckInspectionList(@Param("encounterId") Long encounterId,
                                              @Param("categoryCode") String categoryCode);

    /**
     * 获取西药(中成药)处方单
     *
     * @param prescriptionNo 院内处方编号
     * @param encounterId    就诊id
     * @return 西药(中成药)处方单
     */
    List<PrescriptionPrintDto> getPrescriptionList(@Param("prescriptionNo") String prescriptionNo,
                                                   @Param("encounterId") Long encounterId);

    /**
     * 获取输液的瓶签
     *
     * @param serviceId   服务请求id
     * @param encounterId 就诊id
     * @param groupId     药品组id
     * @return 输液的瓶签列表
     */
    List<BottleLabelDto> getBottleLabelList(@Param("serviceId") Long serviceId, @Param("encounterId") Long encounterId,
                                            @Param("groupId") Long groupId);

    /**
     * 获取输液的瓶签
     *
     * @param serviceIds   服务请求id集合
     * @param encounterIds 就诊id集合
     * @return 输液的瓶签列表
     */
    List<BottleLabelDto> getBottleLabelBatchList(@Param("serviceIds") List<Long> serviceIds, @Param("encounterIds") List<Long> encounterIds);
}
