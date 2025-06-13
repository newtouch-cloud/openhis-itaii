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
import com.core.system.service.ISysTenantService;
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
import com.openhis.common.enums.PractitionerRoles;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.basedatamanage.dto.PractitionerOrgAndLocationDto;
import com.openhis.web.basedatamanage.dto.PractitionerRolesDto;
import com.openhis.web.basedatamanage.dto.SelectableOrgDto;
import com.openhis.web.basedatamanage.dto.UserAndPractitionerDto;
import com.openhis.web.basedatamanage.mapper.PractitionerAppAppMapper;

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

    @Resource
    ISysTenantService sysTenantService;

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
        String pharPracCertNo = userAndPractitionerDto.getPharPracCertNo(); // 职业证件编号
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
        Long userId =
            iBizUserService.getOne(new LambdaQueryWrapper<BizUser>().eq(BizUser::getUserName, userName)).getUserId(); // 用户id

        // 初始化租户绑定
        sysTenantService.initTenantBind(userId);

        // 新增 sys_user_role
        List<PractitionerRolesDto> practitionerRoleDtoList = userAndPractitionerDto.getPractitionerRolesDtoList();
        BizUserRole bizUserRole;
        for (PractitionerRolesDto practitionerRolesDto : practitionerRoleDtoList) {
            bizUserRole = new BizUserRole();
            bizUserRole.setUserId(userId);
            bizUserRole.setRoleId(practitionerRolesDto.getRoleId());
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
        // 责任科室
        List<PractitionerOrgAndLocationDto> responsibilityOrgDtoList =
            userAndPractitionerDto.getResponsibilityOrgDtoList();
        practitioner.setOrgId(responsibilityOrgDtoList.get(0).getOrgId()); // 机构id
        practitioner.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(nickName)); // 拼音码
        practitioner.setWbStr(ChineseConvertUtils.toWBFirstLetter(nickName)); // 五笔码
        practitioner.setPharPracCertNo(pharPracCertNo); // 职业证件编号
        iPractitionerService.save(practitioner);
        Long practitionerId = practitioner.getId();// 参与者id
        // 新增 adm_practitioner_role
        PractitionerRole practitionerRole;
        // 1.责任科室
        for (PractitionerOrgAndLocationDto responsibilityOrgDto : responsibilityOrgDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setOrgId(responsibilityOrgDto.getOrgId()); // 机构id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 2.医生出诊科室
        List<PractitionerOrgAndLocationDto> doctorVisitOrgDtoList = userAndPractitionerDto.getDoctorVisitOrgDtoList();
        for (PractitionerOrgAndLocationDto doctorVisitOrgDto : doctorVisitOrgDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.DOCTOR.getCode());// 角色code
            practitionerRole.setOrgId(doctorVisitOrgDto.getOrgId()); // 机构id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 3.管理库房
        List<PractitionerOrgAndLocationDto> manageLocationDtoList = userAndPractitionerDto.getManageLocationDtoList();
        for (PractitionerOrgAndLocationDto manageLocationDto : manageLocationDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.LOCATION_ADMIN.getCode()); // 角色code
            practitionerRole.setLocationId(manageLocationDto.getLocationId()); // 位置id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 4.管理药房
        List<PractitionerOrgAndLocationDto> manageMedicationLocationDtoList =
            userAndPractitionerDto.getManageMedicationLocationDtoList();
        for (PractitionerOrgAndLocationDto practitionerOrgAndLocationDto : manageMedicationLocationDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.PHARMACIST.getCode()); // 角色code
            practitionerRole.setLocationId(practitionerOrgAndLocationDto.getLocationId()); // 位置id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 5.护士-管理科室集合
        List<PractitionerOrgAndLocationDto> manageOrgDtoList = userAndPractitionerDto.getManageOrgDtoList();
        for (PractitionerOrgAndLocationDto manageOrgDto : manageOrgDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.NURSE.getCode());// 角色code
            practitionerRole.setOrgId(manageOrgDto.getOrgId()); // 机构id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 5.护士-管理病区集合
        List<PractitionerOrgAndLocationDto> manageWardLocationDtoList =
            userAndPractitionerDto.getManageWardLocationDtoList();
        for (PractitionerOrgAndLocationDto manageWardLocationDto : manageWardLocationDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.NURSE.getCode()); // 角色code
            practitionerRole.setLocationId(manageWardLocationDto.getLocationId()); // 位置id
            iPractitionerRoleService.save(practitionerRole);
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"人员信息"}));
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
        queryWrapper.orderByDesc("user_id");
        IPage<UserAndPractitionerDto> userPractitionerPage =
            practitionerAppAppMapper.getUserPractitionerPage(new Page<>(pageNo, pageSize), queryWrapper);
        List<UserAndPractitionerDto> records = userPractitionerPage.getRecords();
        // 参与者id集合
        List<Long> practitionerIdList =
            records.stream().map(UserAndPractitionerDto::getPractitionerId).collect(Collectors.toList());
        // 角色集合
        List<PractitionerRolesDto> practitionerRolesDtoList =
            practitionerAppAppMapper.getPractitionerRolesDtoList(practitionerIdList);
        // 科室和位置
        List<PractitionerOrgAndLocationDto> orgAndLocationDtoList =
            practitionerAppAppMapper.getOrgAndLocationDtoList(practitionerIdList);
        for (UserAndPractitionerDto record : records) {
            // 匹配角色
            List<PractitionerRolesDto> list1 = practitionerRolesDtoList.stream()
                .filter(e -> e.getPractitionerId().equals(record.getPractitionerId())).collect(Collectors.toList());
            record.setPractitionerRolesDtoList(list1);
            // 匹配责任科室
            List<PractitionerOrgAndLocationDto> list2 = orgAndLocationDtoList.stream()
                .filter(e -> e.getPractitionerId().equals(record.getPractitionerId()) && "".equals(e.getRoleCode()))
                .collect(Collectors.toList());
            record.setResponsibilityOrgDtoList(list2);
            // 匹配医生出诊科室
            List<PractitionerOrgAndLocationDto> list3 =
                orgAndLocationDtoList.stream().filter(e -> e.getPractitionerId().equals(record.getPractitionerId())
                    && PractitionerRoles.DOCTOR.getCode().equals(e.getRoleCode())).collect(Collectors.toList());
            record.setDoctorVisitOrgDtoList(list3);
            // 匹配管理库房
            List<PractitionerOrgAndLocationDto> list4 =
                orgAndLocationDtoList.stream()
                    .filter(e -> e.getPractitionerId().equals(record.getPractitionerId())
                        && PractitionerRoles.LOCATION_ADMIN.getCode().equals(e.getRoleCode()))
                    .collect(Collectors.toList());
            record.setManageLocationDtoList(list4);
            // 匹配管理药房
            List<
                PractitionerOrgAndLocationDto> list5 =
                    orgAndLocationDtoList.stream()
                        .filter(e -> e.getPractitionerId().equals(record.getPractitionerId())
                            && PractitionerRoles.PHARMACIST.getCode().equals(e.getRoleCode()))
                        .collect(Collectors.toList());
            record.setManageMedicationLocationDtoList(list5);
            // 匹配护士-管理科室
            List<PractitionerOrgAndLocationDto> list6 = orgAndLocationDtoList.stream()
                .filter(e -> e.getPractitionerId().equals(record.getPractitionerId())
                    && PractitionerRoles.NURSE.getCode().equals(e.getRoleCode()) && e.getOrgId() != null)
                .collect(Collectors.toList());
            record.setManageOrgDtoList(list6);
            // 匹配护士-管理病区
            List<PractitionerOrgAndLocationDto> list7 = orgAndLocationDtoList.stream()
                .filter(e -> e.getPractitionerId().equals(record.getPractitionerId())
                    && PractitionerRoles.NURSE.getCode().equals(e.getRoleCode()) && e.getLocationId() != null)
                .collect(Collectors.toList());
            record.setManageWardLocationDtoList(list7);
        }
        return userPractitionerPage;
    }

    /**
     * 修改用户及参与者 : 登录账号和密码不允许编辑
     *
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    @Override
    public R<?> editUserPractitioner(UserAndPractitionerDto userAndPractitionerDto) {
        Long userId = userAndPractitionerDto.getUserId(); // 系统用户id
        Long practitionerId = userAndPractitionerDto.getPractitionerId(); // 参与者id

        String nickName = userAndPractitionerDto.getNickName();
        String phonenumber = userAndPractitionerDto.getPhonenumber();
        String sex = userAndPractitionerDto.getSex();
        String pharPracCertNo = userAndPractitionerDto.getPharPracCertNo(); // 职业证件编号
        // 编辑 sys_user
        BizUser bizUser = new BizUser();
        bizUser.setNickName(nickName); // 昵称
        bizUser.setEmail(userAndPractitionerDto.getEmail());// 邮箱
        bizUser.setPhonenumber(phonenumber); // 电话
        bizUser.setSex(sex); // 性别
        bizUser.setStatus(userAndPractitionerDto.getStatus()); // 状态
        bizUser.setRemark(userAndPractitionerDto.getRemark()); // 备注
        iBizUserService.update(bizUser, new LambdaQueryWrapper<BizUser>().eq(BizUser::getUserId, userId));
        // 先删除,再新增 sys_user_role
        practitionerAppAppMapper.delUserRole(userId);
        List<PractitionerRolesDto> practitionerRoleDtoList = userAndPractitionerDto.getPractitionerRolesDtoList();
        BizUserRole bizUserRole;
        for (PractitionerRolesDto practitionerRolesDto : practitionerRoleDtoList) {
            bizUserRole = new BizUserRole();
            bizUserRole.setUserId(userId);
            bizUserRole.setRoleId(practitionerRolesDto.getRoleId());
            iBizUserRoleService.save(bizUserRole);
        }
        // 编辑 adm_practitioner
        Practitioner practitioner = new Practitioner();
        practitioner.setId(practitionerId);
        practitioner.setName(nickName); // 姓名
        practitioner.setGenderEnum(Integer.parseInt(sex)); // 性别
        practitioner.setBirthDate(userAndPractitionerDto.getBirthDate()); // 出生日期
        practitioner.setPhone(phonenumber); // 电话
        practitioner.setAddress(userAndPractitionerDto.getAddress()); // 地址
        practitioner.setYbNo(userAndPractitionerDto.getYbNo()); // 医保码
        practitioner.setUserId(userId); // 系统用户id
        // 责任科室
        List<PractitionerOrgAndLocationDto> responsibilityOrgDtoList =
            userAndPractitionerDto.getResponsibilityOrgDtoList();
        practitioner.setOrgId(responsibilityOrgDtoList.get(0).getOrgId()); // 机构id
        practitioner.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(nickName)); // 拼音码
        practitioner.setWbStr(ChineseConvertUtils.toWBFirstLetter(nickName)); // 五笔码
        practitioner.setPharPracCertNo(pharPracCertNo);// 职业证件编号
        iPractitionerService.updateById(practitioner);
        // 先删除,再新增 adm_practitioner_role
        practitionerAppAppMapper.delPractitionerRole(practitionerId);
        PractitionerRole practitionerRole;
        // 1.责任科室
        for (PractitionerOrgAndLocationDto responsibilityOrgDto : responsibilityOrgDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setOrgId(responsibilityOrgDto.getOrgId()); // 机构id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 2.医生出诊科室
        List<PractitionerOrgAndLocationDto> doctorVisitOrgDtoList = userAndPractitionerDto.getDoctorVisitOrgDtoList();
        for (PractitionerOrgAndLocationDto doctorVisitOrgDto : doctorVisitOrgDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.DOCTOR.getCode());// 角色code
            practitionerRole.setOrgId(doctorVisitOrgDto.getOrgId()); // 机构id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 3.管理库房
        List<PractitionerOrgAndLocationDto> manageLocationDtoList = userAndPractitionerDto.getManageLocationDtoList();
        for (PractitionerOrgAndLocationDto manageLocationDto : manageLocationDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.LOCATION_ADMIN.getCode()); // 角色code
            practitionerRole.setLocationId(manageLocationDto.getLocationId()); // 位置id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 4.管理药房
        List<PractitionerOrgAndLocationDto> manageMedicationLocationDtoList =
            userAndPractitionerDto.getManageMedicationLocationDtoList();
        for (PractitionerOrgAndLocationDto practitionerOrgAndLocationDto : manageMedicationLocationDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.PHARMACIST.getCode()); // 角色code
            practitionerRole.setLocationId(practitionerOrgAndLocationDto.getLocationId()); // 位置id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 5.护士-管理科室集合
        List<PractitionerOrgAndLocationDto> manageOrgDtoList = userAndPractitionerDto.getManageOrgDtoList();
        for (PractitionerOrgAndLocationDto manageOrgDto : manageOrgDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.NURSE.getCode());// 角色code
            practitionerRole.setOrgId(manageOrgDto.getOrgId()); // 机构id
            iPractitionerRoleService.save(practitionerRole);
        }
        // 5.护士-管理病区集合
        List<PractitionerOrgAndLocationDto> manageWardLocationDtoList =
            userAndPractitionerDto.getManageWardLocationDtoList();
        for (PractitionerOrgAndLocationDto manageWardLocationDto : manageWardLocationDtoList) {
            practitionerRole = new PractitionerRole();
            practitionerRole.setName(nickName); // 姓名
            practitionerRole.setPractitionerId(practitionerId); // 参与者id
            practitionerRole.setRoleCode(PractitionerRoles.NURSE.getCode()); // 角色code
            practitionerRole.setLocationId(manageWardLocationDto.getLocationId()); // 位置id
            iPractitionerRoleService.save(practitionerRole);
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"人员信息"}));
    }

    /**
     * 删除用户及参与者 ; admin不允许删除
     *
     * @param userId 系统用户id
     * @return 结果
     */
    @Override
    public R<?> delUserPractitioner(Long userId) {
        if (1L == userId) {
            return R.fail(null, "admin不允许删除");
        }
        iBizUserService.remove(new LambdaQueryWrapper<BizUser>().eq(BizUser::getUserId, userId));
        practitionerAppAppMapper.delUserRole(userId);
        Practitioner one =
            iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>().eq(Practitioner::getUserId, userId));
        Long practitionerId = one.getId();// 参与者id
        iPractitionerService.removeById(practitionerId);
        iPractitionerRoleService
            .remove(new LambdaQueryWrapper<PractitionerRole>().eq(PractitionerRole::getPractitionerId, practitionerId));

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"人员信息"}));
    }

    /**
     * 查询可选择切换科室集合
     *
     * @return 可选择切换科室集合
     */
    @Override
    public List<SelectableOrgDto> getSelectableOrgList() {
        // 参与者id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        return practitionerAppAppMapper.getSelectableOrgList(practitionerId);
    }

    /**
     * 切换科室
     *
     * @param orgId 科室id
     * @return 结果
     */
    @Override
    public R<?> switchOrg(Long orgId) {
        // 参与者id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        Practitioner practitioner = new Practitioner();
        practitioner.setId(practitionerId);
        practitioner.setOrgId(orgId);
        iPractitionerService.updateById(practitioner);
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"切换科室"}));
    }

}
