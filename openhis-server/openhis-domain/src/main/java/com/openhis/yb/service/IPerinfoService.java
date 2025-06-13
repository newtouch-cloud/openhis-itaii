package com.openhis.yb.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.yb.domain.InfoPerson;

/**
 * @Description: 人员基础信息
 * @Author: jeecg-boot
 * @Date:   2021-08-27
 * @Version: V1.0
 */
public interface IPerinfoService extends IService<InfoPerson> {
    InfoPerson getPerInfoByIdCard(String idCard,Integer tenantId);
}
