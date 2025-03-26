package com.openhis.web.basedatamanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.ChineseConvertUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.BizUser;
import com.openhis.administration.domain.BizUserRole;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.service.IBizUserRoleService;
import com.openhis.administration.service.IBizUserService;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.basedatamanage.mapper.PractitionerAppAppMapper;
import com.openhis.web.doctorstation.dto.UserAndPractitionerChildDto;
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
        String userName = userAndPractitionerDto.getUserName();
        String nickName = userAndPractitionerDto.getNickName();
        String phonenumber = userAndPractitionerDto.getPhonenumber();
        String sex = userAndPractitionerDto.getSex();
        // 账号唯一性
        long count = iBizUserService.count(new LambdaQueryWrapper<BizUser>().eq(BizUser::getUserName, userName));
        if (count > 0L) {
            return R.fail(null, "账号已存在");
        }
        // 新增 sys_user
        BizUser bizUser = new BizUser();
        bizUser.setUserName(userName); // 账号
        bizUser.setNickName(nickName); // 昵称
        bizUser.setEmail(userAndPractitionerDto.getEmail());// 邮箱
        bizUser.setPhonenumber(phonenumber); // 电话
        bizUser.setSex(sex); // 性别
        bizUser.setPassword(SecurityUtils.encryptPassword(userAndPractitionerDto.getPassword())); // 密码
        bizUser.setStatus(userAndPractitionerDto.getStatus()); // 状态
        bizUser.setRemark(userAndPractitionerDto.getRemark()); // 备注
        iBizUserService.save(bizUser);
        Long userId = bizUser.getUserId(); // 用户id
        // 新增 sys_user_role
        List<Long> roleIds = userAndPractitionerDto.getRoleIds();
        BizUserRole bizUserRole;
        for (Long roleId : roleIds) {
            bizUserRole = new BizUserRole();
            bizUserRole.setUserId(userId);
            bizUserRole.setRoleId(roleId);
            iBizUserRoleService.save(bizUserRole);
        }
        // 新增 adm_practitioner
        Practitioner practitioner = new Practitioner();
        practitioner.setActiveFlag(AccountStatus.ACTIVE.getValue()); // 活动标记
        practitioner.setName(nickName); // 姓名
        practitioner.setGenderEnum(Integer.parseInt(sex)); // 性别
        practitioner.setBirthDate(userAndPractitionerDto.getBirthDate()); // 出生日期
        practitioner.setPhone(phonenumber); // 电话
        practitioner.setAddress(userAndPractitionerDto.getAddress()); // 地址
        practitioner.setYbNo(userAndPractitionerDto.getYbNo()); // 医保码
        practitioner.setUserId(userId); // 系统用户id
        practitioner.setOrgId(userAndPractitionerDto.getOrgId()); // 机构id
        practitioner.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(nickName)); // 拼音码
        practitioner.setWbStr(ChineseConvertUtils.toWBFirstLetter(nickName)); // 五笔码
        iPractitionerService.save(practitioner);
        Long practitionerId = practitioner.getId();// 参与者id
        // 新增 adm_practitioner_role
        List<UserAndPractitionerChildDto> childList = userAndPractitionerDto.getChildList();
        PractitionerRole practitionerRole;
        for (UserAndPractitionerChildDto userAndPractitionerChildDto : childList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(userAndPractitionerChildDto.getRoleCode()); // 角色code
            practitionerRole.setOrgId(userAndPractitionerChildDto.getOrgId()); // 机构id
            practitionerRole.setLocationId(userAndPractitionerChildDto.getLocationId()); // 位置id
            iPractitionerRoleService.save(practitionerRole);
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"人员信息"}));
    }

    /**
     * 查询用户及参与者
     *
     * @param userAndPractitionerDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 用户及参与者
     */
    @Override
    public IPage<UserAndPractitionerDto> getUserPractitionerPage(UserAndPractitionerDto userAndPractitionerDto,
        String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<UserAndPractitionerDto> queryWrapper = HisQueryUtils.buildQueryWrapper(userAndPractitionerDto,
            searchKey, new HashSet<>(Arrays.asList("user_name", "nick_name", "py_str", "wb_str")), null);
        IPage<UserAndPractitionerDto> userPractitionerPage =
            practitionerAppAppMapper.getUserPractitionerPage(new Page<>(pageNo, pageSize), queryWrapper);
        List<UserAndPractitionerDto> records = userPractitionerPage.getRecords();
        // 参与者id集合
        List<Long> practitionerIdList =
            records.stream().map(UserAndPractitionerDto::getPractitionerId).collect(Collectors.toList());
        // 子集合
        List<UserAndPractitionerChildDto> childList = practitionerAppAppMapper.getChildList(practitionerIdList);
        for (UserAndPractitionerDto record : records) {
            // 匹配子集合
            List<UserAndPractitionerChildDto> childDtoList =
                childList.stream().filter(e -> e.getPractitionerId().equals(record.getPractitionerId())).collect(Collectors.toList());
            record.setChildList(childDtoList);
        }
        return userPractitionerPage;
    }

}
