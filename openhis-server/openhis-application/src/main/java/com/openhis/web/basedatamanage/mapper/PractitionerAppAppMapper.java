package com.openhis.web.basedatamanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.doctorstation.dto.UserAndPractitionerChildDto;
import com.openhis.web.doctorstation.dto.UserAndPractitionerDto;

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
     * 查询子集合
     * 
     * @param practitionerIdList 参与者id集合
     * @return 子集合
     */
    List<UserAndPractitionerChildDto> getChildList(@Param("practitionerIdList") List<Long> practitionerIdList);

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

}
