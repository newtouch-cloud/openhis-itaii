package com.openhis.web.doctorstation.appservice.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.clinical.domain.DiagnosisBelongBinding;
import com.openhis.clinical.service.IDiagnosisBelongBindingService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.doctorstation.appservice.IDoctorStationDiagnosisAppService;
import com.openhis.web.doctorstation.dto.DiagnosisBelongBindingDto;
import com.openhis.web.doctorstation.mapper.DoctorStationDiagnosisAppMapper;

/**
 * 医生站-诊断 应用实现类
 */
@Service
public class DoctorStationDiagnosisAppServiceImpl implements IDoctorStationDiagnosisAppService {

    @Resource
    IDiagnosisBelongBindingService iDiagnosisBelongBindingService;

    @Resource
    DoctorStationDiagnosisAppMapper doctorStationDiagnosisAppMapper;

    /**
     * 新增诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    @Override
    public R<?> addDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto) {
        DiagnosisBelongBinding diagnosisBelongBinding = new DiagnosisBelongBinding();
        BeanUtils.copyProperties(diagnosisBelongBindingDto, diagnosisBelongBinding);
        // 校验是否重复新增
        long count = iDiagnosisBelongBindingService.count(new LambdaQueryWrapper<DiagnosisBelongBinding>()
            .eq(DiagnosisBelongBinding::getObjectId, diagnosisBelongBindingDto.getObjectId())
            .eq(DiagnosisBelongBinding::getDefinitionId, diagnosisBelongBindingDto.getDefinitionId())
            .eq(DiagnosisBelongBinding::getBindingEnum, diagnosisBelongBindingDto.getBindingEnum()));
        if (count > 0L) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"绑定关系"}));
        }
        iDiagnosisBelongBindingService.save(diagnosisBelongBinding);
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"诊断归属绑定关系"}));
    }

    /**
     * 编辑诊断归属绑定
     *
     * @param diagnosisBelongBindingDto 诊断归属绑定
     * @return 结果
     */
    @Override
    public R<?> updateDiagnosisBelongBinding(DiagnosisBelongBindingDto diagnosisBelongBindingDto) {
        DiagnosisBelongBinding diagnosisBelongBinding = new DiagnosisBelongBinding();
        BeanUtils.copyProperties(diagnosisBelongBindingDto, diagnosisBelongBinding);
        // 校验是否重复编辑
        long count = iDiagnosisBelongBindingService.count(new LambdaQueryWrapper<DiagnosisBelongBinding>()
            .eq(DiagnosisBelongBinding::getObjectId, diagnosisBelongBindingDto.getObjectId())
            .eq(DiagnosisBelongBinding::getDefinitionId, diagnosisBelongBindingDto.getDefinitionId())
            .eq(DiagnosisBelongBinding::getBindingEnum, diagnosisBelongBindingDto.getBindingEnum())
            .ne(DiagnosisBelongBinding::getId, diagnosisBelongBindingDto.getId()));
        if (count > 0L) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"绑定关系"}));
        }
        iDiagnosisBelongBindingService.updateById(diagnosisBelongBinding);
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊断归属绑定关系"}));
    }

}
