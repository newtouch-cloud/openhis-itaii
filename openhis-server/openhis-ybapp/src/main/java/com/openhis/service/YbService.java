/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service;

import cn.hutool.crypto.asymmetric.Sign;
import com.openhis.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * TODO:医保同一服务层
 *
 * @author SunJQ
 * @date 2025-03-19
 */
public interface YbService {

    /**
     * 签到
     *
     * @param sign
     * @param request
     * @return
     */
    Result<?> signIn(BaseParam sign, HttpServletRequest request);

    /**
     * 签到
     *
     * @param sign
     * @param request
     * @return
     */
    Result<?> signOut(BaseParam sign);


    /**
     * 读卡获取身份信息
     *
     * @param baseParam
     * @return
     */
    Result<?> getPerInfo(BaseParam baseParam);

    /**
     * 挂号
     *
     * @param baseParam
     * @return
     */
    Result<?> reg(BaseParam baseParam);

    /**
     * 退号
     *
     * @param baseParam
     * @return
     */
    Result<?> cancelReg(BaseParam baseParam);

    /**
     * 门诊就诊信息上传
     *
     * @param
     * @return
     */
    Result<?> saveMedical(BaseParam baseParam);

    /**
     * 门诊费用明细上传
     *
     * @param baseParam
     * @return
     */
    Result<?> upFeeDetail(BaseParam baseParam);

    /**
     * 门诊费预结算
     *
     * @param baseParam
     * @return
     */
    Result<?> preSetl(BaseParam baseParam);

    /**
     * 门诊费用明细撤销
     * 
     * @param baseParam
     * @return
     */
    Result<?> cancelFeeDetail(BaseParam baseParam);

    /**
     * 门诊费用结算
     *
     * @param
     * @return
     */
    Result<?> setl(BaseParam baseParam);

    /**
     * 门诊费用撤销
     *
     * @param
     * @return
     */
    Result<?> cancelSetl(BaseParam baseParam);

    /**
     * 目录下载
     * 
     * @param fileDownload
     * @return
     */
    Result<?> download(BaseParam baseParam, FileDownload fileDownload);

    /**
     * 预结算
     * 
     * @param baseParam
     * @return
     */
    Result<?> preSettle(BaseParam baseParam);

    /**
     * 目录对照
     * 
     * @param baseParam
     * @return
     */
    Result<?> directoryCheck(BaseParam baseParam);

    /**
     * 目录对照撤销
     * 
     * @param baseParam
     * @return
     */
    Result<?> directoryUnCheck(BaseParam baseParam);

    /**
     * 对总账
     * 
     * @param financial3201Param
     * @return
     */
    Result<?> reconcileGeneralLedger(BaseParam baseParam);

    /**
     * 明细对账
     * 
     * @param financial3202Param
     * @return
     */
    Result<?> reconcileGeneralLedgerDetail(BaseParam baseParam, FinancialSettlement3202Param financial3202Param);

    /**
     * 第三方异常数据结算
     * 
     * @param financialSettlement3209AParam
     * @return
     */
    Result<?> threePartSearch(BaseParam baseParam, FinancialSettlement3209AParam financialSettlement3209AParam);

    /**
     * 清算申请
     * 
     * @param financial3203AParam
     * @return
     */
    Result<?> applyFinancialClearing(BaseParam baseParam, Financial3203AParam financial3203AParam);

    /**
     * 清算申请状态查询
     * 
     * @param clearing3205AParma
     * @return
     */
    Result<?> getFinancialClearingStatus(BaseParam baseParam, Clearing3205AParma clearing3205AParma);

    /**
     * 撤销清算申请
     * 
     * @param financial3204Param
     * @return
     */
    Result<?> cancelFinancialClearing(BaseParam baseParam, Financial3204Param financial3204Param);

    /**
     * 库存盘点上传
     * 
     * @param baseParam
     * @return
     */
    Result<?> uploadInventoryCount(BaseParam baseParam);

    /**
     * 库存信息变更
     * 
     * @param baseParam
     * @return
     */
    Result<?> updateInventoryCount(BaseParam baseParam);

    /**
     * 商品采购
     * 
     * @param baseParam
     * @return
     */
    Result<?> procurement(BaseParam baseParam);

    /**
     * 商品采购撤销
     * 
     * @param baseParam
     * @return
     */
    Result<?> cancelProcurement(BaseParam baseParam);

    /**
     * 商品销售
     * 
     * @param baseParam
     * @return
     */
    Result<?> merchandise(BaseParam baseParam);

    /**
     * 商品销售退货
     * 
     * @param baseParam
     * @return
     */
    Result<?> cancelMerchandise(BaseParam baseParam);

    Result<?> deleteGoodsInfo(BaseParam baseParam, Medical3507Param medical3507Param);

    /**
     * 医保目录信息查询
     *
     * @param baseParam
     * @return
     */
    Result<?> queryYbCatalogue(BaseParam baseParam);

}
