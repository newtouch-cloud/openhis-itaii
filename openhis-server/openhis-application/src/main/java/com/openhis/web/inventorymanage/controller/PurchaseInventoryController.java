/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.controller;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.domain.Location;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.administration.service.ILocationService;
import com.openhis.administration.service.IPatientService;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.service.IMedicationService;
import com.openhis.web.inventorymanage.dto.InventoryDto;
import com.openhis.web.inventorymanage.dto.InventoryReceiptInitDto;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;
import com.openhis.web.inventorymanage.dto.SaveInventoryReceiptDto;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.mapper.SupplyRequestMapper;
import com.openhis.workflow.service.ISupplyRequestService;

import lombok.extern.slf4j.Slf4j;

/**
 * 采购入库 controller
 *
 * @author zwh
 * @date 2025-02-18
 */
@RestController
@RequestMapping("/inventory-manage-purchase")
@Slf4j
public class PurchaseInventoryController {

    @Autowired
    private ISupplyRequestService supplyRequestService;
    @Autowired
    private IMedicationService medicationService;
    @Autowired
    private IPatientService patientService;
    @Autowired
    private IChargeItemService chargeItemService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private ILocationService locationService;
    @Autowired
    private SupplyRequestMapper supplyRequestMapper;

    @GetMapping(value = "/init")
    public R<?> init() {

        InventoryReceiptInitDto initDto = new InventoryReceiptInitDto();
        // 设置供应商列表
        initDto.setSupplier(supplierService.list())
            // 设置药房列表
            .setLocation(locationService.list(new LambdaQueryWrapper<Location>().in(Location::getFormEnum, 1)))
            // 药品详细
            .setMedicationDetail(medicationService.getDetailList());
        return R.ok(initDto);
    }

    // 添加入库单据之前需要
    // 1.supplier供应商信息列表
    // 2.location信息列表包括（药房，药库，材料柜，护理站）
    // 3.practitioner_role与practitioner联查获取对应location的管理员列表
    // 4.查询选定对应药品类型的药品信息列表

    /**
     * 入库单据列表
     *
     * @param inventorySearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 入库单据分页列表
     */
    @GetMapping(value = "/inventory-receipt-page")
    public R<?> getPage(InventorySearchParam inventorySearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.BusNo);

        // 构建查询条件
        QueryWrapper<InventorySearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(inventorySearchParam, searchKey, searchFields, request);

        // Page<InventoryReceiptDto> inventoryReceiptPage= supplyRequestService.page(new
        // Page<>(pageNo,pageSize),queryWrapper);
        return R.ok();
    }

    /**
     * 添加入库单据（生成供应请求）
     *
     * @param inventoryDto 入库单据
     */
    @PostMapping("/inventory-receipt")
    public R<?> addInventoryReceipt(@Validated @RequestBody InventoryDto inventoryDto) {
        // 生成待发送的入库单据
        SupplyRequest supplyRequest = new SupplyRequest();
        BeanUtils.copyProperties(inventoryDto, supplyRequest);
        // 如果业务上不需要其它处理 直接调用service的保存方法
        boolean saveSupplyRequestSuccess = supplyRequestService.save(supplyRequest);

        if (!saveSupplyRequestSuccess) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        // 生成收费项目charge_item
        ChargeItem chargeItem = new ChargeItem();
        // 如果字段很少建议手动set赋值
        chargeItem.setUnitPrice(inventoryDto.getUnitPrice());
        boolean saveChargeItemSuccess = chargeItemService.saveChargeItem(chargeItem);
        // 如果采购单价被修改了，需要根据批次号更新采购单价子表价格、

        // if (saveChargeItemSuccess) {
        // return R.ok();
        // } else {
        // return R.fail();
        // }
        return saveChargeItemSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"采购入库单据"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }

    /**
     * 编辑入库单据
     *
     * @param saveInventoryReceiptDto 供应请求信息
     */
    @PutMapping("/inventory-receipt")
    public R<?> editInventoryReceipt(@Validated @RequestBody SaveInventoryReceiptDto saveInventoryReceiptDto) {
        // 更新supply_request信息
        SupplyRequest saveRequest = new SupplyRequest();
        BeanUtils.copyProperties(saveInventoryReceiptDto, saveRequest);
        if (!supplyRequestService.updateById(saveRequest)) {
            return R.fail();
        }
        // 更新收费项目charge_item
        ChargeItem chargeItem = new ChargeItem();
        BeanUtils.copyProperties(saveInventoryReceiptDto, chargeItem);
        chargeItem.setId(saveInventoryReceiptDto.getChargeItemId());
        return chargeItemService.updateChargeItem(chargeItem) ? R.ok() : R.fail();
    }

    /**
     * 删除方法
     *
     * @param supplyRequestId 主表id
     */
    @DeleteMapping("/inventory-receipt")
    public R<?> deleteInventoryReceipt(@RequestParam Long supplyRequestId) {
        // 全都是逻辑删除

        boolean deleteSuccess = supplyRequestService.removeById(supplyRequestId);

        if (!deleteSuccess) {
            return R.fail();
        }

        boolean deleteChargeItemSuccess = chargeItemService
            .remove(new LambdaUpdateWrapper<ChargeItem>().eq(ChargeItem::getServiceId, supplyRequestId));

        return deleteChargeItemSuccess ? R.ok() : R.fail();
    }

    /**
     * 单据提交申请
     *
     * @param supplyRequest 供应请求信息
     */
    @PutMapping("/submit-examine")
    public void submitExamine(SupplyRequest supplyRequest) {

        // 更改供应请求单据状态
        // 生成供应分发supply_delivery
    }
}
