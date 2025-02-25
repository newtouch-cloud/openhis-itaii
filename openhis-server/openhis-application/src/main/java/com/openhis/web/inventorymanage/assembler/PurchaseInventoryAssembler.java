/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.assembler;

/**
 * 采购入库相关dto转换器
 *
 * @author zwh
 * @date 2025-02-20
 */
public class PurchaseInventoryAssembler {

    /**
     * 装配【入库单据分页列表DTO】分页
     *
     * @param supplyRequestPage 【供应申请管理Entity实体】分页
     * @param medicationList 【药品基本信息管理Entity实体】列表
     * @param patientList【患者管理Entity实体】列表
     * @return 【入库单据分页列表DTO】分页
     */
    // public static Page<InventoryReceiptDto> assembleInventoryReceiptDto(Page<SupplyRequest> supplyRequestPage,
    // List<Medication> medicationList, List<Patient> patientList) {
    //
    // // 将查询到的【药品基本信息管理】列表，作成以ID为Key的Map
    // Map<Long, Medication> medicationMap =
    // medicationList.stream().collect(Collectors.toMap(Medication::getId, Function.identity()));
    //
    // // 将查询到的【患者管理】列表，作成以ID为Key的Map
    // Map<Long, Patient> patientMap =
    // patientList.stream().collect(Collectors.toMap(Patient::getId, Function.identity()));
    //
    // // 定义【入库单据分页列表DTO】的分页，传入【页码】、【行数】、及上面分页的【总数】
    // Page<InventoryReceiptDto> returnPage =
    // new Page<>(supplyRequestPage.getCurrent(), supplyRequestPage.getSize(), supplyRequestPage.getTotal());
    //
    // // 将【供应申请管理】的分页转化为返回【入库单据分页列表DTO】的分页
    // returnPage.setRecords(supplyRequestPage.getRecords().stream().map(entity -> {
    // // 定义【入库单据分页列表DTO】
    // InventoryReceiptDto dto = new InventoryReceiptDto();
    // // 从主表COPY需要的字段
    // dto.setId(entity.getId());
    // dto.setCategoryEnum(1);
    // // 从【药品基本信息管理】Map取值，设置【药品基本信息管理】返回实体
    // dto.setMedication(medicationMap.getOrDefault(entity.getDispenseId(), null));
    // // 从【患者管理】Map取值，设置【患者管理】返回实体
    // dto.setPatient(patientMap.getOrDefault(entity.getPatientId(), null));
    // return dto;
    // }).collect(Collectors.toList()));
    //
    // // 返回【入库单据分页列表DTO】分页
    // return returnPage;
    // }
}
