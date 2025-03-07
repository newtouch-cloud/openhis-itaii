package com.openhis.web.outpatientservice.appservice;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.basicservice.dto.HealthcareServiceDto;
import com.openhis.web.outpatientservice.dto.*;

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
     * 查询诊断信息
     * 
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断信息
     */
    Page<ConditionDefinitionMetadata> getConditionDefinitionMetadataSearchKey(String searchKey, Integer pageNo,
        Integer pageSize);

    /**
     * 根据位置id筛选医生
     * 
     * @param locationId 位置ID
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 筛选医生
     */
    IPage<PractitionerMetadata> getPractitionerMetadataByLocationId(Long locationId, String searchKey, Integer pageNo,
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
     * 保存挂号
     * 
     * @param outpatientRegistrationAddParam 就诊表单信息
     * @return 结果
     */
    R<?> saveRegister(OutpatientRegistrationAddParam outpatientRegistrationAddParam);

}
