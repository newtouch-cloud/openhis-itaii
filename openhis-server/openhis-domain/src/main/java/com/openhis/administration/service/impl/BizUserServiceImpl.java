package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.BizUser;
import com.openhis.administration.mapper.BizUserMapper;
import com.openhis.administration.service.IBizUserService;

/**
 * 用户管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class BizUserServiceImpl extends ServiceImpl<BizUserMapper, BizUser> implements IBizUserService {

}