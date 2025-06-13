/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.ConditionCode;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.appservice.ItemDefinitionAppService;
import com.openhis.web.datadictionary.dto.ItemDefinitionDetailDto;
import com.openhis.web.datadictionary.dto.ItemDefinitionDto;
import com.openhis.web.datadictionary.mapper.ItemDefinitionAppMapper;

/**
 * 项目定价 应用impl
 */
@Service
public class ItemDefinitionAppServiceImpl implements ItemDefinitionAppService {

    @Resource
    ItemDefinitionAppMapper itemDefinitionAppMapper;

    @Resource
    IChargeItemDefinitionService iChargeItemDefinitionService;

    /**
     * 项目定价 分页
     *
     * @param itemDefinitionDto dto
     * @param searchKey 模糊查询关键字
     * @param chargeItemContext 收费项目类型
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 项目定价
     */
    @Override
    public IPage<ItemDefinitionDto> getChargeItemInfo(ItemDefinitionDto itemDefinitionDto, Integer chargeItemContext,
        String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<ItemDefinitionDto> queryWrapper = HisQueryUtils.buildQueryWrapper(itemDefinitionDto, searchKey,
            new HashSet<>(Arrays.asList("charge_name")), null);
        IPage<ItemDefinitionDto> chargeItemInfo = itemDefinitionAppMapper.getChargeItemInfo(
            new Page<>(pageNo, pageSize), chargeItemContext, CommonConstants.TableName.MED_MEDICATION_DEFINITION,
            CommonConstants.TableName.ADM_DEVICE_DEFINITION, CommonConstants.TableName.WOR_ACTIVITY_DEFINITION,
            CommonConstants.TableName.ADM_HEALTHCARE_SERVICE, queryWrapper);
        List<ItemDefinitionDto> records = chargeItemInfo.getRecords();
        for (ItemDefinitionDto record : records) {
            // 状态
            record.setStatusEnum_enumText(EnumUtils.getInfoByValue(PublicationStatus.class, record.getStatusEnum()));
        }
        return chargeItemInfo;
    }

    /**
     * 项目定价详细
     *
     * @param id id
     * @return 项目定价详细
     */
    @Override
    public List<ItemDefinitionDetailDto> getChargeItemInfoDetail(Long id) {
        List<ItemDefinitionDetailDto> chargeItemInfoDetail = itemDefinitionAppMapper.getChargeItemInfoDetail(id);
        for (ItemDefinitionDetailDto itemDefinitionDetailDto : chargeItemInfoDetail) {
            // 条件
            itemDefinitionDetailDto.setConditionCode_enumText(
                EnumUtils.getInfoByValue(ConditionCode.class, itemDefinitionDetailDto.getConditionCode()));
        }
        return chargeItemInfoDetail;
    }

    /**
     * 改价
     *
     * @param id id
     * @param price 价格
     * @return 结果
     */
    @Override
    public R<?> updateChargeItemInfo(Long id, BigDecimal price) {
        ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
        chargeItemDefinition.setId(id);
        chargeItemDefinition.setPrice(price);
        iChargeItemDefinitionService.updateById(chargeItemDefinition);
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"项目定价"}));
    }

}
