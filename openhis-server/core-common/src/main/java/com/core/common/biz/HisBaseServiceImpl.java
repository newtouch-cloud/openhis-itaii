package com.core.common.biz;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public class HisBaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements HisBaseService<T> {

    @Override
    public boolean logicalDelById(Long id) {
        return ((HisBaseMapper<T>) baseMapper).logicalDelById(id) > 0;
    }

    @Override
    public boolean logicalDelByIds(List<Long> ids) {
        return ((HisBaseMapper<T>) baseMapper).logicalDelByIds(ids) > 0;
    }

    @Override
    public boolean logicalDelByWrapper(LambdaUpdateWrapper<T> updateWrapper) {
        return ((HisBaseMapper<T>) baseMapper).logicalDelByWrapper(updateWrapper) > 0;
    }
}
