package com.openhis.workflow.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.AssignSeqUtil;
import com.openhis.administration.domain.Practitioner;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.common.enums.DbOpType;
import com.openhis.common.enums.DispenseStatus;
import com.openhis.workflow.domain.DeviceDispense;
import com.openhis.workflow.domain.DeviceRequest;
import com.openhis.workflow.mapper.DeviceDispenseMapper;
import com.openhis.workflow.service.IDeviceDispenseService;

/**
 * 器材发放管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class DeviceDispenseServiceImpl extends ServiceImpl<DeviceDispenseMapper, DeviceDispense>
    implements IDeviceDispenseService {

    @Resource
    AssignSeqUtil assignSeqUtil;

    /**
     * 处理器材发放信息
     *
     * @param deviceRequest 器材请求信息
     * @param dbOpType db操作类型
     */
    @Override
    public void handleDeviceDispense(DeviceRequest deviceRequest, String dbOpType) {
        DeviceDispense deviceDispense = new DeviceDispense();
        // 器材发放id
        deviceDispense.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_DIS_NO.getPrefix(), 10));
        // 器材请求id
        deviceDispense.setDeviceReqId(deviceRequest.getId());
        // 器材发放状态
        deviceDispense.setStatusEnum(DispenseStatus.DRAFT.getValue());
        // 发药类型
        deviceDispense.setDispenseCategoryEnum(deviceRequest.getCategoryEnum());
        // 器材编码
        deviceDispense.setDeviceDefId(deviceRequest.getDeviceDefId());
        // 领药患者
        deviceDispense.setPatientId(deviceRequest.getPatientId());
        // 相关诊疗
        deviceDispense.setEncounterId(deviceRequest.getEncounterId());
        // 发放数量
        deviceDispense.setQuantity(deviceRequest.getQuantity());
        // 发放单位
        deviceDispense.setUnitCode(deviceRequest.getUnitCode());
        // 产品批号
        deviceDispense.setLotNumber(deviceRequest.getLotNumber());
        // 发药人
        deviceDispense.setPerformerId(deviceRequest.getPerformerId());
        // 发放器材房
        deviceDispense.setLocationId(deviceRequest.getPerformLocation());
        // 支持用药信息
        deviceDispense.setSupportInfo(deviceRequest.getSupportInfo());
        // 已发药数量
        deviceDispense.setDispenseQuantity(0);
        // 发药频次
        deviceDispense.setDispenseFrequencyCode(deviceRequest.getRateCode());

        if (DbOpType.INSERT.getCode().equals(dbOpType)) {
            baseMapper.insert(deviceDispense);
        } else if (DbOpType.UPDATE.getCode().equals(dbOpType)) {
            baseMapper.update(deviceDispense,
                new LambdaUpdateWrapper<DeviceDispense>().eq(DeviceDispense::getDeviceReqId, deviceRequest.getId()));
        }
    }

    /**
     * 删除器材发放信息
     *
     * @param deviceReqId 器材请求id
     */
    @Override
    public void deleteDeviceDispense(Long deviceReqId) {
        baseMapper.delete(new LambdaQueryWrapper<DeviceDispense>().eq(DeviceDispense::getDeviceReqId, deviceReqId));
    }

    /**
     * 更新未发放耗材状态：停止发放
     *
     * @param devDisIdList 发放id列表
     * @param refund 停止原因：退费
     */
    @Override
    public void updateStopDispenseStatus(List<Long> devDisIdList, Integer refund) {
        baseMapper.update(
            new DeviceDispense().setStatusEnum(DispenseStatus.STOPPED.getValue()).setNotPerformedReasonEnum(refund),
            new LambdaUpdateWrapper<DeviceDispense>().in(DeviceDispense::getId, devDisIdList));
    }

    /**
     * 获取执行过的器材数据
     *
     * @param basedOnId 请求基于什么的ID
     */
    @Override
    public List<DeviceDispense> selectDeviceDispenseByBasedOnId(Long basedOnId) {
        return (baseMapper
            .selectList(new LambdaQueryWrapper<DeviceDispense>().eq(DeviceDispense::getBasedOnId, basedOnId)
                .eq(DeviceDispense::getStatusEnum, DispenseStatus.COMPLETED.getValue())));
    }

    /**
     * 执行器材发放
     *
     * @param deviceDispense 器材发放信息
     * @param now 当前时间
     * @param loginUser 登录用户信息
     * @param step 执行次数
     * @param quantity 发药数量
     */
    @Override
    public DeviceDispense createCompletedDeviceDispense(DeviceDispense deviceDispense, Date now, Practitioner loginUser,
        String step, Integer quantity) {
        // 服务请求编码
        deviceDispense.setBusNo(deviceDispense.getBusNo() + "." + step);
        // 请求基于什么
        deviceDispense.setBasedOnTable(CommonConstants.TableName.WOR_DEVICE_DISPENSE);
        // 请求基于什么的ID
        deviceDispense.setBasedOnId(deviceDispense.getId());
        // 已发药数量
        deviceDispense.setDispenseQuantity(quantity);
        // 状态
        deviceDispense.setStatusEnum(DispenseStatus.COMPLETED.getValue());
        // 发药人
        deviceDispense.setPerformerId(loginUser.getId());
        // 发放科室
        deviceDispense.setLocationId(loginUser.getOrgId());
        // 发药时间
        deviceDispense.setDispenseTime(now);
        // id
        deviceDispense.setId(null);
        // 新增器材发放
        baseMapper.insert(deviceDispense);

        return deviceDispense;
    }

    /**
     * 器材发放状态：已发药
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    @Override
    public boolean completedStatusEnum(Long id, Date now, Practitioner loginUser) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<DeviceDispense>().eq(DeviceDispense::getId, id)
                .set(DeviceDispense::getStatusEnum, DispenseStatus.COMPLETED.getValue())
                .set(DeviceDispense::getPerformerId, loginUser.getId())
                .set(DeviceDispense::getLocationId, loginUser.getOrgId()).set(DeviceDispense::getDispenseTime, now));
        return updateCount > 0;
    }

    /**
     * 器材发放状态：撤回
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    @Override
    public boolean cancelledStatusEnum(Long id, Date now, Practitioner loginUser) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<DeviceDispense>().eq(DeviceDispense::getId, id)
                .set(DeviceDispense::getStatusEnum, DispenseStatus.CANCELLED.getValue())
                .set(DeviceDispense::getPerformerId, loginUser.getId())
                .set(DeviceDispense::getLocationId, loginUser.getOrgId()).set(DeviceDispense::getDispenseTime, now));
        return updateCount > 0;
    }

    /**
     * 器材发放状态：待发药
     *
     * @param id ID
     * @param now 当前时间
     * @param loginUser 登录用户信息
     */
    @Override
    public boolean inProgressStatusEnum(Long id, Date now, Practitioner loginUser) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<DeviceDispense>().eq(DeviceDispense::getId, id)
                .set(DeviceDispense::getStatusEnum, DispenseStatus.IN_PROGRESS.getValue())
                .set(DeviceDispense::getPerformerId, loginUser.getId())
                .set(DeviceDispense::getLocationId, loginUser.getOrgId()).set(DeviceDispense::getDispenseTime, now));
        return updateCount > 0;
    }

    /**
     * 更新耗材状态：待配药
     *
     * @param deviceRequestIdList 请求id列表
     */
    @Override
    public void updatePreparationDispenseStatus(List<Long> deviceRequestIdList) {
        baseMapper.update(null,
            new LambdaUpdateWrapper<DeviceDispense>()
                .set(DeviceDispense::getStatusEnum, DispenseStatus.PREPARATION.getValue())
                .in(DeviceDispense::getDeviceReqId, deviceRequestIdList));
    }

    /**
     * 更新耗材状态：暂停
     *
     * @param devReqIdList 请求id列表
     */
    @Override
    public void updateOnHoldDispenseStatus(List<Long> devReqIdList) {
        baseMapper.update(null,
                new LambdaUpdateWrapper<DeviceDispense>()
                        .set(DeviceDispense::getStatusEnum, DispenseStatus.ON_HOLD.getValue())
                        .in(DeviceDispense::getDeviceDefId, devReqIdList));
    }

}
