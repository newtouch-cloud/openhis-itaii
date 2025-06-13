package com.openhis.workflow.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.common.enums.DelFlag;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.mapper.SupplyRequestMapper;
import com.openhis.workflow.service.ISupplyRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 供应申请管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class SupplyRequestServiceImpl extends ServiceImpl<SupplyRequestMapper, SupplyRequest>
    implements ISupplyRequestService {

    /**
     * 通过单据号查询单据信息
     *
     * @param busNo 单据号
     * @return 单据信息
     */
    @Override
    public List<SupplyRequest> getSupplyByBusNo(String busNo) {
        return baseMapper.selectList(new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getBusNo, busNo));
    }

    /**
     * 同意申请
     *
     * @param busNo 单据号
     * @param now 当前时间
     * @return 单据详情
     */
    @Override
    public List<SupplyRequest> agreeRequest(String busNo, Date now) {
        // 更新单据状态
        baseMapper.update(null,
            new LambdaUpdateWrapper<SupplyRequest>().eq(SupplyRequest::getBusNo, busNo)
                .set(SupplyRequest::getApprovalTime, now)
                .set(SupplyRequest::getApproverId, SecurityUtils.getLoginUser().getPractitionerId())
                .set(SupplyRequest::getStatusEnum, SupplyStatus.AGREE.getValue()));
        // 返回单据详情
        return this.getSupplyByBusNo(busNo);
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public boolean submitApproval(String busNo) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<SupplyRequest>().eq(SupplyRequest::getBusNo, busNo)
                .set(SupplyRequest::getApplyTime, DateUtils.getNowDate())
                .set(SupplyRequest::getStatusEnum, SupplyStatus.APPROVAL.getValue()));
        return updateCount > 0;
    }

    /**
     * 撤回
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public boolean withdrawApproval(String busNo) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<SupplyRequest>().eq(SupplyRequest::getBusNo, busNo)
                .set(SupplyRequest::getApplyTime, DateUtils.getNowDate())
                .set(SupplyRequest::getStatusEnum, SupplyStatus.WITHDRAW.getValue()));
        return updateCount > 0;
    }

    /**
     * 驳回申请
     *
     * @param busNo 单据号
     */
    @Override
    public boolean rejectRequest(String busNo) {
        // 更新单据状态
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<SupplyRequest>().eq(SupplyRequest::getBusNo, busNo)
                .set(SupplyRequest::getApprovalTime, DateUtils.getNowDate())
                .set(SupplyRequest::getApproverId, SecurityUtils.getLoginUser().getPractitionerId())
                .set(SupplyRequest::getStatusEnum, SupplyStatus.REJECT.getValue()));
        return updateCount > 0;
    }

    /**
     * 获取供应项目所在表
     *
     * @param agreedList 供应单据信息
     * @return 供应项目所在表
     */
    @Override
    public String getItemTable(List<SupplyRequest> agreedList) {
        return agreedList.stream().map(SupplyRequest::getItemTable).findFirst().orElse(null);
    }

    /**
     * 获取供应的物品
     *
     * @param agreedList 供应单据
     * @return 物品id
     */
    @Override
    public List<Long> getItem(List<SupplyRequest> agreedList) {
        return agreedList.stream().map(SupplyRequest::getItemId).collect(Collectors.toList());
    }

    /**
     * 校验(已经审批通过的单号(请求状态是同意),不能再重复编辑请求)
     *
     * @param busNo 单据号
     */
    @Override
    public boolean supplyRequestValidation(String busNo) {

        // 根据单据号查询请求状态
        List<SupplyRequest> requestList =
            baseMapper.selectList(new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getBusNo, busNo));
        if (!requestList.isEmpty()) {
            for (SupplyRequest supplyRequest : requestList) {
                if (SupplyStatus.AGREE.getValue().equals(supplyRequest.getStatusEnum())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证是否发生过业务
     *
     * @param itemId 项目id
     * @return 校验结果
     */
    @Override
    public boolean verifyAbleEdit(Long itemId) {
        return baseMapper.exists(new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getItemId, itemId)
            .eq(SupplyRequest::getDeleteFlag, DelFlag.NO.getCode()));
    }
}
