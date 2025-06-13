package com.openhis.web.inventorymanage.appservice.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.core.domain.R;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.InsuranceLevel;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.enums.LocationStatus;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.enums.ybenums.YbChrgitmLv;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inventorymanage.appservice.IProductDetailsAppService;
import com.openhis.web.inventorymanage.dto.ProductDetailsInitDto;
import com.openhis.web.inventorymanage.dto.ProductDetailsPageDto;
import com.openhis.web.inventorymanage.dto.ProductDetailsSearchParam;
import com.openhis.web.inventorymanage.mapper.ProductDetailsMapper;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.mapper.InventoryItemMapper;

/**
 * 库存商品明细 impl
 *
 * @author yuanzs
 * @date 2025-04-24
 */
@Service
public class ProductDetailsAppServiceImpl extends ServiceImpl<InventoryItemMapper, InventoryItem>
    implements IProductDetailsAppService {

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    /**
     * 库存商品明细初始化
     *
     * @return 下拉列表值
     */
    @Override
    public R<?> getInit() {

        ProductDetailsInitDto productDetailsInitDto = new ProductDetailsInitDto();

        // 医保等级List
        List<ProductDetailsInitDto.statusEnumOption> chrgitmLvOptions = Stream.of(InsuranceLevel.values())
            .map(status -> new ProductDetailsInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());

        productDetailsInitDto.setChrgitmLvOptions(chrgitmLvOptions);

        return R.ok(productDetailsInitDto);
    }

    /**
     * 查询库存商品明细分页列表
     *
     * @param productDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 库存商品明细分页列表
     */
    @Override
    public R<?> getPage(ProductDetailsSearchParam productDetailsSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 数据初始化，不使用eq条件拼接
        // 项目类型
        Integer categoryCode = productDetailsSearchParam.getCategoryCode();
        productDetailsSearchParam.setCategoryCode(null);
        // 库存范围
        Integer warehouseScope = productDetailsSearchParam.getWarehouseScope();
        productDetailsSearchParam.setWarehouseScope(null);
        // 剩余过期天数
        Integer remainingDays = productDetailsSearchParam.getRemainingDays();
        productDetailsSearchParam.setRemainingDays(null);

        // 构建查询条件
        QueryWrapper<ProductDetailsSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(productDetailsSearchParam, searchKey,
                new HashSet<>(Arrays.asList("medicine_name", "bus_no")), request);

        // 项目类型为中成药（1）、西药（2）、外购药品（3）、中草药（4）时查药品表；为耗材（9）时查器材表
        if (categoryCode != null) {
            if (categoryCode.equals(1) || categoryCode.equals(2) || categoryCode.equals(3) || categoryCode.equals(4)) {
                queryWrapper.eq("item_type", "0");
                queryWrapper.eq("category_code", categoryCode.toString());
            } else if (categoryCode.equals(9)) {
                queryWrapper.eq("item_type", "9");
            }
        }
        // 库存范围：无限制（1）、数量等于0（2）、数量大于0（3）
        if (warehouseScope != null) {
            if (warehouseScope.equals(2)) {
                queryWrapper.eq("quantity", 0);
            } else if (warehouseScope.equals(3)) {
                queryWrapper.gt("quantity", 0);
            }
        }
        // 查询条件为剩余过期天数=0时
        if (remainingDays != null) {
            if (remainingDays.equals(0)) {
                // 查询 <= 0天的数据
                queryWrapper.le("remaining_days", 0);
            } else {
                //查询 <= remainingDays天的数据
                queryWrapper.le("remaining_days", remainingDays);
            }
        }

        // 查询库存商品明细分页列表
        Page<ProductDetailsPageDto> productDetailsPage =
            productDetailsMapper.selectProductDetailsPage(new Page<>(pageNo, pageSize), queryWrapper);

        productDetailsPage.getRecords().forEach(e -> {
            // 医保等级
            e.setChrgitmLv_enumText(EnumUtils.getInfoByValue(YbChrgitmLv.class, e.getChrgitmLv()));
            // 药品停用
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(PublicationStatus.class, e.getStatusEnum()));
            // 仓库类型
            e.setFormEnum_enumText(EnumUtils.getInfoByValue(LocationForm.class, e.getFormEnum()));
            // 停供状态
            e.setInventoryStatusEnum_enumText(
                EnumUtils.getInfoByValue(PublicationStatus.class, e.getInventoryStatusEnum()));
            // 如果剩余天数小于0，显示为0
            if (e.getRemainingDays() != null && e.getRemainingDays().compareTo(0) <= 0) {
                e.setRemainingDays(0);
            }
        });
        return R.ok(productDetailsPage);
    }

    /**
     * 操作：停供
     *
     * @param id 库存项目管理ID
     * @return 操作结果
     */
    @Override
    public R<?> stopSupplyById(Long id) {

        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 当前登录账号
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 停供按钮压下，更新库存项目管理表的库存状态：已停供
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<InventoryItem>().eq(InventoryItem::getId, id).set(InventoryItem::getUpdateTime, now)
                .set(InventoryItem::getUpdateBy, loginUser.getUserId())
                .set(InventoryItem::getInventoryStatusEnum, PublicationStatus.RETIRED.getValue()));

        return updateCount > 0 ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 操作：取消停供
     *
     * @param id 库存项目管理ID
     * @return 操作结果
     */
    @Override
    public R<?> cancelSupplyById(Long id) {

        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 当前登录账号
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 取消停供按钮压下，更新库存项目管理表的库存状态：未停供
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<InventoryItem>().eq(InventoryItem::getId, id).set(InventoryItem::getUpdateTime, now)
                .set(InventoryItem::getUpdateBy, loginUser.getUserId())
                .set(InventoryItem::getInventoryStatusEnum, PublicationStatus.ACTIVE.getValue()));

        return updateCount > 0 ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }
}
