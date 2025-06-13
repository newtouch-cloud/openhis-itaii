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

    /**
     * 获取就诊患者的自费账户id
     * 
     * @param encounterId 就诊id
     * @return 账户id
     */
    Long getSelfPayAccount(Long encounterId);

    /**
     * 获取就诊患者的医保账户id
     *
     * @param encounterId 就诊id
     * @return 账户id
     */
    Long getMedicalInsuranceAccount(Long encounterId);

    /**
     * 插入或更新 Account 实体
     * 
     * @param account 实体对象
     * @return 是否成功
     */
    boolean saveOrUpdateAccount(Account account);

    /**
     * 插入或更新 Account 实体
     *
     * @param account 实体对象
     * @return 是否成功
     */
    boolean isSelfPay(Account account);
}