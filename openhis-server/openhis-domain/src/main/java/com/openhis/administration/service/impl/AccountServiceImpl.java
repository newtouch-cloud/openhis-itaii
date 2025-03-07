package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Account;
import com.openhis.administration.mapper.AccountMapper;
import com.openhis.administration.service.IAccountService;

/**
 * 就诊账户管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    /**
     * 门诊挂号时保存账号信息
     *
     * @param account 账号信息
     */
    @Override
    public Long saveAccountByRegister(Account account) {
        baseMapper.insert(account);
        return account.getId();
    }

}