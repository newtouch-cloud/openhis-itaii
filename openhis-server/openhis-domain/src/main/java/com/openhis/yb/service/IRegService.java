package com.openhis.yb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.yb.domain.ClinicReg;

import java.util.List;

/**
 * 挂号管理-服务接口
 */
public interface IRegService extends IService<ClinicReg> {
    void updateStatus(Long id,String status);

    /**
     * 通过单据号查询
     *
     * @param busNo 单据号
     * @return
     */
    ClinicReg getByBusNo(String busNo);

}
