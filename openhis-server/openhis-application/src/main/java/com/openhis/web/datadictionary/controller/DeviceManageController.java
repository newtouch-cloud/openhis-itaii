package com.openhis.web.datadictionary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.appservice.IDeviceManageAppService;
import com.openhis.web.datadictionary.dto.DeviceManageDto;
import com.openhis.web.datadictionary.dto.DeviceManageSelParam;
import com.openhis.web.datadictionary.dto.DeviceManageUpDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:器材目录
 *
 * @author lpt
 * @date 2025-02-20
 */
@RestController
@RequestMapping("/data-dictionary/device")
@Slf4j
@AllArgsConstructor
public class DeviceManageController {

    @Autowired
    private IDeviceManageAppService deviceManageAppService;

    /**
     * 器材目录初期查询
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getDeviceManageInit() {
        return deviceManageAppService.getDeviceManageInit();
    }

    /**
     * 查询器材目录分页列表
     *
     * @param deviceManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDevicePage(DeviceManageSelParam deviceManageSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return deviceManageAppService.getDevicePage(deviceManageSelParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 根据id查询器材详情
     *
     * @param id 器材ID
     * @return
     */
    @GetMapping("/information-one")
    public R<?> getDeviceOne(@RequestParam Long id) {

        return deviceManageAppService.getDeviceOne(id);
    }

    /**
     * 器材目录编辑
     *
     * @param deviceManageDto 器材目录列表
     * @return
     */
    @PutMapping("/information")
    public R<?> editDevice(@RequestBody DeviceManageUpDto deviceManageDto) {

        return deviceManageAppService.editDevice(deviceManageDto);

    }

    /**
     * 器材目录停用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editDeviceStop(@RequestBody List<Long> ids) {
        return deviceManageAppService.editDeviceStop(ids);
    }

    /**
     * 器材目录启用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editDeviceStart(@RequestBody List<Long> ids) {
        return deviceManageAppService.editDeviceStart(ids);
    }

    /**
     * 新增器材目录
     *
     * @param deviceManageUpDto 器材目录
     * @return
     */
    @PostMapping("/information")
    public R<?> addDevice(@Validated @RequestBody DeviceManageUpDto deviceManageUpDto) {
        return deviceManageAppService.addDevice(deviceManageUpDto);
    }

    /**
     * 新增医保器材目录
     *
     * @param DeviceManageUpDto 器材目录
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbDevice(@RequestBody DeviceManageUpDto DeviceManageUpDto) {
        return null;
    }

    /**
     * 器材目录导出
     *
     * @param DeviceManageDto 器材目录
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportDevice(@RequestBody DeviceManageDto DeviceManageDto) {
        return null;
    }
}
