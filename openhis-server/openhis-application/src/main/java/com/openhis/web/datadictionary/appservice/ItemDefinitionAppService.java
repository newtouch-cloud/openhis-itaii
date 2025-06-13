package com.openhis.web.datadictionary.appservice;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.ItemDefinitionDetailDto;
import com.openhis.web.datadictionary.dto.ItemDefinitionDto;

/**
 * 项目定价 应用Service
 */
public interface ItemDefinitionAppService {

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
    IPage<ItemDefinitionDto> getChargeItemInfo(ItemDefinitionDto itemDefinitionDto, Integer chargeItemContext,
        String searchKey, Integer pageNo, Integer pageSize);

    /**
     * 项目定价详细
     *
     * @param id id
     * @return 项目定价详细
     */
    List<ItemDefinitionDetailDto> getChargeItemInfoDetail(Long id);

    /**
     * 改价
     *
     * @param id id
     * @param price 价格
     * @return 结果
     */
    R<?> updateChargeItemInfo(Long id, BigDecimal price);

}
