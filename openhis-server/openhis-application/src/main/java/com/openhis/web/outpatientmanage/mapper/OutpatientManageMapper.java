package com.openhis.web.outpatientmanage.mapper;

import java.util.List;

import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import org.apache.ibatis.annotations.Param;

import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordSearchParam;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;

/**
 * 门诊管理
 *
 * @author liuhr
 * @date 2025/3/5
 */
public interface OutpatientManageMapper {

    /**
     * 门诊皮试记录分页查询
     *
     * @param outpatientSkinTestRecordSearchParam 门诊皮试记录查询参数
     * @param pageSize 页面大小
     * @param offset 跳过条数
     * @return 分页查询
     */
    List<OutpatientSkinTestRecordDto> getOutpatientSkinTestRecord(
        @Param("OutpatientSkinTestRecordSearchParam") OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam,
        @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    /**
     * 统计门诊皮试记录数的方法
     *
     * @param outpatientSkinTestRecordSearchParam 门诊皮试记录查询参数
     * @return 分页查询
     */
    long countOutpatientSkinTestRecords(
        @Param("OutpatientSkinTestRecordSearchParam") OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam);

}
