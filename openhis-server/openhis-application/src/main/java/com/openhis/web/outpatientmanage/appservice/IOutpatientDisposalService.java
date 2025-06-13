package com.openhis.web.outpatientmanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.outpatientmanage.dto.OutpatientDisposalActivityInfoDto;
import com.openhis.web.outpatientmanage.dto.OutpatientDisposalExecuteInfoDto;
import com.openhis.web.pharmacymanage.dto.EncounterInfoSearchParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 门诊处置
 *
 * @author yuxj
 * @date 2025/4/10
 */
public interface IOutpatientDisposalService {

    /**
     * 获取门诊处置初期数据列表
     *
     * @return 获取门诊处置初期数据列表
     */
    R<?> init();

    /**
     * 分页查询就诊病人列表
     *
     * @param encounterInfoSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 就诊病人分页列表
     */
    R<?> getEncounterInfoListPage(EncounterInfoSearchParam encounterInfoSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request);
    /**
     * 查询诊疗单列表
     *
     * @param encounterId 就诊号
     * @return 诊疗单列表
     */
    R<?> getDisposalInfoList(Long encounterId);

    /**
     * 查询执行列表
     *
     * @param busNo 编码
     * @param activityId 诊疗Id
     * @param type 类型
     * @return 执行列表
     */
    R<?> getExecuteInfoList(String busNo, Long activityId, Integer type);

    /**
     * 执行
     *
     * @param itemId 诊疗id
     * @param itemId 诊疗id
     * @return 执行结果
     */
    R<?> execute(Long itemId, Integer type);

    /**
     * 取消
     *
     * @param busNo 编码
     * @param activityId 诊疗Id
     * @param type 类型
     * @return 执行结果
     */
    R<?> cancel(String busNo, Long activityId, Integer type);
}
