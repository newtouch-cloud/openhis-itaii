/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.template.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.template.domain.OrderGroup;
import com.openhis.template.mapper.OrderGroupMapper;
import com.openhis.template.service.OrderGroupService;

/**
 * 组套Service业务层处理
 *
 * @author yangmo
 * @date 2025-04-10
 */
@Service
public class OrderGroupServiceImpl extends ServiceImpl<OrderGroupMapper, OrderGroup> implements OrderGroupService {}
