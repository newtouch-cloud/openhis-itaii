package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.OrgContrast;
import com.openhis.yb.domain.ClinicSettle;

/**
 * 【对照表】Service接口
 *
 * @author system
 * @date 2025-04-25
 */
public interface IOrgContrastService extends IService<OrgContrast> {

    /**
     * 通过字典编码查询
     *
     * @param dictCode 字典编码
     * @param typeEnum 类型
     * @return
     */
    OrgContrast getByDictCode(Long dictCode,Integer typeEnum );
}