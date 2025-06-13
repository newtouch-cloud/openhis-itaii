package com.openhis.web.chargemanage.appservice;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.basicservice.dto.HealthcareServiceDto;
import com.openhis.web.chargemanage.dto.*;
import com.openhis.web.paymentmanage.dto.CancelRegPaymentDto;

import javax.servlet.http.HttpServletRequest;

/**
 * 门诊挂号 应用Service
 */
public interface IOutpatientRegistrationAppService {

    /**
     * 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者信息
     */
    Page<PatientMetadata> getPatientMetadataBySearchKey(String searchKey, Integer pageNo, Integer pageSize);

    /**
     * 查询费用性质
     *
     * @return 费用性质
     */
    List<ContractMetadata> getContractMetadata();

    /**
     * 查询门诊科室数据
     *
     * @return 门诊科室
     */
    List<OrgMetadata> getOrgMetadata();

    /**
     * 根据科室id筛选医生
     *
     * @param orgId 科室ID
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 筛选医生
     */
    IPage<PractitionerMetadata> getPractitionerMetadataByLocationId(Long orgId, String searchKey, Integer pageNo,
        Integer pageSize);

    /**
     * 根据机构id筛选服务项目
     *
     * @param organizationId 机构id
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 服务项目
     */
    IPage<HealthcareServiceDto> getHealthcareMetadataByOrganizationId(Long organizationId, String searchKey,
        Integer pageNo, Integer pageSize);

    /**
     * 退号
     *
     * @param cancelRegPaymentDto 就诊id
     * @return 结果
     */
    R<?> returnRegister(CancelRegPaymentDto cancelRegPaymentDto);

    /**
     * 查询当日就诊数据
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 当日就诊数据
     */
    IPage<CurrentDayEncounterDto> getCurrentDayEncounter(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 取消挂号
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    R<?> cancelRegister(Long encounterId);

}
