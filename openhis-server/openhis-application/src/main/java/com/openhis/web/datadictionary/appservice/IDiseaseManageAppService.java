package com.openhis.web.datadictionary.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.DiseaseManageSelParam;
import com.openhis.web.datadictionary.dto.DiseaseManageUpDto;

/**
 * 病种目录
 *
 * @author liuhr
 * @date 2025/3/30
 */
public interface IDiseaseManageAppService {

    /**
     * 病种目录初始化
     *
     * @return
     */
    R<?> getDiseaseInit();

    /**
     * 查询病种目录分页列表
     *
     * @param diseaseManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    R<?> getDiseaseList(DiseaseManageSelParam diseaseManageSelParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 根据id查询疾病详情
     *
     * @param id 疾病ID
     * @return
     */
    R<?> getDiseaseOne(Long id);

    /**
     * 病种目录编辑
     *
     * @param diseaseManageDto 病种目录列表
     * @return
     */
    R<?> editDisease(DiseaseManageUpDto diseaseManageDto);

    /**
     * 病种目录停用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    R<?> editDiseaseStop(List<Long> ids);

    /**
     * 病种目录启用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    R<?> editDiseaseStart(List<Long> ids);

    /**
     * 新增外来病种目录
     *
     * @param diseaseManageUpDto 病种目录
     * @return
     */
    R<?> addDisease(DiseaseManageUpDto diseaseManageUpDto);

}
