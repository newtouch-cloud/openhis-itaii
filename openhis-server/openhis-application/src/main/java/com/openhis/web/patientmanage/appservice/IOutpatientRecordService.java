package com.openhis.web.patientmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;

/**
 * 门诊记录查询
 *
 * @author liuhr
 * @date 2025/3/15
 */
public interface IOutpatientRecordService {

    /**
     * 分页查询门诊记录
     *
     * @param outpatientRecordSearchParam 门诊录查询参数
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     * @return 分页查询
     */
    IPage<OutpatientRecordDto> getPatient(OutpatientRecordSearchParam outpatientRecordSearchParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 获取医生名字列表
     *
     * @return 医生名字列表
     */
    R<?> getDoctorNames();
}
