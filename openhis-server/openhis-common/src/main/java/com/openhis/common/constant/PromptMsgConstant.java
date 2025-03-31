/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.constant;

/**
 * APL消息ID常量类
 *
 * @author zwh
 * @date 2025-02-25
 */
public class PromptMsgConstant {

    /**
     * 共用
     */
    public interface Common {
        /**
         * {0}添加成功
         */
        String M00001 = "apl.common.M00001";
        /**
         * {0}保存成功
         */
        String M00002 = "apl.common.M00002";
        /**
         * {0}已经存在
         */
        String M00003 = "apl.common.M00003";
        /**
         * {0}操作成功
         */
        String M00004 = "apl.common.M00004";
        /**
         * {0}删除成功
         */
        String M00005 = "apl.common.M00005";
        /**
         * 操作失败,该数据已被他人删除,请刷新后重试
         */
        String M00006 = "apl.common.M00006";
        /**
         * 操作失败,该数据已被他人更改,请刷新后重试
         */
        String M00007 = "apl.common.M00007";
        /**
         * 请勿重复提交
         */
        String M00008 = "apl.common.M00008";
        /**
         * 查询成功
         */
        String M00009 = "apl.common.M00009";

        /**
         * 操作失败,请联系管理员
         */
        String M00010 = "apl.common.M00010";

    }

    /**
     * 库存
     */
    public interface Inventory {

        /**
         * {0}添加成功
         */
        String M00001 = "apl.inventory.M00001";

    }

    /**
     * 退费
     */
    public interface ChargeRefund {

        /**
         * 该收费单相关{0}已经发出，请先退药后再进行退费
         */
        String M00001 = "apl.chargeRefund.M00001";

        /**
         * 该收费单相关诊疗项目已经执行，请等待医技科室审批完成后再进行退费
         */
        String M00002 = "apl.chargeRefund.M00002";

    }
}
