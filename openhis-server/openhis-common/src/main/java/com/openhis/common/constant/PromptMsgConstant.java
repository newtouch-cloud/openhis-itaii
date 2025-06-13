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

        /**
         * 数据更新失败
         */
        String M00011 = "apl.common.M00011";

        /**
         * 数量计算错误
         */
        String M00012 = "apl.common.M00012";
        /**
         * 就诊ID不能是空
         */
        String M00013 = "apl.common.M00013";

        /**
         * 已经审批通过的单号(发放状态是已完成),不能再重复审批通过
         */
        String M00014 = "apl.common.M00014";

        /**
         * 已经审批通过的单号(请求状态是同意),不能再重复编辑请求
         */
        String M00015 = "apl.common.M00015";
    }

    /**
     * 库存
     */
    public interface Inventory {

        /**
         * {0}添加成功
         */
        String M00001 = "apl.inventory.M00001";

        /**
         * 操作失败,库存数量不足
         */
        String M00002 = "apl.inventory.M00002";

    }

    /**
     * 药品发放
     */
    public interface MedicationDispense {

        /**
         * 配药人和发药人是同一人
         */
        String M00001 = "apl.medicationDispense.M00001";

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

    /**
     * 医保相关
     */
    public interface Yb {

        /**
         * {0}目录不存在，请联系管理员
         */
        String M00001 = "apl.yb.M00001";

    }

    /**
     * 组套
     */
    public interface orderGroup {

        /**
         * 组套名称已存在
         */
        String M00001 = "apl.orderGroup.M00001";

    }

    /**
     * 收费
     */
    public interface Payment {

        /**
         * 各缴费渠道实收金额合计不等于实收金额
         */
        String M00001 = "apl.payment.M00001";

        /**
         * 实收金额合计不等于应收金额
         */
        String M00002 = "apl.payment.M00002";

        /**
         * 请选择支付方式
         */
        String M00003 = "apl.payment.M00003";
        /**
         * 查询不到就诊信息
         */
        String M00004 = "apl.payment.M00004";
        /**
         * 已开具项目，不可退号
         */
        String M00005 = "apl.payment.M00005";
        /**
         * 成功收费
         */
        String M00006 = "apl.payment.M00006";
        /**
         * 未查询到收费项目
         */
        String M00007 = "apl.payment.M00007";

    }
    /**
     * 电子发票
     */
    public interface invoice {

        /**
         * 挂号未收费,不能开发票
         */
        String M00002 = "apl.invoice.M00002";
        /**
         * 挂号费已开发票，不能重复开票
         */
        String M00003 = "apl.invoice.M00003";

        /**
         * 发票ID不能是空
         */
        String M00004 = "apl.invoice.M00004";
        /**
         * 发票已冲红
         */
        String M00005 = "apl.invoice.M00005";
        /**
         * 已退号,不能开发票
         */
        String M00006 = "apl.invoice.M00006";
        /**
         * 发票未开具，不能冲红
         */
        String M00007 = "apl.invoice.M00007";
        /**
         * 收费成功，电子发票开具失败
         */
        String M00008 = "apl.invoice.M00008";
    }

    /**
     * 报表
     */
    public interface Report {

        /**
         * 没有可用的报表
         */
        String M00001 = "apl.report.M00001";
        /**
         * 统计类型错误
         */
        String M00002 = "apl.report.M00002";

    }
}
