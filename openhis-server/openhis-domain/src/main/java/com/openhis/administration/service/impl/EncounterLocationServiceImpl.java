package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.EncounterLocation;
import com.openhis.administration.mapper.EncounterLocationMapper;
import com.openhis.administration.service.IEncounterLocationService;

/**
 * 就诊位置管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class EncounterLocationServiceImpl extends ServiceImpl<EncounterLocationMapper, EncounterLocation>
    implements IEncounterLocationService {

    /**
     * 保存就诊位置信息
     *
     * @param encounterLocation 就诊位置信息
     */
    @Override
    public void saveEncounterLocation(EncounterLocation encounterLocation) {
        baseMapper.insert(encounterLocation);
    }

}