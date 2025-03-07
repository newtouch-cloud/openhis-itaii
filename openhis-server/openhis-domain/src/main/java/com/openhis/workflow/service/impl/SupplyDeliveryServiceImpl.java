package com.openhis.workflow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.enums.EventStatus;
import com.openhis.common.enums.SupplyType;
import com.openhis.workflow.domain.SupplyDelivery;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.mapper.SupplyDeliveryMapper;
import com.openhis.workflow.service.ISupplyDeliveryService;

/**
 * 供应发放管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class SupplyDeliveryServiceImpl extends ServiceImpl<SupplyDeliveryMapper, SupplyDelivery>
    implements ISupplyDeliveryService {

    /**
     * 根据单据，发放物品
     *
     * @param supplyRequestList 单据信息
     * @param now 当前时间
     * @return 发放详情
     */
    @Override
    public List<SupplyDelivery> createCompletedSupplyDelivery(List<SupplyRequest> supplyRequestList, Date now) {

        List<SupplyDelivery> deliveryList = new ArrayList<>();
        // 根据申请单据生成发放单据
        for (SupplyRequest supplyRequest : supplyRequestList) {
            SupplyDelivery supplyDelivery = new SupplyDelivery();
            supplyDelivery
                // 请求id
                .setRequestId(supplyRequest.getId())
                // 发放状态：已完成
                .setStatusEnum(EventStatus.COMPLETED.getValue())
                // 单据类型：采购入库
                .setTypeEnum(SupplyType.PURCHASE_INVENTORY.getValue())
                // 发放项目所在表
                .setItemTable(supplyRequest.getItemTable())
                // 发放物品id
                .setItemId(supplyRequest.getItemId())
                // 物品单位
                .setUnitIdCode(supplyRequest.getUnitCode())
                // 发放数量
                .setQuantity(supplyRequest.getItemQuantity())
                // 批次号
                .setLotNumber(supplyRequest.getLotNumber())
                // 追溯码
                .setTraceNo(supplyRequest.getTraceNo())
                // 供应商id
                .setSupplierId(supplyRequest.getSupplierId())
                // 审批人
                .setPractitionerId(supplyRequest.getApplicantId())
                // 发放时间
                .setOccurrenceTime(now)
                // 接收位置
                .setReceiverId(supplyRequest.getPurposeLocationId())
                // 接收时间
                .setReceiveTime(now)
                // 生产日期
                .setOccurrenceStartTime(supplyRequest.getStartTime())
                // 失效日期
                .setOccurrenceEndTime(supplyRequest.getEndTime());
            deliveryList.add(supplyDelivery);
            // 新增发放单据
            baseMapper.insert(supplyDelivery);
        }
        return deliveryList;
    }
}