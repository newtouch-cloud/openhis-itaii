package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.mapper.EncounterMapper;
import com.openhis.administration.service.IEncounterService;

/**
 * 就诊管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class EncounterServiceImpl extends ServiceImpl<EncounterMapper, Encounter> implements IEncounterService {

    /**
     * 保存就诊信息
     *
     * @param encounter 就诊信息
     * @return 保存后的信息
     */
    @Override
    public Long saveEncounter(Encounter encounter) {
        // 生产就诊编码

        // 生产就诊序号
        baseMapper.insert(encounter);
        return encounter.getId();
    }

}