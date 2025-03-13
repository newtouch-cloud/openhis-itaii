package com.openhis.web.basedatamanage.appservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.dto.LocationQueryDto;

/**
 * Location 应该服务类
 */
public interface ILocationAppService {
    /**
     * 位置信息
     *
     * @param formKey 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 位置信息分页列表
     */
    R<?> getLocationTree(Integer formKey, Integer pageNo, Integer pageSize);

    /**
     * 位置信息详情
     *
     * @param locationId 位置信息id
     * @return 位置信息详情
     */
    R<?> getLocationById(Long locationId);

    /**
     * 添加/编辑位置信息
     *
     * @param locationQueryDto 位置信息
     * @return 操作结果
     */
    R<?> addOrEditInventoryReceipt(LocationQueryDto locationQueryDto);

    /**
     * 删除位置信息
     *
     * @param locationId 位置信息id
     * @return 操作结果
     */
    R<?> deleteLocation(Long locationId);

}
