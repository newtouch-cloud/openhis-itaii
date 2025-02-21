package com.core.common.biz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface HisBaseService<T> extends IService<T> {

    /**
     * 根据ID逻辑删除单条记录
     *
     * @param id 主键ID
     * @return 是否成功
     */
    boolean logicalDelById(Long id);

    /**
     * 根据ID列表批量逻辑删除记录
     *
     * @param ids 主键ID列表
     * @return 是否成功
     */
    boolean logicalDelByIds(List<Long> ids);

    /**
     * 根据条件逻辑删除记录
     *
     * @param updateWrapper 更新条件（Lambda形式）
     * @return 是否成功
     */
    boolean logicalDelByWrapper(LambdaUpdateWrapper<T> updateWrapper);
}