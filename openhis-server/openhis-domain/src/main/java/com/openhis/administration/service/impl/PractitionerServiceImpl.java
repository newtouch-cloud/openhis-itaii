package com.openhis.administration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.mapper.PractitionerMapper;
import com.openhis.administration.service.IPractitionerService;

/**
 * 医疗参与者管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PractitionerServiceImpl extends ServiceImpl<PractitionerMapper, Practitioner>
    implements IPractitionerService {

    @Autowired
    PractitionerMapper practitionerMapper;

    /**
     * 根据执行人ID查询
     *
     * @param userId 系统用户id
     * @return 医疗参与者管理实体
     */
    @Override
    public Practitioner getPractitionerByUserId(long userId) {

        QueryWrapper<Practitioner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return practitionerMapper.selectOne(queryWrapper);
    }
}