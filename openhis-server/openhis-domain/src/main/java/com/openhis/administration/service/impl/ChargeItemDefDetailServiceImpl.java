package com.openhis.administration.service.impl;

import com.openhis.administration.domain.ChargeItemDefDetail;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.mapper.ChargeItemDefAppMapper;
import com.openhis.administration.service.IChargeItemDefDetailService;

/**
 * 费用定价管理子Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ChargeItemDefDetailServiceImpl extends ServiceImpl<ChargeItemDefAppMapper, ChargeItemDefDetail>
    implements IChargeItemDefDetailService {

    /**
     * 更新项目定价
     *
     * @param chargeItemDefDetail 更新内容
     * @return 更新结果
     */
    @Override
    public boolean updateChargeItemDefApp(ChargeItemDefDetail chargeItemDefDetail) {
        // 更新样例 一切以实际为主
        if (chargeItemDefDetail.getId() != null) {
            // 获取更新前收费项目，避免更新导致数据库崩溃
            if (baseMapper.selectById(chargeItemDefDetail.getId()) == null) {
                return false;
            } else {
                //todo deleteFlag=1
                return baseMapper.updateById(chargeItemDefDetail) > 0;
            }
        } else {
            return false;
        }
    }

    /**
     * 新增费用定价
     *
     * @param chargeItemDefDetail 新增内容
     * @return 新增结果
     */
    @Override
    public boolean addChargeItemDefApp(ChargeItemDefDetail chargeItemDefDetail) {
        // 此判断是为了避免插入时主键重复
        if (chargeItemDefDetail.getId() != null) {
            return false;
        } else {
            return baseMapper.insert(chargeItemDefDetail) > 0;
        }
    }

    /**
     * 删除费用定价
     *
     * @param id 费用定价id
     * @return 新增结果
     */
    @Override
    public boolean deleteChargeItemDefApp(Long id) {
        if (baseMapper.selectById(id) == null) {
            return false;
        } else {
            return baseMapper.deleteById(id) > 0;
        }
    }
}