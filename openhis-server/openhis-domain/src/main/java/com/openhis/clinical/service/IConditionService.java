package com.openhis.clinical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.clinical.domain.Condition;

/**
 * 疾病与诊断管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IConditionService extends IService<Condition> {

    /**
     * 医生门诊就诊时保存诊断
     * 
     * @param condition 诊断信息
     * @return 主键ID
     */
    Long saveConditionByDoctor(Condition condition);

}