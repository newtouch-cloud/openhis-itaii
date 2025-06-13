package com.openhis.sys.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.sys.domain.Option;
import com.openhis.sys.mapper.OptionMapper;
import com.openhis.sys.service.IOptionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统选项配置Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class OptionServiceImpl extends ServiceImpl<OptionMapper, Option> implements IOptionService {

}