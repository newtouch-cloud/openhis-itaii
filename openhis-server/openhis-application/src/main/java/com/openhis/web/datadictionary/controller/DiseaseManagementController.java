package com.openhis.web.datadictionary.controller;

import java.util.List;

import com.core.common.utils.bean.BeanUtils;
import com.openhis.web.datadictionary.dto.DiseaseInDto;
import org.springframework.web.bind.annotation.*;

import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:病种目录
 *
 * @author lpt
 * @date 2025-02-20
 */
@RestController
@RequestMapping("/DataDictionary-DiseaseManagement")
@Slf4j
@AllArgsConstructor
public class DiseaseManagementController {
    private final IConditionDefinitionService iConditionDefinitionService;

    /**
     * 病种目录分类查询
     *
     * @return
     */
    @GetMapping("/GetDiseaseCategory")
    public List<ConditionDefinition> GetDiseaseCategory() {
        return null;
    }

    /**
     * 病种目录查询
     *
     * @param
     * @return
     */
    @GetMapping("/GetDiseaseList")
    public List<ConditionDefinition> GetDiseaseList() {
        return null;
    }

    // 病种目录编辑
    @PutMapping("/EditDisease")
    void EditDisease() {}

    // 新增外来病种目录
    @PostMapping("/AddDisease")
    void AddDisease(DiseaseInDto diseaseInDto) {
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        BeanUtils.copyProperties(diseaseInDto,conditionDefinition);
//        iConditionDefinitionService.AddDisease(conditionDefinition);
    }

    // 新增医保病种目录
    @PostMapping("/AddYbDisease")
    void AddYbDisease(@RequestParam int id) {}
}
