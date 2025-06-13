/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.controller;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.crypto.asymmetric.Sign;
import com.openhis.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.core.common.annotation.Anonymous;
import com.openhis.config.InterfaceConfig;
import com.openhis.service.FileService;
import com.openhis.service.YbService;

/**
 * TODO:医保控制层
 *
 * @author SunJQ
 * @date 2025-03-18
 */
@RestController
@RequestMapping("/yb")
public class YbController {

    Logger logger = LoggerFactory.getLogger(YbController.class);
    // logger.debug("这是专用日志测试");
    /** 中心接口配置 */
    @Autowired
    protected InterfaceConfig interfaceConfig;
    /** 医保服务 */
    @Autowired
    private YbService ybService;
    /** 文件服务 */
    @Autowired
    private FileService fileService;

    @PostMapping(value = "/signIn")
    @Anonymous
    public Result<?> signIn(@RequestBody BaseParam sign, HttpServletRequest request) {
        return ybService.signIn(sign, request);
    }

    @PostMapping(value = "/signOut")
    @Anonymous
    public Result<?> signOut(BaseParam baseParam, Sign sign) {
        return ybService.signOut(baseParam);
    }

    @PostMapping(value = "/getPatinfo")
    @Anonymous
    public Result<?> getPerInfo(@RequestBody BaseParam baseParam) {
        return ybService.getPerInfo(baseParam);
    }

    @PostMapping(value = "/reg")
    @Anonymous
    public Result<?> reg(@RequestBody BaseParam baseParam) {
        logger.info(JSON.toJSONString(baseParam));
        return ybService.reg(baseParam);
    }

    @PostMapping(value = "/cancelReg")
    @Anonymous
    public Result<?> cancelReg(@RequestBody BaseParam baseParam) {
        return ybService.cancelReg(baseParam);
    }

    @PostMapping(value = "/preSetl")
    @Anonymous
    public Result<?> preSetl(@RequestBody BaseParam baseParam) {
        return ybService.preSetl(baseParam);
    }

    @PostMapping(value = "/unPreSettle")
    @Anonymous
    public Result<?> unPreSettle(@RequestBody BaseParam baseParam) {
        return ybService.cancelFeeDetail(baseParam);
    }


    @PostMapping(value = "/upload2203-record")
    @Anonymous
    public Result<?> upload2203Record(@RequestBody BaseParam baseParam) {
        return ybService.saveMedical(baseParam);
    }

    @PostMapping(value = "/upload2204-record")
    @Anonymous
    public Result<?> preSettle(@RequestBody BaseParam baseParam) {
        return ybService.upFeeDetail(baseParam);
    }

    @PostMapping(value = "/cancel2205")
    @Anonymous
    public Result<?> cancelFeeDetail(@RequestBody BaseParam baseParam) {
        return ybService.cancelFeeDetail(baseParam);
    }

    @PostMapping(value = "/setl")
    @Anonymous
    public Result<?> setl(@RequestBody BaseParam baseParam) {
        return ybService.setl(baseParam);
    }

    @PostMapping(value = "/cancelSetl")
    @Anonymous
    public Result<?> cancelSetl(@RequestBody BaseParam baseParam) {
        return ybService.cancelSetl(baseParam);
    }

    @PostMapping(value = "/download")
    @Anonymous
    public Result<?> download(BaseParam baseParam, FileDownload fileDownload) {
        return ybService.download(baseParam, fileDownload);
    }

    @PostMapping(value = "/directoryCheck")
    @Anonymous
    public Result<?> directoryCheck(@RequestBody BaseParam baseParam) {
        return ybService.directoryCheck(baseParam);
    }

    @PostMapping(value = "/directoryUnCheck")
    @Anonymous
    public Result<?> directoryUnCheck(@RequestBody BaseParam baseParam) {
        return ybService.directoryUnCheck(baseParam);
    }

    @PostMapping(value = "/reconcile")
    @Anonymous
    public Result<?> reconcileGeneralLedger(@RequestBody BaseParam baseParam) {
        return ybService.reconcileGeneralLedger(baseParam);
    }

    @PostMapping(value = "/reconcile-detail")
    @Anonymous
    public Result<?> reconcileGeneralLedgerDetail(BaseParam baseParam,
        FinancialSettlement3202Param financial3202Param) {
        return ybService.reconcileGeneralLedgerDetail(baseParam, financial3202Param);
    }

    @PostMapping(value = "/three-part-search-err")
    @Anonymous
    public Result<?> threePartSearch(BaseParam baseParam, FinancialSettlement3209AParam financialSettlement3209AParam) {
        return ybService.threePartSearch(baseParam, financialSettlement3209AParam);
    }

    @PostMapping(value = "/apply-clearing")
    @Anonymous
    public Result<?> applyFinancialClearing(BaseParam baseParam, Financial3203AParam financial3203AParam) {
        return ybService.applyFinancialClearing(baseParam, financial3203AParam);
    }

    @PostMapping(value = "/cancel-clearing")
    @Anonymous
    public Result<?> cancelFinancialClearing(BaseParam baseParam, Financial3204Param financial3204Param) {
        return ybService.cancelFinancialClearing(baseParam, financial3204Param);
    }

    @PostMapping(value = "/get-clearing-status")
    @Anonymous
    public Result<?> getFinancialClearingStatus(BaseParam baseParam, Clearing3205AParma clearing3205AParma) {
        return ybService.getFinancialClearingStatus(baseParam, clearing3205AParma);
    }

    @PostMapping(value = "/upload-inventory-count")
    @Anonymous
    public Result<?> uploadInventoryCount(@RequestBody BaseParam baseParam) {
        return ybService.uploadInventoryCount(baseParam);
    }

    @PostMapping(value = "/update-inventory")
    @Anonymous
    public Result<?> updateInventoryCount(@RequestBody BaseParam baseParam) {
        return ybService.updateInventoryCount(baseParam);
    }

    @PostMapping(value = "/procurement")
    @Anonymous
    public Result<?> procurement(@RequestBody BaseParam baseParam) {
        return ybService.procurement(baseParam);
    }

    @PostMapping(value = "/procurement-cancel")
    @Anonymous
    public Result<?> cancelProcurement(@RequestBody BaseParam baseParam) {
        return ybService.cancelProcurement(baseParam);
    }

    @PostMapping(value = "/merchandise")
    @Anonymous
    public Result<?> merchandise(@RequestBody BaseParam baseParam) {
        return ybService.merchandise(baseParam);
    }

    @PostMapping(value = "/cancel-merchandise")
    @Anonymous
    public Result<?> cancelMerchandise(@RequestBody BaseParam baseParam) {
        return ybService.cancelMerchandise(baseParam);
    }

    @PostMapping(value = "/del-goods")
    @Anonymous
    public Result<?> deleteGoodsInfo(BaseParam baseParam, Medical3507Param medical3507Param) {
        return ybService.deleteGoodsInfo(baseParam, medical3507Param);
    }

    @PostMapping(value = "/file-up")
    @Anonymous
    public Result<?> uploadFile(@RequestParam("in") MultipartFile file, // 对应接口参数名"in"
        @RequestParam("filename") String filename, @RequestParam("fixmedins_code") String fixmedinsCode) {
        // 1. 基础校验
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        if (!file.getOriginalFilename().endsWith(".zip")) {
            return Result.error("仅支持ZIP格式文件");
        }

        // 2. 调用Service处理业务逻辑
        try {
            FileResult fileResult = fileService.uploadFile(file.getInputStream(), // 改为直接传递输入流
                filename, fixmedinsCode);
            return Result.ok(fileResult);
        } catch (Exception e) {
            return Result.error("处理失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "/file-down")
    @Anonymous
    public Result<?> downloadFile(FileResult fileResult) {
        return fileService.downloadFile(fileResult);
    }

    @PostMapping(value = "/queryYbCatalogue")
    @Anonymous
    public Result<?> queryYbCatalogue(@RequestBody BaseParam baseParam) {
        return ybService.queryYbCatalogue(baseParam);
    }

}
