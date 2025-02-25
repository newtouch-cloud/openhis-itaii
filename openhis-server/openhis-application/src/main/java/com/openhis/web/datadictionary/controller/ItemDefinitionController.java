/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.enums.AssignSeqEnum;
import com.core.common.enums.ChargeItemEnum;
import com.core.common.enums.DefinitionTypeEnum;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.ChargeItemDefApp;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.service.IChargeItemDefAppService;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.datadictionary.dto.ChargeItemDefPageDto;
import com.openhis.web.datadictionary.dto.ChargeItemOptionDto;
import com.openhis.web.datadictionary.dto.ItemDefSearchParam;
import com.openhis.web.datadictionary.dto.ItemDefinitionDto;
import com.openhis.web.datadictionary.mapper.ChargeItemDefSearchMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 项目定价
 *
 * @author zxy
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/dict-manager/definition")
@Slf4j
public class ItemDefinitionController {

    @Autowired(required = false)
    private IChargeItemDefinitionService chargeItemDefinitionService;
    @Autowired(required = false)
    private IChargeItemDefAppService chargeItemDefAppService;
    @Autowired(required = false)
    private ChargeItemDefSearchMapper chargeItemDefSearchMapper;
    @Autowired(required = false)
    private AssignSeqUtil assignSeqUtil;

    /**
     * 项目定价列表
     *
     * @param itemDefSearchParam 查询条件
     * @return 项目定价列表
     */
    @GetMapping(value = "/init")
    public R<?> getInitDefinitionOptions(ItemDefSearchParam itemDefSearchParam) {
        List<ChargeItemOptionDto> chargeItemOptions = new ArrayList<>();
        if (DefinitionTypeEnum.MEDICATION.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 西药
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.WEST_MEDICINE.getCode())
                .setLabel(ChargeItemEnum.WEST_MEDICINE.getInfo()));
            // 中药饮片
            chargeItemOptions
                .add(new ChargeItemOptionDto().setValue(ChargeItemEnum.CHINESE_MEDICINE_SLICES_FEE.getCode())
                    .setLabel(ChargeItemEnum.CHINESE_MEDICINE_SLICES_FEE.getInfo()));
            // 中成药
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.CHINESE_MEDICINE_FEE.getCode())
                .setLabel(ChargeItemEnum.CHINESE_MEDICINE_FEE.getInfo()));
            // 其他
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.OTHER_FEE.getCode())
                .setLabel(ChargeItemEnum.OTHER_FEE.getInfo()));
        } else if (DefinitionTypeEnum.DEVICE.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 卫生材料
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.SANITARY_MATERIALS_FEE.getCode())
                .setLabel(ChargeItemEnum.SANITARY_MATERIALS_FEE.getInfo()));
            // 其他
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.OTHER_FEE.getCode())
                .setLabel(ChargeItemEnum.OTHER_FEE.getInfo()));
        } else if (DefinitionTypeEnum.ACTIVITY.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 床位
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.BED_FEE.getCode())
                .setLabel(ChargeItemEnum.BED_FEE.getInfo()));
            // 诊察
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.DIAGNOSTIC_FEE.getCode())
                .setLabel(ChargeItemEnum.DIAGNOSTIC_FEE.getInfo()));
            // 检查
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.CHECK_FEE.getCode())
                .setLabel(ChargeItemEnum.CHECK_FEE.getInfo()));
            // 化验
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.DIAGNOSTIC_TEST_FEE.getCode())
                .setLabel(ChargeItemEnum.DIAGNOSTIC_TEST_FEE.getInfo()));
            // 治疗
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.MEDICAL_EXPENSE_FEE.getCode())
                .setLabel(ChargeItemEnum.MEDICAL_EXPENSE_FEE.getInfo()));
            // 手术
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.OPERATION_FEE.getCode())
                .setLabel(ChargeItemEnum.OPERATION_FEE.getInfo()));
            // 护理费
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.NURSING_FEE.getCode())
                .setLabel(ChargeItemEnum.NURSING_FEE.getInfo()));
            // 其他
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.GENERAL_CONSULTATION_FEE.getCode())
                .setLabel(ChargeItemEnum.GENERAL_CONSULTATION_FEE.getInfo()));
            // 挂号
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.REGISTRATION_FEE.getCode())
                .setLabel(ChargeItemEnum.REGISTRATION_FEE.getInfo()));
            // 其他
            chargeItemOptions.add(new ChargeItemOptionDto().setValue(ChargeItemEnum.OTHER_FEE.getCode())
                .setLabel(ChargeItemEnum.OTHER_FEE.getInfo()));
        }
        return R.ok(chargeItemOptions);
    }

    /**
     * 项目定价列表
     *
     * @param itemDefSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 项目定价列表
     */
    @GetMapping(value = "/item-definition-page")
    public R<?> getDefinitionPage(ItemDefSearchParam itemDefSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(value = "searchKey", required = false) String searchKey, HttpServletRequest request) {

        IPage<ChargeItemDefPageDto> chargeItemDefinitionPage = new Page<>();
        List<ChargeItemDefPageDto> chargeItemDefinitionList;

        // 跳过的数量
        int skipCount = 0;
        if (pageNo > 0) {
            skipCount = (pageNo - 1) * pageSize;
        }
        // 通过 DefinitionType 区分药品定价/器具定价/手术定价
        if (DefinitionTypeEnum.MEDICATION.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 获取定价列表
            chargeItemDefinitionList =
                chargeItemDefSearchMapper.getMedList(itemDefSearchParam, pageNo, pageSize, searchKey, skipCount);
        } else if (DefinitionTypeEnum.DEVICE.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 获取定价列表
            chargeItemDefinitionList =
                chargeItemDefSearchMapper.getDevList(itemDefSearchParam, pageNo, pageSize, searchKey, skipCount);
        } else if (DefinitionTypeEnum.ACTIVITY.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 获取定价列表
            chargeItemDefinitionList =
                chargeItemDefSearchMapper.getActList(itemDefSearchParam, pageNo, pageSize, searchKey, skipCount);
        } else {
            chargeItemDefinitionList = new ArrayList<>();
        }
        // 设置分页条件
        chargeItemDefinitionPage.setSize(pageSize);
        chargeItemDefinitionPage.setCurrent(pageNo);
        if (chargeItemDefinitionList.size() > 0) {
            chargeItemDefinitionPage.setTotal(chargeItemDefinitionList.get(0).getTotalCount());
            chargeItemDefinitionPage.setRecords(chargeItemDefinitionList);
        } else {
            chargeItemDefinitionPage.setTotal(0);
            chargeItemDefinitionPage.setRecords(new ArrayList<>());
        }
        return R.ok(chargeItemDefinitionPage, MessageUtils.createMessage(PromptMsgConstant.Common.M00009, null));
    }

    /**
     * 修改项目定价
     *
     * @param itemDefinitionDto 修改内容
     * @return 修改结果
     */
    @PutMapping(value = "/item-definition")
    public R<?> edit(@Validated @RequestBody ItemDefinitionDto itemDefinitionDto) {
        // 更新adm_charge_item_definition信息
        ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
        BeanUtils.copyProperties(itemDefinitionDto, chargeItemDefinition);
        if (!chargeItemDefinitionService.updateById(chargeItemDefinition)) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 更新收费项目adm_charge_item_def_app
        ChargeItemDefApp chargeItemDefApp = new ChargeItemDefApp();
        BeanUtils.copyProperties(itemDefinitionDto, chargeItemDefApp);
        chargeItemDefApp.setDefinitionId(itemDefinitionDto.getId());
        chargeItemDefApp.setId(itemDefinitionDto.getItemId());
        return chargeItemDefAppService.updateChargeItemDefApp(chargeItemDefApp)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"费用定价"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 采番测试（例子，非常规代码，请勿调用）
     *
     * @return 采番测试结果
     */
    @GetMapping(value = "/test-assign")
    public R<?> getTestAssign() {
        // 基础采番
        String code = assignSeqUtil.getSeq(AssignSeqEnum.TEST.getPrefix());
        // 控制长度采番(seqLength: 总长度)
        String code1 = assignSeqUtil.getSeq(AssignSeqEnum.TEST.getPrefix(), 8);
        // 控制长度批量采番
        List<String> code2 = assignSeqUtil.getSeq(AssignSeqEnum.TEST.getPrefix(), 8, 3);
        // 获取编号
        Integer code3 = assignSeqUtil.getSeqNo(AssignSeqEnum.TEST.getPrefix());
        // 批量获取编号
        List<Integer> code4 = assignSeqUtil.getSeqNo(AssignSeqEnum.TEST.getPrefix(), 3);
        // 每日采番
        String code5 = assignSeqUtil.getSeqByDay(AssignSeqEnum.TEST.getPrefix());
        // 每日按长度采番(seqLength: 日期后的数字位数)
        String code6 = assignSeqUtil.getSeqByDay(AssignSeqEnum.TEST.getPrefix(), 8);
        // 每日批量采番
        List<String> code7 = assignSeqUtil.getSeqByDay(AssignSeqEnum.TEST.getPrefix(), 8, 3);
        // 每日获取编号
        Integer code8 = assignSeqUtil.getSeqNoByDay(AssignSeqEnum.TEST.getPrefix());
        // 每日批量获取编号
        List<Integer> code9 = assignSeqUtil.getSeqNoByDay(AssignSeqEnum.TEST.getPrefix(), 3);
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("code1", code1);
        map.put("code2", code2);
        map.put("code3", code3);
        map.put("code4", code4);
        map.put("code5", code5);
        map.put("code6", code6);
        map.put("code7", code7);
        map.put("code8", code8);
        map.put("code9", code9);
        return R.ok(map);
    }
}
