package com.openhis.web.outpatientservice.controller.appservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientservice.dto.PatientMetadata;

/**
 * 门诊挂号 Service
 */
public interface IOutpatientRegistrationService {

    /**
     * 门诊挂号 - 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo    当前页
     * @param pageSize  每页多少条
     * @return 患者信息
     */
    Page<PatientMetadata> getPatientMetadataBySearchKey(String searchKey, Integer pageNo, Integer pageSize);

}
