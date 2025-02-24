package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.ChargeItemDefApp;

/**
 * 费用定价管理子Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IChargeItemDefAppService extends IService<ChargeItemDefApp> {

    /**
     * 更新项目定价
     * 
     * @param chargeItemDefApp 更新内容
     * @return 更新结果
     */
    boolean updateChargeItemDefApp(ChargeItemDefApp chargeItemDefApp);

    /**
     * 新增费用定价
     *
     * @param chargeItemDefApp 新增内容
     * @return 新增结果
     */
    boolean addChargeItemDefApp(ChargeItemDefApp chargeItemDefApp);

    /**
     * 删除费用定价
     *
     * @param id 费用定价id
     * @return 新增结果
     */
    boolean deleteChargeItemDefApp(Long id);
}