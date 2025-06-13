package com.openhis.workflow.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.model.LoginUser;
import com.openhis.administration.domain.Practitioner;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.workflow.domain.DeviceDispense;
import com.openhis.workflow.domain.DeviceRequest;
import com.openhis.workflow.domain.ServiceRequest;

/**
 * 器材发放管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IDeviceDispenseService extends IService<DeviceDispense> {

    /**
     * 处理器材发放信息
     *
     * @param deviceRequest 器材请求信息
     * @param dbOpType db操作类型
     */
    void handleDeviceDispense(DeviceRequest deviceRequest, String dbOpType);

    /**
     * 删除器材发放信息
     * 
     * @param deviceReqId 器材请求id
     */
    void deleteDeviceDispense(Long deviceReqId);

    /**
     * 更新未发放耗材状态：停止发放
     *
     * @param devDisIdList 发放id列表
     * @param refund 停止原因：退费
     */
    void updateStopDispenseStatus(List<Long> devDisIdList, Integer refund);

    /**
     * 获取执行过的器材数据
     *
     * @param basedOnId 请求基于什么的ID
     */
    List<DeviceDispense> selectDeviceDispenseByBasedOnId(Long basedOnId);

    /**
     * 执行器材发放
     *
     * @param deviceDispense 器材发放信息
     * @param now 当前时间
     * @param loginUser 登录用户信息
     * @param step 执行次数
     * @param quantity 发药数量
     */
    DeviceDispense createCompletedDeviceDispense(DeviceDispense deviceDispense, Date now, Practitioner loginUser,
        String step, Integer quantity);

    /**
     * 器材发放状态：已发药
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    boolean completedStatusEnum(Long id, Date now, Practitioner loginUser);

    /**
     * 器材发放状态：撤回
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    boolean cancelledStatusEnum(Long id, Date now, Practitioner loginUser);

    /**
     * 器材发放状态：待发药
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    boolean inProgressStatusEnum(Long id, Date now, Practitioner loginUser);

    /**
     * 更新耗材状态：待配药
     *
     * @param deviceRequestIdList 请求id列表
     */
    void updatePreparationDispenseStatus(List<Long> deviceRequestIdList);

    /**
     * 更新耗材状态：暂停
     *
     * @param devReqIdList 请求id列表
     */
    void updateOnHoldDispenseStatus(List<Long> devReqIdList);
}
