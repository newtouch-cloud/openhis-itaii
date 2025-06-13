package com.openhis.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Component;

/**
 * 生成msg交易id
 */
@Component
public class BusnessIdUtil {

    /**
     * 生成msg_id
     * @return
     */
    public String getMsgid(String fixmedinsCode){
        return fixmedinsCode + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomInt(1000, 9999);
    }

    /**
     * 生成发票号
     * @return
     */
    public static String gettInvoiceNo(String fixmedinsCode){
        return DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6);
    }

    /**
     * 生成费用明细流水号
     * @return
     */
    public String gettFeedetlSn(String fixmedinsCode){
        return fixmedinsCode + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(4);
    }

    /**
     * 生成id
     * @return
     */
    public String getId(String fixmedinsCode){
        return fixmedinsCode + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(10);
    }

    /**
     * 生成医院科室编码
     * @return
     */
    public String getCodg(String fixmedinsCode){
        return fixmedinsCode + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(4);
    }

}