package com.core.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.core.domain.R;
import com.core.common.enums.TenantOptionDict;
import com.core.common.utils.StringUtils;
import com.core.system.domain.SysTenantOption;
import com.core.system.domain.dto.SysTenantOptionDto;
import com.core.system.mapper.SysTenantOptionMapper;
import com.core.system.service.ISysTenantOptionService;

/**
 * 租户配置项表Service业务层处理
 * 
 * @author system
 */
@Service
public class SysTenantOptionServiceImpl extends ServiceImpl<SysTenantOptionMapper, SysTenantOption>
    implements ISysTenantOptionService {

    /**
     * 查询租户配置项分页列表
     *
     * @param tenantId 租户ID查询
     * @param optionCode 配置项编码查询
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 租户配置项分页列表
     */
    @Override
    public R<IPage<SysTenantOption>> getTenantOptionPage(Integer tenantId, String optionCode, Integer pageNum,
        Integer pageSize) {
        LambdaQueryWrapper<SysTenantOption> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 指定租户ID
        if (tenantId != null) {
            lambdaQueryWrapper.eq(SysTenantOption::getTenantId, tenantId);
        }
        // 指定配置项编码
        if (StringUtils.isNotEmpty(optionCode)) {
            lambdaQueryWrapper.eq(SysTenantOption::getOptionCode, optionCode);
        }
        IPage<SysTenantOption> page = baseMapper.selectPage(new Page<>(pageNum, pageSize), lambdaQueryWrapper);
        // 获取配置项名称
        for (SysTenantOption record : page.getRecords()) {
            TenantOptionDict tenantOptionDict = TenantOptionDict.getByCode(record.getOptionCode());
            if (tenantOptionDict != null) {
                record.setOptionName(tenantOptionDict.getName());
            }
        }
        return R.ok(page);
    }

    /**
     * 新增租户配置项
     *
     * @param sysTenantOption 租户配置项实体
     * @return 结果
     */
    @Override
    public R<?> addTenantOption(SysTenantOption sysTenantOption) {
        List<SysTenantOption> existOptionCodeList = baseMapper.selectList(
            new LambdaQueryWrapper<SysTenantOption>().eq(SysTenantOption::getTenantId, sysTenantOption.getTenantId())
                .eq(SysTenantOption::getOptionCode, sysTenantOption.getOptionCode()));
        if (!existOptionCodeList.isEmpty()) {
            return R.fail("当前租户已存在该配置项");
        }
        baseMapper.insert(sysTenantOption);
        return R.ok();
    }

    /**
     * 查询租户配置项下拉列表
     *
     * @return 租户配置项下拉列表
     */
    @Override
    public List<SysTenantOptionDto> getTenantOptionDropdown() {
        List<SysTenantOptionDto> optionDtoList = new ArrayList<>();
        SysTenantOptionDto optionDto;
        // 遍历租户配置项字典枚举类
        for (TenantOptionDict dict : TenantOptionDict.values()) {
            optionDto = new SysTenantOptionDto();
            optionDto.setCode(dict.getCode());
            optionDto.setName(dict.getName());
            optionDtoList.add(optionDto);
        }
        return optionDtoList;
    }

    /**
     * 查询全部租户配置项
     *
     * @param tenantId 租户ID
     * @return 全部租户配置项
     */
    @Override
    public List<SysTenantOptionDto> getAllTenantOption(Integer tenantId) {
        List<SysTenantOption> sysTenantOptionList =
            baseMapper.selectList(new LambdaQueryWrapper<SysTenantOption>().eq(SysTenantOption::getTenantId, tenantId));
        List<SysTenantOptionDto> optionDtoList = new ArrayList<>();
        SysTenantOptionDto optionDto;
        // 遍历该租户的所有配置
        for (SysTenantOption option : sysTenantOptionList) {
            optionDto = new SysTenantOptionDto();
            // 获取配置项名称
            TenantOptionDict tenantOptionDict = TenantOptionDict.getByCode(option.getOptionCode());
            if (tenantOptionDict != null) {
                optionDto.setName(tenantOptionDict.getName());
            }
            optionDto.setCode(option.getOptionCode());
            optionDto.setContent(option.getOptionContent());
            optionDtoList.add(optionDto);
        }
        return optionDtoList;
    }

    /**
     * 查询指定的租户配置项内容
     *
     * @param tenantId 租户ID
     * @param tenantOptionDict 配置项字典枚举
     * @return 指定的租户配置项内容（不存在返回NULL）
     */
    @Override
    public SysTenantOptionDto getTenantOptionContent(Integer tenantId, TenantOptionDict tenantOptionDict) {
        List<SysTenantOption> sysTenantOptionList = baseMapper.selectList(new LambdaQueryWrapper<SysTenantOption>()
            .eq(SysTenantOption::getTenantId, tenantId).eq(SysTenantOption::getOptionCode, tenantOptionDict.getCode()));
        if (sysTenantOptionList.isEmpty()) {
            return null;
        }
        SysTenantOption sysTenantOption = sysTenantOptionList.get(0);
        return new SysTenantOptionDto().setCode(sysTenantOption.getOptionCode())
            .setName(sysTenantOption.getOptionName()).setContent(sysTenantOption.getOptionContent());
    }

}
