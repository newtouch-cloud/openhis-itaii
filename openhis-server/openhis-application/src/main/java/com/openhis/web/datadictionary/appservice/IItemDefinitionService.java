package com.openhis.web.datadictionary.appservice;

import com.openhis.administration.domain.ChargeItemDefDetail;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.web.datadictionary.dto.ItemUpFromDirectoryDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目定价
 *
 * @author liuhr
 * @date 2025/3/25
 */
public interface IItemDefinitionService {

    /**
     * 添加药品/器材/诊疗的项目定价
     *
     * @param itemUpFromDirectoryDto 药品/器材/诊疗目录信息
     */
    boolean addItem(ItemUpFromDirectoryDto itemUpFromDirectoryDto);

    /**
     * 修改项目定价表
     *
     * @param chargeItemDefinition 项目定价表信息
     */
    boolean updateItem(ChargeItemDefinition chargeItemDefinition);

    /**
     * 修改项目定价表子信息
     *
     * @param chargeItemDefinition 项目定价表信息
     * @param price 价格
     * @param conditionCode 条件
     */
    boolean updateItemDetail(ChargeItemDefinition chargeItemDefinition, BigDecimal price,String conditionCode);
}
