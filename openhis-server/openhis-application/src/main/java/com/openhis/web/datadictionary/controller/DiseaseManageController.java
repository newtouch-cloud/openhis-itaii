package com.openhis.web.datadictionary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.web.datadictionary.appservice.IDiseaseManageAppService;
import com.openhis.web.datadictionary.dto.DiseaseManageDto;
import com.openhis.web.datadictionary.dto.DiseaseManageSelParam;
import com.openhis.web.datadictionary.dto.DiseaseManageUpDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:病种目录
 *
 * @author lpt
 * @date 2025-02-20
 */
@RestController
@RequestMapping("/data-dictionary/disease")
@Slf4j
@AllArgsConstructor
public class DiseaseManageController {

    @Autowired
    private IDiseaseManageAppService diseaseManageAppService;

    /**
     * 病种目录初始化
     * 
     * @return
     */
    @GetMapping("/information-init")
    public R<?> getDiseaseInit() {

        return diseaseManageAppService.getDiseaseInit();
    }

    /**
     * 查询病种目录分页列表
     *
     * @param diseaseManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDiseaseList(DiseaseManageSelParam diseaseManageSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 返回【病种目录列表DTO】分页
        return diseaseManageAppService.getDiseaseList(diseaseManageSelParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 根据id查询疾病详情
     *
     * @param id 疾病ID
     * @return
     */
    @GetMapping("/information-one")
    public R<?> getDiseaseOne(@RequestParam Long id) {

        // 根据ID查询【病种目录】
        return diseaseManageAppService.getDiseaseOne(id);
    }

    /**
     * 病种目录编辑
     * 
     * @param diseaseManageDto 病种目录列表
     * @return
     */
    @PutMapping("/information")
    public R<?> editDisease(@RequestBody DiseaseManageUpDto diseaseManageDto) {

        ConditionDefinition conditionDefinition = new ConditionDefinition();
        BeanUtils.copyProperties(diseaseManageDto, conditionDefinition);

        // 更新病种信息
        return diseaseManageAppService.editDisease(diseaseManageDto);
    }

    /**
     * 病种目录停用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editDiseaseStop(@RequestBody List<Long> ids) {
        // 更新病种信息
        return diseaseManageAppService.editDiseaseStop(ids);
    }

    /**
     * 病种目录启用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editDiseaseStart(@RequestBody List<Long> ids) {

        // 更新病种信息
        return diseaseManageAppService.editDiseaseStart(ids);
    }

    /**
     * 新增外来病种目录
     * 
     * @param diseaseManageUpDto 病种目录
     * @return
     */
    @PostMapping("/information")
    public R<?> addDisease(@Validated @RequestBody DiseaseManageUpDto diseaseManageUpDto) {

        return diseaseManageAppService.addDisease(diseaseManageUpDto);
    }

    /**
     * 新增医保病种目录
     * 
     * @param diseaseManageUpDto 病种目录
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbDisease(@RequestBody DiseaseManageUpDto diseaseManageUpDto) {
        return null;
    }

    /**
     * 病种目录导出
     *
     * @param diseaseManageDto 病种目录
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportDisease(@RequestBody DiseaseManageDto diseaseManageDto) {
        return null;
    }
}
