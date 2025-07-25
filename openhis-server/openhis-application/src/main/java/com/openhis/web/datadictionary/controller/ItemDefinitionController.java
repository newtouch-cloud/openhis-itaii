/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.StringUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.ChargeItemDefDetail;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.service.IChargeItemDefDetailService;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.ChargeItemEnum;
import com.openhis.common.enums.ItemType;
import com.openhis.common.enums.PublicationStatus;
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
@RequestMapping("/dict-dictionary/definition")
@Slf4j
public class ItemDefinitionController {

    @Autowired(required = false)
    private IChargeItemDefinitionService chargeItemDefinitionService;
    @Autowired(required = false)
    private IChargeItemDefDetailService chargeItemDefAppService;
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
        /// TODO: 2025/2/26 收费项目下拉框 暂未做成用枚举代替，后续替换
        List<ChargeItemOptionDto> chargeItemOptions = new ArrayList<>();
        if (ItemType.MEDICINE.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 西药
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.WEST_MEDICINE.getCode(),
                ChargeItemEnum.WEST_MEDICINE.getInfo()));
            // 中药饮片
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.CHINESE_MEDICINE_SLICES_FEE.getCode(),
                ChargeItemEnum.CHINESE_MEDICINE_SLICES_FEE.getInfo()));
            // 中成药
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.CHINESE_MEDICINE_FEE.getCode(),
                ChargeItemEnum.CHINESE_MEDICINE_FEE.getInfo()));
            // 其他
            chargeItemOptions
                .add(new ChargeItemOptionDto(ChargeItemEnum.OTHER_FEE.getCode(), ChargeItemEnum.OTHER_FEE.getInfo()));
        } else if (ItemType.DEVICE.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 卫生材料
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.SANITARY_MATERIALS_FEE.getCode(),
                ChargeItemEnum.SANITARY_MATERIALS_FEE.getInfo()));
            // 其他
            chargeItemOptions
                .add(new ChargeItemOptionDto(ChargeItemEnum.OTHER_FEE.getCode(), ChargeItemEnum.OTHER_FEE.getInfo()));
        } else if (ItemType.ACTIVITY.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            // 床位
            chargeItemOptions
                .add(new ChargeItemOptionDto(ChargeItemEnum.BED_FEE.getCode(), ChargeItemEnum.BED_FEE.getInfo()));
            // 诊察
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.DIAGNOSTIC_FEE.getCode(),
                ChargeItemEnum.DIAGNOSTIC_FEE.getInfo()));
            // 检查
            chargeItemOptions
                .add(new ChargeItemOptionDto(ChargeItemEnum.CHECK_FEE.getCode(), ChargeItemEnum.CHECK_FEE.getInfo()));
            // 化验
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.DIAGNOSTIC_TEST_FEE.getCode(),
                ChargeItemEnum.DIAGNOSTIC_TEST_FEE.getInfo()));
            // 治疗
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.MEDICAL_EXPENSE_FEE.getCode(),
                ChargeItemEnum.MEDICAL_EXPENSE_FEE.getInfo()));
            // 手术
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.OPERATION_FEE.getCode(),
                ChargeItemEnum.OPERATION_FEE.getInfo()));
            // 护理费
            chargeItemOptions.add(
                new ChargeItemOptionDto(ChargeItemEnum.NURSING_FEE.getCode(), ChargeItemEnum.NURSING_FEE.getInfo()));
            // 其他
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.GENERAL_CONSULTATION_FEE.getCode(),
                ChargeItemEnum.GENERAL_CONSULTATION_FEE.getInfo()));
            // 挂号
            chargeItemOptions.add(new ChargeItemOptionDto(ChargeItemEnum.REGISTRATION_FEE.getCode(),
                ChargeItemEnum.REGISTRATION_FEE.getInfo()));
            // 其他
            chargeItemOptions
                .add(new ChargeItemOptionDto(ChargeItemEnum.OTHER_FEE.getCode(), ChargeItemEnum.OTHER_FEE.getInfo()));
        }
        return R.ok(chargeItemOptions);
    }

    /**
     * 项目定价列表
     *
     * @param chargeItemDefPageDto 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 项目定价列表
     */
    @GetMapping(value = "/item-definition-page")
    public R<?> getDefinitionPage(ChargeItemDefPageDto chargeItemDefPageDto,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(value = "searchKey", required = false) String searchKey, HttpServletRequest request) {

        IPage<ChargeItemDefPageDto> chargeItemDefinitionPage = new Page<>();
        // 初始化表格配置并构建查询条件
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""),
            ChargeItemDefPageDto.class);
        LambdaQueryWrapper<ChargeItemDefPageDto> queryWrapper = new LambdaQueryWrapper<>();
        // 构造查询条件
        if (StringUtils.isNotEmpty(searchKey)) {
            queryWrapper.and(q -> q.like(ChargeItemDefPageDto::getChargeName, searchKey).or()
                .like(ChargeItemDefPageDto::getItemNo, searchKey).or().like(ChargeItemDefPageDto::getPyStr, searchKey));
        }
        if (chargeItemDefPageDto.getChargeItem() != null) {
            queryWrapper.eq(ChargeItemDefPageDto::getCategoryCode, chargeItemDefPageDto.getChargeItem());
        }
        // 通过 DefinitionType 区分药品定价/器具定价/活动定价
        if (ItemType.MEDICINE.getCode().equals(chargeItemDefPageDto.getDefinitionType())) {
            queryWrapper.eq(ChargeItemDefPageDto::getInstanceTable,
                CommonConstants.TableName.MED_MEDICATION_DEFINITION);
            chargeItemDefinitionPage = chargeItemDefSearchMapper.getMedList(new Page<>(pageNo, pageSize), queryWrapper);
        } else if (ItemType.DEVICE.getCode().equals(chargeItemDefPageDto.getDefinitionType())) {
            queryWrapper.eq(ChargeItemDefPageDto::getInstanceTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION);
            chargeItemDefinitionPage = chargeItemDefSearchMapper.getDevList(new Page<>(pageNo, pageSize), queryWrapper);
        } else if (ItemType.ACTIVITY.getCode().equals(chargeItemDefPageDto.getDefinitionType())) {
            queryWrapper.eq(ChargeItemDefPageDto::getInstanceTable, CommonConstants.TableName.WOR_ACTIVITY_DEFINITION);
            chargeItemDefinitionPage = chargeItemDefSearchMapper.getActList(new Page<>(pageNo, pageSize), queryWrapper);
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
        ChargeItemDefDetail chargeItemDefDetail = new ChargeItemDefDetail();
        BeanUtils.copyProperties(itemDefinitionDto, chargeItemDefDetail);
        chargeItemDefDetail.setDefinitionId(itemDefinitionDto.getId());
        chargeItemDefDetail.setId(itemDefinitionDto.getItemId());
        return chargeItemDefAppService.updateChargeItemDefApp(chargeItemDefDetail)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"费用定价"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 修改项目定价
     *
     * @return 修改结果
     */
    @GetMapping(value = "/status-enum-option")
    public R<?> getDropdownOption() {
        return R.ok(Arrays.stream(PublicationStatus.values())
            .map(status -> new ChargeItemOptionDto(status.getValue(), status.getInfo())).collect(Collectors.toList()));
    }
}
