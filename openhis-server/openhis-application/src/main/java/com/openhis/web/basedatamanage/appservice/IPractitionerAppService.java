package com.openhis.web.basedatamanage.appservice;

import com.core.common.core.domain.R;
import com.openhis.web.doctorstation.dto.UserAndPractitionerDto;

/**
 * 参与者 应该服务类
 */
public interface IPractitionerAppService {

    /**
     * 新增用户及参与者
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    R<?> saveUserPractitioner(UserAndPractitionerDto userAndPractitionerDto);

}
