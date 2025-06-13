package com.openhis.web.basedatamanage.mapper;

import java.util.List;

import com.openhis.web.basedatamanage.dto.SelectableOrgDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.basedatamanage.dto.PractitionerOrgAndLocationDto;
import com.openhis.web.basedatamanage.dto.PractitionerRolesDto;
import com.openhis.web.basedatamanage.dto.UserAndPractitionerDto;

/**
 * 参与者 应用Mapper
 */
@Repository
public interface PractitionerAppAppMapper {

    /**
     * 查询用户及参与者
     * 
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 用户及参与者
     */
    IPage<UserAndPractitionerDto> getUserPractitionerPage(@Param("page") Page<UserAndPractitionerDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<UserAndPractitionerDto> queryWrapper);

    /**
     * 查询参与者的角色信息
     * 
     * @param practitionerIdList 参与者id集合
     * @return 参与者的角色信息
     */
    List<PractitionerRolesDto> getPractitionerRolesDtoList(@Param("practitionerIdList") List<Long> practitionerIdList);

    /**
     * 查询科室和位置集合
     * 
     * @param practitionerIdList 参与者id集合
     * @return 科室和位置集合
     */
    List<PractitionerOrgAndLocationDto>
        getOrgAndLocationDtoList(@Param("practitionerIdList") List<Long> practitionerIdList);

    /**
     * 物理删除系统用户与角色的关系
     * 
     * @param userId 系统用户id
     */
    void delUserRole(@Param("userId") Long userId);

    /**
     * 物理删除参与者与业务角色的关系
     * 
     * @param practitionerId 参与者id
     */
    void delPractitionerRole(@Param("practitionerId") Long practitionerId);

    /**
     * 查询可选择切换科室集合
     * 
     * @param practitionerId 参与者id
     * @return 可选择切换科室集合
     */
    List<SelectableOrgDto> getSelectableOrgList(@Param("practitionerId") Long practitionerId);

}
