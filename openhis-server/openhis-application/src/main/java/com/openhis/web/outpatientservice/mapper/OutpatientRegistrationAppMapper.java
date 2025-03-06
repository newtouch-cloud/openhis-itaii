package com.openhis.web.outpatientservice.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientservice.dto.PractitionerMetadata;

/**
 * 门诊挂号 应用Mapper
 */
@Repository
public interface OutpatientRegistrationAppMapper {

    IPage<PractitionerMetadata> getPractitionerMetadataPage(@Param("page") Page<PractitionerMetadata> page,
        @Param("locationId") Long locationId, @Param("RoleCode") String RoleCode,
        @Param(Constants.WRAPPER) QueryWrapper<PractitionerMetadata> queryWrapper);

}
