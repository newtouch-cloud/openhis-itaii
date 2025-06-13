package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.openhis.common.enums.EncounterLocationStatus;
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
     * 门诊挂号时保存就诊位置信息
     *
     * @param encounterLocation 就诊位置信息
     */
    @Override
    public void saveEncounterLocationByRegister(EncounterLocation encounterLocation) {
        baseMapper.insert(encounterLocation);
    }

    /**
     * 自定义插入或更新方法,根据就诊id，病区,判断是更新还是插入
     * @param encounterLocation 实体对象
     * @return 是否成功
     */
    public boolean saveOrUpdateEncounterLocation(EncounterLocation encounterLocation) {
        // 创建 LambdaQueryWrapper
        LambdaQueryWrapper<EncounterLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EncounterLocation::getEncounterId, encounterLocation.getEncounterId())
            .eq(EncounterLocation::getFormEnum,encounterLocation.getFormEnum())
            // 状态为使用中
            .eq(EncounterLocation::getStatusEnum, EncounterLocationStatus.ACTIVE.getValue());

        // 查询是否存在记录
        EncounterLocation existingRecord = baseMapper.selectOne(queryWrapper);
        if (existingRecord != null) {
            // 如果记录存在，更新记录
            encounterLocation.setId(existingRecord.getId());
            return baseMapper.updateById(encounterLocation) > 0;
        } else {
            // 如果记录不存在，插入新记录
            return baseMapper.insert(encounterLocation) > 0;
        }
    }

}