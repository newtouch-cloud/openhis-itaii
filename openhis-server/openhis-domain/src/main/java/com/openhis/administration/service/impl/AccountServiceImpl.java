package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Account;
import com.openhis.administration.mapper.AccountMapper;
import com.openhis.administration.service.IAccountService;
import com.openhis.common.enums.AccountType;

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

    /**
     * 获取就诊患者的自费账户id
     *
     * @param encounterId 就诊id
     * @return 账户id
     */
    @Override
    public Long getSelfPayAccount(Long encounterId) {
        Account account = baseMapper.selectOne(new LambdaQueryWrapper<Account>().select(Account::getId)
            .eq(Account::getEncounterId, encounterId).eq(Account::getTypeCode, AccountType.SELF_PAY.getCode()));
        if (account != null) {
            return account.getId();
        }
        return null;
    }

    /**
     * 获取就诊患者的医保账户id
     *
     * @param encounterId 就诊id
     * @return 账户id
     */
    @Override
    public Long getMedicalInsuranceAccount(Long encounterId) {
        Account account = baseMapper
            .selectOne(new LambdaQueryWrapper<Account>().select(Account::getId).eq(Account::getEncounterId, encounterId)
                .eq(Account::getTypeCode, AccountType.MEDICAL_INSURANCE.getCode()));
        if (account != null) {
            return account.getId();
        }
        return null;
    }

}