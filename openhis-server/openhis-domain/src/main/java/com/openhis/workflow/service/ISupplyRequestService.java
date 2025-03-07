package com.openhis.workflow.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.model.LoginUser;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 供应申请管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface ISupplyRequestService extends IService<SupplyRequest> {

    /**
     * 通过单据号查询单据信息
     *
     * @param busNo 单据号
     * @return 单据信息
     */
    List<SupplyRequest> getSupplyByBusNo(String busNo);

    /**
     * 同意申请
     * 
     * @param busNo 单据号
     * @param loginUser 登录用户信息
     * @param now 当前时间
     * @return 单据详情
     */
    List<SupplyRequest> agreeRequest(String busNo, LoginUser loginUser, Date now);

    /**
     * 提交审批
     * 
     * @param busNo 单据号
     * @return 操作结果
     */
    boolean submitApproval(String busNo);

    /**
     * 撤回
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    boolean withdrawApproval(String busNo);

    /**
     * 驳回申请
     *
     * @param busNo 单据号
     * @param loginUser 登录用户信息
     * @param now 当前时间
     */
    boolean rejectRequest(String busNo, LoginUser loginUser, Date now);

    /**
     * 获取供应项目所在表
     *
     * @param agreedList 供应单据信息
     * @return 供应项目所在表
     */
    String getItemTable(List<SupplyRequest> agreedList);

    /**
     * 获取供应的物品
     *
     * @param agreedList 供应单据
     * @return 物品id
     */
    List<Long> getItem(List<SupplyRequest> agreedList);
}