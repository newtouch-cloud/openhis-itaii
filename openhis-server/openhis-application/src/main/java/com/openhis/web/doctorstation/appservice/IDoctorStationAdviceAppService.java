package com.openhis.web.doctorstation.appservice;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceSaveParam;
import com.openhis.web.doctorstation.dto.UpdateGroupIdParam;

/**
 * 医生站-医嘱/处方 应用Service
 */
public interface IDoctorStationAdviceAppService {

    /**
     * 查询医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param adviceDefinitionIdParamList 医嘱定义id参数集合
     * @param organizationId 患者挂号对应的科室id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param pricingFlag 划价标记
     * @return 医嘱信息
     */
    IPage<AdviceBaseDto> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        List<Long> adviceDefinitionIdParamList, Long organizationId, Integer pageNo, Integer pageSize,
        Integer pricingFlag);

    /**
     * 门诊保存医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @param adviceOpType 医嘱操作类型
     * @return 结果
     */
    R<?> saveAdvice(AdviceSaveParam adviceSaveParam, String adviceOpType);

    /**
     * 查询医嘱请求数据
     *
     * @param encounterId 就诊id
     * @return 医嘱请求数据
     */
    R<?> getRequestBaseInfo(Long encounterId);

    /**
     * 门诊签退医嘱
     *
     * @param requestIdList 请求id列表
     * @return 结果
     */
    R<?> signOffAdvice(List<Long> requestIdList);

    /**
     * 查询历史医嘱请求数据
     *
     * @param patientId 病人id
     * @param encounterId 就诊id
     * @return 历史医嘱请求数据
     */
    R<?> getRequestHistoryInfo(Long patientId, Long encounterId);

    /**
     * 更新组号
     *
     * @param updateGroupIdParam 更新组号参数
     * @return 结果
     */
    void updateGroupId(UpdateGroupIdParam updateGroupIdParam);

    /**
     * 查询就诊费用性质
     *
     * @param encounterId 就诊id
     * @return 就诊费用性质
     */
    R<?> getEncounterContract(Long encounterId);
}
