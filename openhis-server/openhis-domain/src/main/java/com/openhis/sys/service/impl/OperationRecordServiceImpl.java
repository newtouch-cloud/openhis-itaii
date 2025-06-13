package com.openhis.sys.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhis.sys.domain.OperationRecord;
import com.openhis.sys.mapper.OperationRecordMapper;
import com.openhis.sys.service.IOperationRecordService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统操作记录Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class OperationRecordServiceImpl extends ServiceImpl<OperationRecordMapper, OperationRecord>
    implements IOperationRecordService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 新增操作记录
     *
     * @param dbOpType 操作类型
     * @param tableName 表名
     * @param entity 参数
     */
    @Override
    public void addEntityOperationRecord(String dbOpType, String tableName, Object entity) {
        try {

            String paramJson = serializeToKeyValueString(entity);
            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setDbOpType(dbOpType);
            operationRecord.setTableName(tableName);
            operationRecord.setParamJson(paramJson);
            baseMapper.insert(operationRecord);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增操作记录(ids参数)
     *
     * @param dbOpType 操作类型
     * @param tableName 表名
     * @param ids 参数
     */
    @Override
    public void addIdsOperationRecord(String dbOpType, String tableName, List<Long> ids) {
        String paramJson = ids.stream().map(String::valueOf) // 将 Long 转为 String
            .collect(Collectors.joining(","));
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setDbOpType(dbOpType);
        operationRecord.setTableName(tableName);
        operationRecord.setParamJson(paramJson);
        baseMapper.insert(operationRecord);
    }

    /**
     * 将对象序列化为键值对JSON字符串
     * 
     * @param entity 任意对象
     * @return JSON字符串
     */
    private static String serializeToKeyValueString(Object entity) throws JsonProcessingException {
        // 使用Jackson库将对象转为Map
        Map<String, Object> map = objectMapper.convertValue(entity, Map.class);
        return objectMapper.writeValueAsString(map);
    }

}