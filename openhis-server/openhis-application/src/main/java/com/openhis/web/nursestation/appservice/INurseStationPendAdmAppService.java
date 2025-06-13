package com.openhis.web.nursestation.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.nursestation.dto.BedForAdmissionDto;
import com.openhis.web.nursestation.dto.PendingAdmissionDto;

/**
 * 待入科 应用接口
 *
 * @author liuhr
 * @date 2025/4/14
 */
public interface INurseStationPendAdmAppService {

    /**
     * 待入科的初始化数据
     *
     * @return
     */
    R<?> getPendAdmInit();

    /**
     * 选择科室后，查询科室对应病区列表
     *
     * @return 病区列表
     */
    R<?> getWardLocationList(Long orgId);

    /**
     * 查询待入科的患者列表
     *
     * @return 待入科的患者列表
     */
    R<?> getPendAdmInfo();

    /**
     * 病区的病床信息
     *
     * @param wardLocationId 病区ID
     * @return 病床列表
     */
    R<?> getBedList(Long wardLocationId);

    /**
     * 患者选床入科
     * 
     * @param bedInfoDto 病床信息
     * @return 选床入科
     */
    R<?> selectBedForAdmission(BedForAdmissionDto bedInfoDto);

    /**
     * 根据入院科室查询医生列表
     *
     * @param orgId 入院科室Id
     * @return 医生列表
     */
    R<?> getDoctorList(Long orgId);

    /**
     * 根据入院科室查询护士列表
     *
     * @param orgId 入院科室Id
     * @return 护士列表
     */
    R<?> getNurseList(Long orgId);
}
