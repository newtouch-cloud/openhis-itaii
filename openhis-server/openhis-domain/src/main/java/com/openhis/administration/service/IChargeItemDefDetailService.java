package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.ChargeItemDefDetail;

/**
 * 费用定价管理子Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IChargeItemDefDetailService extends IService<ChargeItemDefDetail> {

    /**
     * 更新项目定价
     * 
     * @param chargeItemDefDetail 更新内容
     * @return 更新结果
     */
    boolean updateChargeItemDefApp(ChargeItemDefDetail chargeItemDefDetail);

    /**
     * 新增费用定价
     *
     * @param chargeItemDefDetail 新增内容
     * @return 新增结果
     */
    boolean addChargeItemDefApp(ChargeItemDefDetail chargeItemDefDetail);

    /**
     * 删除费用定价
     *
     * @param id 费用定价id
     * @return 新增结果
     */
    boolean deleteChargeItemDefApp(Long id);
}