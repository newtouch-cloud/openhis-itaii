package com.openhis.financial.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.financial.domain.Claim;
import com.openhis.financial.mapper.ClaimMapper;
import com.openhis.financial.service.IClaimService;

/**
 * 索赔管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ClaimServiceImpl extends ServiceImpl<ClaimMapper, Claim> implements IClaimService {

}