package com.openhis.administration.service.impl;

import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.Whether;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Account;
import com.openhis.administration.mapper.AccountMapper;
import com.openhis.administration.service.IAccountService;
import com.openhis.common.enums.AccountType;

import javax.validation.constraints.NotNull;

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
        account.setEncounterFlag(Whether.YES.getValue());
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
        Account account = baseMapper
            .selectOne(new LambdaQueryWrapper<Account>().select(Account::getId).eq(Account::getEncounterId, encounterId)
                .eq(Account::getTypeCode, AccountType.PERSONAL_CASH_ACCOUNT.getCode()));
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
                .eq(Account::getTypeCode, AccountType.MEDICAL_ELECTRONIC_CERTIFICATE.getCode()));
        if (account != null) {
            return account.getId();
        }
        return null;
    }

    /**
     * 插入或更新 Account 实体
     * 
     * @param account 实体对象
     * @return 是否成功
     */
    /**
     * 插入或更新 Account 实体
     * 
     * @param account 实体对象
     * @return 是否成功
     */
    @Override
    public boolean saveOrUpdateAccount(Account account) {
        // 创建 LambdaQueryWrapper
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getPatientId, account.getPatientId())
            .eq(Account::getEncounterId, account.getEncounterId())
            // 账户状态是有效的
            .eq(Account::getStatusEnum, AccountStatus.ACTIVE.getValue());

        // 查询是否存在记录
        Account existingAccount = baseMapper.selectOne(queryWrapper);
        if (existingAccount != null) {
            // 如果记录存在，更新记录
            account.setId(existingAccount.getId()); // 设置主键
            return baseMapper.updateById(account) > 0;
        } else {
            // 如果记录不存在，插入新记录
            return baseMapper.insert(account) > 0;
        }
    }

    @Override
    public boolean isSelfPay(@NotNull Account account) {
        return "0000".equals(account.getContractNo());
    }
}