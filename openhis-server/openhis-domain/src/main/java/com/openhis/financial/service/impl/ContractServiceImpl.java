package com.openhis.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.openhis.common.constant.YbCommonConstants;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.mapper.ContractMapper;
import com.openhis.financial.service.IContractService;

/**
 * 合同管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {
    /**
     *
     * @param contractNo 医保码
     * @return
     */
    @Override
    public Contract getByContractNo(String contractNo) {
        return baseMapper.selectOne(new LambdaUpdateWrapper<Contract>().eq(Contract::getBusNo,contractNo).last(YbCommonConstants.sqlConst.LIMIT1));
    }
}