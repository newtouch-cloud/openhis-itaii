package com.openhis.web.datadictionary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.appservice.IMedicationManageAppService;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:药品目录
 *
 * @author lpt
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/data-dictionary/medication")
@Slf4j
@AllArgsConstructor
public class MedicationManageController {

    // private final IMedicationDefinitionService iMedicationDefinitionService;
    // private final IMedicationService iMedicationService;
    // private final MedicationManageSearchMapper medicationManageSearchMapper;

    @Autowired
    private IMedicationManageAppService medicationManageAppService;

    /**
     * 药品目录初始化
     *
     * @return
     */
    @GetMapping("/information-init")
    public R<?> getMedicationInit() {

        return medicationManageAppService.getMedicationInit();

        // MedicationManageInitDto medicationManageInitDto = new MedicationManageInitDto();
        // // 获取状态
        // List<MedicationManageInitDto.statusEnumOption> statusEnumOptions = Stream.of(PublicationStatus.values())
        // .map(status -> new MedicationManageInitDto.statusEnumOption(status.getValue(), status.getInfo()))
        // .collect(Collectors.toList());
        // medicationManageInitDto.setStatusFlagOptions(statusEnumOptions);
        // return R.ok(medicationManageInitDto);
    }

    /**
     * 查询药品目录分页列表
     *
     * @param searchKey 查询条件
     * @param statusEnum 查询条件-状态
     * @param ybMatchFlag 查询条件-是否对码
     * @param categoryCode 查询条件-药品分类
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getMedicationList(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "ybMatchFlag", defaultValue = "-1") Integer ybMatchFlag,
        @RequestParam(value = "statusEnum", defaultValue = "-1") Integer statusEnum,
        @RequestParam(value = "categoryCode", defaultValue = "") String categoryCode,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        return medicationManageAppService.getMedicationList(searchKey, ybMatchFlag, statusEnum, categoryCode, pageNo,
            pageSize, request);

        // // 分页设置
        // Integer offset = (pageNo - 1) * pageSize;
        // // 获取租户ID
        // Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // // 查询药品目录列表
        // List<MedicationManageDto> medicationDetailList = medicationManageSearchMapper.getPage(searchKey, ybMatchFlag,
        // statusEnum, categoryCode, tenantId, pageSize, offset);
        // // 查询总记录数
        // long total =
        // medicationManageSearchMapper.getPageCount(searchKey, ybMatchFlag, statusEnum, categoryCode, tenantId);
        // // 创建Page对象并设置属性
        // Page<MedicationManageDto> medicationManageDtoPage = new Page<>(pageNo, pageSize, total);
        // medicationManageDtoPage.setRecords(medicationDetailList);
        // // 返回【药品录列表DTO】分页
        // return R.ok(medicationManageDtoPage);
    }

    /**
     * 根据id查询药品详情
     *
     * @param id 药品ID
     * @return
     */
    @GetMapping("/information-one/{id}")
    public R<?> getMedicationOne(@PathVariable("id") Long id) {

        return medicationManageAppService.getMedicationOne(id);

        // // 获取租户ID
        // Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // // 查询药品目录列表
        // MedicationManageDto medicationManageDto = medicationManageSearchMapper.getOne(id, tenantId);
        // // 返回【药品录列表DTO】列表
        // return R.ok(medicationManageDto);
    }

    // 药品目录编辑
    @PutMapping("/information")
    public R<?> editMedication(@RequestBody MedicationManageUpDto medicationManageUpDto) {

        return medicationManageAppService.editMedication(medicationManageUpDto);

        // MedicationDefinition medicationDefinition = new MedicationDefinition();
        // Medication medication = new Medication();
        // BeanUtils.copyProperties(medicationManageUpDto, medication); // 子表信息
        // BeanUtils.copyProperties(medicationManageUpDto, medicationDefinition);// 主表信息
        //
        // // 更新子表药品信息
        // if (iMedicationService.updateById(medication)) {
        // // 更新主表药品信息
        // return iMedicationDefinitionService.updateById(medicationDefinition)
        // ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
        // : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        // } else {
        // return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        // }
    }

    /**
     * 药品目录停用
     *
     * @param ids 药品ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editMedicationStop(@RequestBody List<Long> ids) {
        // List<Medication> medicationList = new ArrayList<>();
        // // 取得更新值
        // for (Long detail : ids) {
        // Medication medication = new Medication();
        // medication.setId(detail);
        // medication.setStatusEnum(PublicationStatus.RETIRED);
        // medicationList.add(medication);
        // }
        // // 更新药品信息
        // return iMedicationService.updateBatchById(medicationList)
        // ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
        // : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));

        return medicationManageAppService.editMedicationStop(ids);
    }

    /**
     * 药品目录启用
     *
     * @param ids 药品ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editMedicationStart(@RequestBody List<Long> ids) {

        return medicationManageAppService.editMedicationStart(ids);
        // List<Medication> medicationList = new ArrayList<>();
        // // 取得更新值
        // for (Long detail : ids) {
        // Medication medication = new Medication();
        // medication.setId(detail);
        // medication.setStatusEnum(PublicationStatus.ACTIVE);
        // medicationList.add(medication);
        // }
        // // 更新药品信息
        // return iMedicationService.updateBatchById(medicationList)
        // ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
        // : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 新增外来药品目录
     *
     * @param medicationManageUpDto 药品目录信息
     * @return
     */
    @PostMapping("/information")
    public R<?> addMedication(@Validated @RequestBody MedicationManageUpDto medicationManageUpDto) {

        return medicationManageAppService.addMedication(medicationManageUpDto);

        // MedicationDetail medicationDetail = new MedicationDetail();
        // BeanUtils.copyProperties(medicationManageUpDto, medicationDetail);
        // // 新增主表外来药品目录
        // if (iMedicationDefinitionService.addMedication(medicationDetail)) {
        // // 新增子表外来药品目录
        // return iMedicationService.addMedication(medicationDetail)
        // ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
        // : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
        // } else {
        // return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
        // }
    }

    /**
     * 新增医保药品目录
     *
     * @param medicationManageUpDto 药品目录信息
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbMedication(@RequestBody MedicationManageUpDto medicationManageUpDto) {
        return null;
    }

    /**
     * 药品目录导出
     * 
     * @param searchKey 查询条件
     * @param statusEnum 查询条件-状态
     * @param ybMatchFlag 查询条件-是否对码
     * @param categoryCode 查询条件-药品分类
     * @param response
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportMedication(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "ybMatchFlag", defaultValue = "-1") Integer ybMatchFlag,
        @RequestParam(value = "statusEnum", defaultValue = "-1") Integer statusEnum,
        @RequestParam(value = "categoryCode", defaultValue = "") String categoryCode, HttpServletResponse response) {

        return medicationManageAppService.exportMedication(searchKey, ybMatchFlag, statusEnum, categoryCode, response);

        // // 获取租户ID
        // Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // List<MedicationManageDto> list =
        // medicationManageSearchMapper.getList(searchKey, ybMatchFlag, statusEnum, categoryCode, tenantId);
        // ExcelUtil<MedicationManageDto> util = new ExcelUtil<>(MedicationManageDto.class);
        // util.exportExcel(response, list, "药品目录");
        // return null;
    }
}
