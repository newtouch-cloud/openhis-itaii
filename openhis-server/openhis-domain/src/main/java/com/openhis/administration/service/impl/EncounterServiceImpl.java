package com.openhis.administration.service.impl;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.utils.StringUtils;
import com.openhis.administration.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.AssignSeqUtil;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.mapper.EncounterMapper;
import com.openhis.administration.service.IEncounterService;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.common.enums.EncounterStatus;
import com.openhis.common.enums.EncounterType;

/**
 * 就诊管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class EncounterServiceImpl extends ServiceImpl<EncounterMapper, Encounter> implements IEncounterService {

    @Resource
    AssignSeqUtil assignSeqUtil;

    /**
     * 保存就诊信息
     *
     * @param encounter 就诊信息
     * @return 保存后的信息
     */
    @Override
    public Long saveEncounterByRegister(Encounter encounter) {
        if(StringUtils.isEmpty(encounter.getBusNo())){
            // 生成就诊编码  医保挂号时是先生成码后生成实体
            encounter.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.ENCOUNTER_NUM.getPrefix(), 8));
        }
        // 生成就诊序号 (患者ID + 科室ID 作为当日就诊号的唯一标识)
        String preFix = encounter.getPatientId() + String.valueOf(encounter.getOrganizationId());
        encounter.setDisplayOrder(assignSeqUtil.getSeqNoByDay(preFix));
        // 患者ID
        Long patientId = encounter.getPatientId();
        // 初复诊
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Encounter>().eq(Encounter::getPatientId, patientId));
        if (count > 0L) {
            encounter.setFirstEnum(EncounterType.FOLLOW_UP.getValue());
        }
        baseMapper.insert(encounter);
        return encounter.getId();
    }

    /**
     * 退号
     *
     * @param encounterId 就诊id
     */
    @Override
    public void returnRegister(Long encounterId) {
        Encounter encounter = new Encounter();
        encounter.setStatusEnum(EncounterStatus.CANCELLED.getValue());
        encounter.setId(encounterId);
        baseMapper.updateById(encounter);
    }

    /**
     * 更新或者插入就诊管理
     *
     * @param encounter 就诊管理实体
     */
    @Override
    public boolean saveOrUpdateEncounter(Encounter encounter) {

        // 创建 LambdaQueryWrapper
        LambdaQueryWrapper<Encounter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Encounter::getPatientId, encounter.getPatientId())
            .eq(Encounter::getId, encounter.getId());

        // 查询是否存在记录
        Encounter existingEncounter = baseMapper.selectOne(queryWrapper);
        if (existingEncounter != null) {
            // 如果记录存在，更新记录
            encounter.setId(existingEncounter.getId()); // 设置主键
            return baseMapper.updateById(encounter) > 0;
        } else {
            // 使用基础采番，设置住院ID，10位数
            String code = assignSeqUtil.getSeq(AssignSeqEnum.ADMISSION_NUM.getPrefix(), 10);
            encounter.setBusNo(code);
            // 如果记录不存在，插入新记录
            return baseMapper.insert(encounter) > 0;
        }
    }

    /**
     * 通过 id 更新 priorityEnum 字段
     *
     * @param id             Encounter 的 id
     * @param priorityEnum   要更新的 priorityEnum 值
     * @return 更新是否成功
     */
    public boolean updatePriorityEnumById(Long id, Integer priorityEnum) {
        // 创建更新条件
        LambdaUpdateWrapper<Encounter> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Encounter::getId, id)
            .set(Encounter::getPriorityEnum, priorityEnum);

        // 执行更新
        return update(updateWrapper);
    }

}