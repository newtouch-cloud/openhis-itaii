package com.openhis.yb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.domain.ClinicSettle;
import com.openhis.yb.mapper.ClinicSettleMapper;
import com.openhis.yb.service.IClinicSettleService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author system
 * @date 2025-04-20
 */
@Service
public class ClinicSettleServiceImpl extends ServiceImpl<ClinicSettleMapper, ClinicSettle> implements IClinicSettleService {

    /**
     * 通过结算id查询
     *
     * @param paymentId 结算id
     * @return
     */
    @Override
    public  ClinicSettle getByPaymentId(Long paymentId){
        return baseMapper.selectOne(new LambdaQueryWrapper<ClinicSettle>().eq(ClinicSettle::getSetlId, paymentId));
    }


    @Override
    public ClinicSettle getBySettleId(String settleId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<ClinicSettle>().eq(ClinicSettle::getSetlId, settleId));
    }

    @Override
    public List<ClinicSettle> getBySettleIds(List<String> settleIds) {
        return baseMapper.selectList(new LambdaQueryWrapper<ClinicSettle>().in(ClinicSettle::getSetlId, settleIds));
    }
}