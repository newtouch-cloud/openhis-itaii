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
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerRoleMapper;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.appservice.IPractitionerRoleAppService;
import com.openhis.web.basedatamanage.dto.PractRoleSearchParam;
import com.openhis.web.basedatamanage.dto.PractitionerRoleDto;

@Service
public class PractitionerRoleAppServiceImpl implements IPractitionerRoleAppService {

    @Autowired
    private IPractitionerRoleService practitionerRoleService;
    @Autowired
    private PractitionerRoleMapper practitionerRoleMapper;

    @Override
    public R<?> getPractitionerPage(PractRoleSearchParam practRoleSearchParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<PractitionerRole> queryWrapper = HisQueryUtils.buildQueryWrapper(practRoleSearchParam, searchKey,
            new HashSet<>(Arrays.asList("name")), request);

        // 查询岗位分页列表
        Page<PractitionerRoleDto> practitionerRoleDtoPage =
            HisPageUtils.selectPage(practitionerRoleMapper, queryWrapper, pageNo, pageSize, PractitionerRoleDto.class);

        return R.ok(practitionerRoleDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"岗位信息"}));
    }

    /**
     * 岗位信息详情
     *
     * @param practitionerRoleId 岗位信息id
     * @return 岗位信息详情
     */
    @Override
    public R<?> getPractitionerRoleById(Long practitionerRoleId) {
        PractitionerRole practitionerRole = practitionerRoleService.getById(practitionerRoleId);
        if (practitionerRole != null) {
            return R.ok(practitionerRole,
                MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"岗位信息"}));
        } else {
            return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"岗位信息查询失败"}));
        }
    }

    /**
     * 添加/编辑岗位信息
     *
     * @param practitionerRoleDto 岗位信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditPractitionerRole(PractitionerRoleDto practitionerRoleDto) {

        PractitionerRole practitionerRole = new PractitionerRole();
        BeanUtils.copyProperties(practitionerRoleDto, practitionerRole);
        // if (practitionerRole.getRoleCode() == null) {
        // return R.fail(PromptMsgConstant.Common.M00007, "角色编码不能为空");
        // }
        // if (practitionerRole.getOrgId() == null) {
        // return R.fail(PromptMsgConstant.Common.M00007, "科室不能为空");
        // }
        // if (practitionerRole.getLocationId() == null) {
        // return R.fail(PromptMsgConstant.Common.M00007, "位置不能为空");
        // }

        if (practitionerRoleDto.getId() != null) {
            // 更新岗位信息
            practitionerRoleService.updateById(practitionerRole);
        } else {
            // 新增岗位信息
            // practitionerRoleService.save(practitionerRole);
        }

        // 返回岗位id
        return R.ok(practitionerRole.getId(),
            MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"岗位信息更新添加"}));
    }

    /**
     * 删除岗位
     *
     * @param practitionerRoleId 岗位信息id
     * @return 操作结果
     */
    @Override
    public R<?> deletePractitionerRole(Long practitionerRoleId) {
        // 删除岗位信息
        boolean deletePractitionerRole = practitionerRoleService.removeById(practitionerRoleId);
        return deletePractitionerRole
            ? R.ok(practitionerRoleId,
                MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"岗位信息"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"岗位信息"}));
    }

}
