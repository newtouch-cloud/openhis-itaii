package com.openhis.web.personalization.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.personalization.dto.OrderGroupDto;

/**
 * 组套 service
 *
 * @author yangmo
 * @date 2025-04-10
 */
public interface IOrderGroupAppService {

    /**
     * 查询组套信息
     *
     * @return 组套信息
     */
    R<?> getOrderGroup(OrderGroupDto orderGroupDto, String searchKey);

    /**
     * 新增组套信息
     *
     * @param orderGroupDto 组套信息
     * @return 操作结果
     */
    R<?> addOrderGroup(OrderGroupDto orderGroupDto);

    /**
     * 编辑组套信息
     *
     * @param orderGroupDto 组套信息
     * @return 操作结果
     */
    R<?> editOrderGroup(OrderGroupDto orderGroupDto);

    /**
     * 删除组套信息
     *
     * @param id 组套id
     * @return 操作结果
     */
    R<?> deleteOrderGroup(Long id);

    /**
     * 组套页面初始化
     *
     * @return 初始化信息
     */
    R<?> init();
}
