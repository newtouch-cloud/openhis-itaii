package com.openhis.medication.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.medication.domain.MedicationDefinition;
import com.openhis.medication.domain.MedicationDetail;
import com.openhis.medication.mapper.MedicationDefinitionMapper;
import com.openhis.medication.service.IMedicationDefinitionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 药品定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class MedicationDefinitionServiceImpl extends ServiceImpl<MedicationDefinitionMapper, MedicationDefinition>
    implements IMedicationDefinitionService {

    private final MedicationDefinitionMapper medicationDefinitionMapper;

    /**
     * 新增药品目录
     * 
     * @param medicationDetail
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMedication(MedicationDetail medicationDetail) {
        MedicationDefinition medicationDefinition = new MedicationDefinition();
        BeanUtils.copyProperties(medicationDetail, medicationDefinition);
        // 根据药品编码判断药品是否存在
        List<MedicationDefinition> medicationDefinitions =
            medicationDefinitionMapper.selectList(new LambdaQueryWrapper<MedicationDefinition>()
                .eq(MedicationDefinition::getBusNo, medicationDefinition.getBusNo()));
        if (medicationDefinitions.size() > 0) {
            return false;
        }
        // 新增药品目录
        int insert = medicationDefinitionMapper.insert(medicationDefinition);
        if (insert != 1) {
            return false;
        }
        // 获取生成的主键值
        Long generatedId = medicationDefinition.getId();
        // 将生成的 ID 存储到子表中
        medicationDetail.setMedicationDefId(generatedId);
        return true;
    }

    @Override
    public boolean addYbMedicatione(MedicationDetail medicationDetail) {
        return false;
    }
}