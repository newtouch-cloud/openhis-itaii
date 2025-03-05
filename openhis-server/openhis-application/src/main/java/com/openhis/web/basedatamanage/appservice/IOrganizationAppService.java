package com.openhis.web.basedatamanage.appservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.basedatamanage.dto.OrganizationQueryDto;

/**
 * Organization 应该服务类
 */
public interface IOrganizationAppService {
    // 查询机构树
    Page<OrganizationQueryDto> getOrganizationTree(Integer pageNo, Integer pageSize);

}
