package com.openhis.yb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.yb.service.IRegService;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.mapper.RegMapper;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 挂号管理-服务
 */
@Service
public class RegServiceImpl extends ServiceImpl<RegMapper, ClinicReg> implements IRegService {
    @Override
    public void updateStatus(Long id, String status) {
        ClinicReg reg = new ClinicReg();
        reg.setId(id);
        reg.setStatus(status);
        updateById(reg);
    }

    /**
     * 通过单据号查询
     *
     * @param busNo 单据号
     * @return
     */
    @Override
    public ClinicReg getByBusNo(String busNo) {
        return baseMapper.selectOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getIptOtpNo, busNo)
                .orderByDesc(ClinicReg::getCreateTime)
                .last("LIMIT 1"));
    }
}
