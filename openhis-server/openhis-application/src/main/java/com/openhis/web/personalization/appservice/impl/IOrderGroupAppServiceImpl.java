package com.openhis.web.personalization.appservice.impl;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.OrderGroupType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.template.domain.OrderGroup;
import com.openhis.template.mapper.OrderGroupMapper;
import com.openhis.template.service.OrderGroupService;
import com.openhis.web.personalization.appservice.IOrderGroupAppService;
import com.openhis.web.personalization.dto.OrderGroupDto;
import com.openhis.web.personalization.dto.OrderGroupInitDto;
import com.openhis.web.personalization.mapper.OrderGroupAppMapper;

/**
 * 组套 实现类
 *
 * @author yangmo
 * @date 2025-04-10
 */
@Service
public class IOrderGroupAppServiceImpl implements IOrderGroupAppService {

    @Resource
    private OrderGroupAppMapper orderGroupAppMapper;

    @Resource
    private OrderGroupMapper orderGroupMapper;

    @Resource
    private OrderGroupService orderGroupService;

    /**
     * 组套页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {
        OrderGroupInitDto initDto = new OrderGroupInitDto();
        // 状态
        List<OrderGroupInitDto.typeOption> typeOptions = Stream.of(OrderGroupType.values())
            .map(
                orderGroupType -> new OrderGroupInitDto.typeOption(orderGroupType.getValue(), orderGroupType.getInfo()))
            .collect(Collectors.toList());
        initDto.setTypeOptions(typeOptions);
        return R.ok(initDto);
    }

    /**
     * 查询组套信息
     *
     * @return 组套信息
     */
    @Override
    public R<?> getOrderGroup(OrderGroupDto orderGroupDto, String searchKey) {
        // 构建查询条件
        QueryWrapper<OrderGroupDto> queryWrapper = HisQueryUtils.buildQueryWrapper(orderGroupDto, searchKey,
            new HashSet<>(List.of(CommonConstants.FieldName.Name)), null);

        // 分页查询
        Page<OrderGroupDto> orderGroupPage = orderGroupAppMapper.selectOrderGroup(new Page<>(1, 50), queryWrapper);
        orderGroupPage.getRecords().forEach(e -> {
            // 组套类型
            e.setTypeEnum_enumText(EnumUtils.getInfoByValue(OrderGroupType.class, e.getTypeEnum()));
        });
        return R.ok(orderGroupPage);
    }

    /**
     * 新增组套信息
     *
     * @param orderGroupDto 组套信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrderGroup(OrderGroupDto orderGroupDto) {

        boolean exists = orderGroupMapper
            .exists(new LambdaQueryWrapper<OrderGroup>().eq(OrderGroup::getName, orderGroupDto.getName())
                .eq(OrderGroup::getRangeCode, orderGroupDto.getRangeCode()));
        if (exists) {
            return R.fail("该名称已存在");
        }

        // 组套
        OrderGroup orderGroup = new OrderGroup();
        // 新增组套信息
        orderGroup.setName(orderGroupDto.getName()).setTypeEnum(orderGroupDto.getTypeEnum())
            .setRangeCode(orderGroupDto.getRangeCode())
            .setPractitionerId(SecurityUtils.getLoginUser().getPractitionerId())
            .setOrgId(SecurityUtils.getLoginUser().getOrgId()).setGroupJson(orderGroupDto.getGroupJson())
            .setVersionNo(orderGroupDto.getVersionNo());
        boolean result = orderGroupService.save(orderGroup);
        if (result) {
            return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00002, null));
        }
        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }

    /**
     * 编辑组套信息
     *
     * @param orderGroupDto 组套信息
     * @return 操作结果
     */
    @Override
    public R<?> editOrderGroup(OrderGroupDto orderGroupDto) {
        boolean exists = orderGroupMapper
            .exists(new LambdaQueryWrapper<OrderGroup>().eq(OrderGroup::getName, orderGroupDto.getName())
                .eq(OrderGroup::getRangeCode, orderGroupDto.getRangeCode()).ne(OrderGroup::getId, orderGroupDto));
        if (exists) {
            return R.fail("该名称已存在");
        }
        // 组套
        OrderGroup orderGroup = new OrderGroup();
        // 修改组套信息
        orderGroup.setId(orderGroup.getId()).setName(orderGroupDto.getName()).setTypeEnum(orderGroupDto.getTypeEnum())
            .setRangeCode(orderGroupDto.getRangeCode())
            .setPractitionerId(SecurityUtils.getLoginUser().getPractitionerId())
            .setOrgId(SecurityUtils.getLoginUser().getOrgId()).setGroupJson(orderGroupDto.getGroupJson())
            .setVersionNo(orderGroupDto.getVersionNo());
        boolean result = orderGroupService.saveOrUpdate(orderGroup);
        if (result) {
            return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00002, null));
        }
        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }

    /**
     * 删除组套信息
     *
     * @param id 组套id
     * @return 操作结果
     */
    @Override
    public R<?> deleteOrderGroup(Long id) {
        orderGroupService.removeById(id);
        return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00005, null));
    }
}
