package com.openhis.sys.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.sys.domain.OperationRecord;

/**
 * 系统操作记录Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IOperationRecordService extends IService<OperationRecord> {

    /**
     * 新增操作记录(实体参数)
     * 
     * @param dbOpType 操作类型
     * @param tableName 表名
     * @param entity 参数
     */
    void addEntityOperationRecord(String dbOpType, String tableName, Object entity);

    /**
     * 新增操作记录(ids参数)
     *
     * @param dbOpType 操作类型
     * @param tableName 表名
     * @param ids 参数
     */
    void addIdsOperationRecord(String dbOpType, String tableName, List<Long> ids);

}