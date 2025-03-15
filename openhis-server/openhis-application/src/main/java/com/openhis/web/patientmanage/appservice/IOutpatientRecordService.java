package com.openhis.web.patientmanage.appservice;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;

/**
 * 门诊记录查询
 *
 * @author Wuser
 * @date 2025/3/15
 */
public interface IOutpatientRecordService {

    /**
     * 获取门诊记录初期数据列表
     *
     * @return 门诊记录初期数据列表
     */
    List<String> getOutpatientRecordInit();

    /**
     * 分页查询门诊记录
     *
     * @param outpatientRecordSearchParam 门诊录查询参数
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     * @return 分页查询
     */
    Page<OutpatientRecordDto> getPatient(OutpatientRecordSearchParam outpatientRecordSearchParam, Integer pageNo,
        Integer pageSize);

}
