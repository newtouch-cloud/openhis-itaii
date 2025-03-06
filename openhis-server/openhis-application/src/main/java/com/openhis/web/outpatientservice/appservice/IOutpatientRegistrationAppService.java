package com.openhis.web.outpatientservice.appservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientservice.dto.ConditionDefinitionMetadata;
import com.openhis.web.outpatientservice.dto.ContractMetadata;
import com.openhis.web.outpatientservice.dto.PatientMetadata;

import java.util.List;

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

}
