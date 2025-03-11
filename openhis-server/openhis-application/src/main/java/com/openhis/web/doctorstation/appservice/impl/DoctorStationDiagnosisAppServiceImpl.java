package com.openhis.web.doctorstation.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.clinical.domain.DiagnosisBelongBinding;
import com.openhis.clinical.service.IDiagnosisBelongBindingService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.BindingType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
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

    /**
     * 查询诊断归属绑定列表
     *
     * @param diagnosisBelongBindingDto 查询条件dto
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断归属绑定列表
     */
    @Override
    public R<?> getDiagnosisBelongBindingPage(DiagnosisBelongBindingDto diagnosisBelongBindingDto, String searchKey,
        Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<DiagnosisBelongBindingDto> queryWrapper = HisQueryUtils.buildQueryWrapper(
            diagnosisBelongBindingDto, searchKey, new HashSet<>(Arrays.asList("definition_name", "object_name")), null);
        IPage<DiagnosisBelongBindingDto> diagnosisBelongBindingPage =
            doctorStationDiagnosisAppMapper.getDiagnosisBelongBindingPage(new Page<>(pageNo, pageSize),
                BindingType.PERSONAL.getValue(), BindingType.DEFINITION.getValue(), queryWrapper);
        diagnosisBelongBindingPage.getRecords().forEach(e -> {
            // 诊断绑定类型
            e.setBindingEnum_enumText(EnumUtils.getInfoByValue(BindingType.class, e.getBindingEnum()));
        });
        return R.ok(diagnosisBelongBindingPage);
    }

    /**
     * 删除诊断归属绑定
     *
     * @param id ID
     * @return 结果
     */
    @Override
    public R<?> delDiagnosisBelongBinding(Long id) {
        boolean res = iDiagnosisBelongBindingService.removeById(id);
        return res ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"诊断归属绑定"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
    }

}
