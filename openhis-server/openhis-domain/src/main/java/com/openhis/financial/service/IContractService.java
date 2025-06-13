package com.openhis.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.financial.domain.Contract;

/**
 * 合同管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IContractService extends IService<Contract> {

    /**
     * 根据医保码找合同信息
     * @param contractNo 医保码
     * @return 合同实体
     */
    Contract getByContractNo(String contractNo);
}