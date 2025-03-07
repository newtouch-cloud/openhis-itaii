package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Account;

/**
 * 就诊账户管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IAccountService extends IService<Account> {

    /**
     * 门诊挂号时保存账号信息
     * 
     * @param account 账号信息
     */
    Long saveAccountByRegister(Account account);

}