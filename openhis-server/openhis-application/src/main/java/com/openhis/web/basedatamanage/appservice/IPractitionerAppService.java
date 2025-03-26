package com.openhis.web.basedatamanage.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.UserAndPractitionerDto;

/**
 * 参与者 应该服务类
 */
public interface IPractitionerAppService {

    /**
     * 新增用户及参与者
     * 
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    R<?> saveUserPractitioner(UserAndPractitionerDto userAndPractitionerDto);

    /**
     * 查询用户及参与者
     * 
     * @param userAndPractitionerDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 用户及参与者
     */
    IPage<UserAndPractitionerDto> getUserPractitionerPage(UserAndPractitionerDto userAndPractitionerDto,
        String searchKey, Integer pageNo, Integer pageSize);

}
