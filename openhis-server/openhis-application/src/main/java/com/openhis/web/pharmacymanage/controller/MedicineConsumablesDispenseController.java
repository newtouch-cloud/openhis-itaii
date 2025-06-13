//package com.openhis.web.pharmacymanage.controller;
//
//import com.core.common.core.domain.R;
//import com.openhis.web.pharmacymanage.dto.EncounterInfoSearchParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import com.openhis.web.pharmacymanage.appservice.IMedicineConsumablesDispenseAppService;
//
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.servlet.http.HttpServletRequest;
//
//
//@RestController
//@Slf4j
//@AllArgsConstructor
//@RequestMapping("/pharmacy-manage/medicine-consumables-dispense")
//public class MedicineConsumablesDispenseController {
//
//    @Autowired
//    public IMedicineConsumablesDispenseAppService iMedicineConsumablesDispenseService;
//
//    /**
//     * 获取页面初始化信息
//     *
//     * @return 初始化信息
//     */
//    @GetMapping(value = "/init")
//    public R<?> consumablesDispenseInit() {
//        return iMedicineConsumablesDispenseService.init();
//    }
//
//    /**
//     * 分页查询就诊病人列表
//     *
//     * @param encounterInfoSearchParam 查询条件
//     * @param pageNo 当前页码
//     * @param pageSize 查询条数
//     * @param request 请求数据
//     * @return 就诊病人列表
//     */
//    @GetMapping("/encounter-list")
//    public R<?> getEncounterInfoList(EncounterInfoSearchParam encounterInfoSearchParam,
//                                     @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
//                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
//        return iMedicineConsumablesDispenseService.getEncounterInfoListPage(encounterInfoSearchParam, pageNo, pageSize,
//                request);
//    }
//
//    /**
//     * 处方单查询
//     *
//     * @param encounterId 就诊Id
//     */
//    @GetMapping("/prescription-list")
//    public R<?> getPatientInfoList(@RequestParam(value = "encounterId") Long encounterId) {
//        return iMedicineConsumablesDispenseService.getPrescriptionInfo(encounterId);
//    }
//
//    /**
//     * 配药
//     *
//     * @param prescriptionNo 处方号
//     * @param preparerId 配药人
//     */
//    @PutMapping("/prepare")
//    public R<?> prepare(@RequestParam(value = "prescriptionNo") String prescriptionNo,
//                        @RequestParam(value = "preparerId") Long preparerId) {
//        return iMedicineConsumablesDispenseService.prepare(prescriptionNo, preparerId);
//    }
//
//    /**
//     * 核对发药
//     *
//     * @param prescriptionNo 处方号
//     */
//    @PutMapping("/consumables-dispense")
//    public R<?> consumablesDispense(@RequestParam(value = "prescriptionNo") String prescriptionNo) {
//        return iMedicineConsumablesDispenseService.consumablesDispense(prescriptionNo);
//    }
//
//    /**
//     * 作废
//     *
//     * @param prescriptionNo 处方号
//     * @param notPerformedReasonEnum 未发药原因
//     */
//    @PutMapping("/consumables-cancel")
//    public R<?> medicineCancel(@RequestParam(value = "prescriptionNo") String prescriptionNo,
//                               @RequestParam(value = "notPerformedReasonEnum") Integer notPerformedReasonEnum) {
//        return iMedicineConsumablesDispenseService.consumablesCancel(prescriptionNo, notPerformedReasonEnum);
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
