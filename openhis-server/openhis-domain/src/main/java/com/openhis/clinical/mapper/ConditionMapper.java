package com.openhis.clinical.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.clinical.domain.Condition;

/**
 * 疾病与诊断管理Mapper接口
 *
 * @author system
 * @date 2025-02-20
 */
@Repository
public interface ConditionMapper extends BaseMapper<Condition> {

    /**
     * 删除诊断管理
     *
     * @param encounterId 就诊id
     */
    void deleteByEncounterId(@Param("encounterId") Long encounterId);


    /**
     * 删除中医诊断管理
     *
     * @param encounterId 就诊id
     */
    void deleteTcmByEncounterId(@Param("encounterId") Long encounterId);

}