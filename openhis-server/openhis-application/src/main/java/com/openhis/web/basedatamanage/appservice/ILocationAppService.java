package com.openhis.web.basedatamanage.appservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.basedatamanage.dto.LocationQueryDto;

/**
 * Location 应该服务类
 */
public interface ILocationAppService {
    // 查询位置树
    Page<LocationQueryDto> getLocationTree(Integer formKey, Integer pageNo, Integer pageSize);

}
