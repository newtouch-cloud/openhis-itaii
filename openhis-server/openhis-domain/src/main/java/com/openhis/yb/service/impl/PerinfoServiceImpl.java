package com.openhis.yb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.constant.YbCommonConstants;
import com.openhis.yb.domain.InfoPerson;
import com.openhis.yb.service.IPerinfoService;
import com.openhis.yb.mapper.PerinfoMapper;

import org.springframework.stereotype.Service;

/**
 * @Description: 人员基础信息
 * @Author: jeecg-boot
 * @Date:   2021-08-27
 * @Version: V1.0
 */
@Service
public class PerinfoServiceImpl extends ServiceImpl<PerinfoMapper, InfoPerson> implements IPerinfoService {
    @Override
    public InfoPerson getPerInfoByIdCard(String idCard,Integer tenantId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<InfoPerson>()
                .eq(InfoPerson::getCertno, idCard).eq(InfoPerson::getTenantId, tenantId)
                .orderByDesc(InfoPerson::getCreateTime).last(YbCommonConstants.sqlConst.LIMIT1));
    }
}
