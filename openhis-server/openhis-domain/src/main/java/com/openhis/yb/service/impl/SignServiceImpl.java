package com.openhis.yb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.yb.service.ISignService;
import com.openhis.yb.domain.Sign;
import com.openhis.yb.mapper.SignMapper;

import org.springframework.stereotype.Service;

/**
 * 签到管理-服务
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {

}
