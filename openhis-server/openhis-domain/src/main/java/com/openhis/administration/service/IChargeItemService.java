package com.openhis.administration.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.ChargeItem;

/**
 * 费用项管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IChargeItemService extends IService<ChargeItem> {

    /**
     * 创建已计费的采购账单
     *
     * @param chargeItemList 采购账单
     */
    void createBilledPurchaseCharge(List<ChargeItem> chargeItemList);

    /**
     * 门诊挂号时保存 费用项
     *
     * @param chargeItem 费用项
     */
    void saveChargeItemByRegister(ChargeItem chargeItem);

    /**
     * 更改就诊患者账户类型
     *
     * @param encounterId 就诊患者
     * @param accountId 账户id
     * @return 更新结果
     */
    boolean updateAccountType(Long encounterId, Long accountId);

    /**
     * 根据收费项目id列表获取收费信息
     *
     * @param chargeItemIdList 收费项目id列表
     * @return 收费信息
     */
    List<ChargeItem> getChargeItemInfo(List<Long> chargeItemIdList);

    /**
     * 更新收费状态：退费中
     *
     * @param chargeItemIdList 收费id列表
     */
    void updateRefundChargeStatus(List<Long> chargeItemIdList);

    /**
     * 根据集合更改收费状态
     * @param chargeItemIdList 实体集合
     * @param value 状态值
     */
    void updatePaymentStatus(List<Long> chargeItemIdList, Integer value);

    /**
     * 根据表名和id删除费用项
     * @param tableName 表名
     * @param serviceId id
     */
    void deleteByServiceTableAndId(String tableName,Long serviceId);

    /**
     * 根据就诊id查询患者因退费重新生成的账单
     *
     * @param encounterId 就诊id
     * @return 重新生成的账单列表
     */
    List<String> getRegenerateCharge(Long encounterId);

    /**
     * 根据请求编号列表查询收费项目信息
     *
     * @param requestIdList 请求id列表
     * @return 收费项目信息
     */
    List<ChargeItem> getChargeItemInfoByReqId(List<Long> requestIdList);
}
