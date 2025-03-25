package com.openhis.web.basedatamanage.appservice.impl;

import javax.annotation.Resource;

import com.openhis.administration.service.IBizUserRoleService;
import com.openhis.administration.service.IBizUserService;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.administration.service.IPractitionerService;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.basedatamanage.mapper.PractitionerAppAppMapper;
import com.openhis.web.doctorstation.dto.UserAndPractitionerDto;

@Service
public class PractitionerAppServiceImpl implements IPractitionerAppService {

    @Resource
    PractitionerAppAppMapper practitionerAppAppMapper;

    @Resource
    IBizUserService iBizUserService;

    @Resource
    IBizUserRoleService iBizUserRoleService;

    @Resource
    IPractitionerService iPractitionerService;

    @Resource
    IPractitionerRoleService iPractitionerRoleService;


    /**
     * 新增用户及参与者
     * 
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    @Override
    public R<?> saveUserPractitioner(UserAndPractitionerDto userAndPractitionerDto) {
        // 数据有效性校验 : 账号唯一性

        // 新增 sys_user

        // 新增 sys_user_role

        // 新增 adm_practitioner

        // 新增 adm_practitioner_role

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"人员信息"}));
    }

}
