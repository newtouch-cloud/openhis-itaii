package com.openhis.web.basedatamanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerMapper;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.basedatamanage.dto.PractSearchParam;
import com.openhis.web.basedatamanage.dto.PractitionerDto;

@Service
public class PractitionerAppServiceImpl implements IPractitionerAppService {

    @Autowired
    private IPractitionerService practitionerService;

    @Autowired
    private PractitionerMapper practitionerMapper;

    @Autowired
    private IPractitionerRoleService practitionerRoleService;

    @Override
    public R<?> getPractitionerPage(PractSearchParam practSearchParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<Practitioner> queryWrapper = HisQueryUtils.buildQueryWrapper(practSearchParam, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), request);

        // 查询员工分页列表
        Page<PractitionerDto> practitionerDtoPage =
            HisPageUtils.selectPage(practitionerMapper, queryWrapper, pageNo, pageSize, PractitionerDto.class);

        return R.ok(practitionerDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"员工信息"}));
    }

    /**
     * 员工信息详情
     *
     * @param practitionerId 员工信息id
     * @return 员工信息详情
     */
    @Override
    public R<?> getPractitionerById(Long practitionerId) {
        Practitioner practitioner = practitionerService.getById(practitionerId);
        if (practitioner != null) {
            return R.ok(practitioner,
                MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"员工信息"}));
        } else {
            return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"员工信息查询失败"}));
        }
    }

    /**
     * 添加/编辑员工信息
     *
     * @param practitionerDto 员工信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditPractitioner(PractitionerDto practitionerDto) {

        Practitioner practitioner = new Practitioner();
        BeanUtils.copyProperties(practitionerDto, practitioner);
        PractitionerRole practitionerRole = new PractitionerRole();

        if (practitionerDto.getId() != null) {
            // 更新员工信息
            practitionerService.updateById(practitioner);
        } else {
            // 新增员工信息
            practitionerService.save(practitioner);

            // 新增员工岗位信息
            practitionerRole.setPractitionerId(practitioner.getId());
            practitionerRole.setName(practitioner.getName());
            practitionerRoleService.save(practitionerRole);
        }
        // 返回员工id
        return R.ok(practitioner.getId(),
            MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"员工信息更新添加"}));
    }

    /**
     * 删除员工
     *
     * @param practitionerId 员工信息id
     * @return 操作结果
     */
    @Override
    public R<?> deletePractitioner(Long practitionerId) {
        // 删除员工信息
        boolean deletepractitioner = practitionerService.removeById(practitionerId);
        return deletepractitioner
            ? R.ok(practitionerId, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"员工信息"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"员工信息"}));
    }
}
