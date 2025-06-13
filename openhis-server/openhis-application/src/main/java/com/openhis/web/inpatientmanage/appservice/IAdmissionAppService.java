package com.openhis.web.inpatientmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;

import com.core.common.core.domain.R;
import com.openhis.web.inpatientmanage.dto.AdmissionSearchParam;
import com.openhis.web.inpatientmanage.dto.AdmissionUpDto;

/**
 * 住院登记 应用实现
 *
 * @author liuhr
 * @since 2025/04/07
 */
public interface IAdmissionAppService {

    /**
     * 病获取住院信息初期数据列表
     *
     * @return 住院信息初期数据列表
     */
    R<?> getAdmissionInfoInit();

    /**
     * 获取住院信息 分页显示
     *
     * @param admissionSearchParam 查询参数
     * @param searchKey 模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 住院信息
     */
    R<?> getAdmissionInfoPage(AdmissionSearchParam admissionSearchParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 住院无档登记
     *
     * @param admissionUpDto 住院登记信息
     */
    R<?> addAdmissionInfo(AdmissionUpDto admissionUpDto);

    /**
     * 登记
     *
     * @param admissionUpDto 住院登记信息
     */
    R<?> editAdmissionInfo(AdmissionUpDto admissionUpDto);

    /**
     * 住院登记详细查询
     *
     * @param id 查询条件
     * @return 住院登记详细查询结果
     */
    R<?> getAdmissionOne(@RequestParam Long id);

}
