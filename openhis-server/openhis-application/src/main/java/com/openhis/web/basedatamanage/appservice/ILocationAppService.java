package com.openhis.web.basedatamanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.dto.LocationAddOrEditDto;
import com.openhis.web.basedatamanage.dto.LocationPageParam;

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
     * 删除位置信息
     *
     * @param busNo 位置信息编码
     * @return 操作结果
     */
    R<?> deleteLocation(String busNo);

    /**
     * 位置分页列表
     *
     * @param locationPageParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询条件
     * @param request 请求
     * @return 位置分页列表
     */
    R<?> getLocationPage(LocationPageParam locationPageParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 新增位置信息
     *
     * @param locationAddOrEditDto 库房位置信息
     * @return 操作结果
     */
    R<?> addLocation(LocationAddOrEditDto locationAddOrEditDto);

    /**
     * 编辑位置信息
     *
     * @param locationAddOrEditDto 库房位置信息
     * @return 操作结果
     */
    R<?> editLocation(LocationAddOrEditDto locationAddOrEditDto);

    /**
     * 位置初始化
     *
     * @return 初始化信息
     */
    R<?> locationInit();
}
