package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.openhis.yb.domain.ClinicSettle;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.OrgContrast;
import com.openhis.administration.mapper.OrgContrastMapper;
import com.openhis.administration.service.IOrgContrastService;

/**
 * 【对照表】Service业务层处理
 *
 * @author system
 * @date 2025-04-25
 */
@Service
public class OrgContrastServiceImpl extends ServiceImpl<OrgContrastMapper, OrgContrast> implements IOrgContrastService {

    /**
     * 通过字典编码查询
     *
     * @param dictCode 字典编码
     * @param typeEnum 类型
     * @return
     */
    @Override
   public OrgContrast getByDictCode(Long dictCode,Integer typeEnum){

        return baseMapper.selectOne(new LambdaQueryWrapper<OrgContrast>().eq(OrgContrast::getDictCode, dictCode).eq(OrgContrast::getTypeEnum, typeEnum));
    }
}