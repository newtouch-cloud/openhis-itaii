package com.openhis.web.basedatamanage.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.ChineseConvertUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.StringUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.mapper.LocationMapper;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.appservice.ILocationAppService;
import com.openhis.web.basedatamanage.dto.*;

@Service
public class LocationAppServiceImpl implements ILocationAppService {

    @Resource
    private ILocationService locationService;

    @Resource
    private AssignSeqUtil assignSeqUtil;

    @Resource
    private LocationMapper locationMapper;

    /**
     * 位置初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> locationInit() {
        LocationInitDto initDto = new LocationInitDto();
        // 位置状态
        List<LocationInitDto.locationStatusOption> locationStatusOptions = new ArrayList<>();
        locationStatusOptions.add(new LocationInitDto.locationStatusOption(LocationStatus.ACTIVE.getValue(),
            LocationStatus.ACTIVE.getInfo()));
        locationStatusOptions.add(new LocationInitDto.locationStatusOption(LocationStatus.INACTIVE.getValue(),
            LocationStatus.INACTIVE.getInfo()));
        initDto.setLocationStatusOptions(locationStatusOptions);
        return R.ok(initDto);
    }

    /**
     * 位置信息详情
     *
     * @param locationId 位置信息id
     * @return 位置信息详情
     */
    @Override
    public R<?> getLocationById(Long locationId) {
        LocationInfoDto locationInfoDto = new LocationInfoDto();
        BeanUtils.copyProperties(locationService.getById(locationId), locationInfoDto);
        // 位置类型
        locationInfoDto
            .setFormEnum_enumText(EnumUtils.getInfoByValue(LocationForm.class, locationInfoDto.getFormEnum()));
        // 使用状态
        locationInfoDto.setOperationalEnum_enumText(
            EnumUtils.getInfoByValue(LocationOperational.class, locationInfoDto.getOperationalEnum()));
        // 启用停用
        locationInfoDto
            .setStatusEnum_enumText(EnumUtils.getInfoByValue(LocationStatus.class, locationInfoDto.getStatusEnum()));
        return R.ok(locationInfoDto,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"位置信息查询"}));
    }

    /**
     * 删除位置信息
     *
     * @param busNo 位置信息编码
     * @return 操作结果
     */
    @Override
    public R<?> deleteLocation(String busNo) {
        // 删除位置信息(连同子集)
        boolean result =
            locationService.remove(new LambdaQueryWrapper<Location>().likeRight(Location::getBusNo, busNo));
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"位置信息删除"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, new Object[] {"位置信息删除"}));
    }

    /**
     * 位置分页列表
     *
     * @param locationPageParam 查询条件
     * @param searchKey 模糊查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求
     * @return 位置分页列表
     */
    @Override
    public R<?> getLocationPage(LocationPageParam locationPageParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {
        //数据初始化，不使用eq条件拼接
        List<Integer> formList = locationPageParam.getLocationFormList();
        locationPageParam.setLocationFormList(null);
        String busNo = locationPageParam.getBusNo();
        locationPageParam.setBusNo(null);
        // 构建查询条件
        QueryWrapper<Location> queryWrapper = HisQueryUtils.buildQueryWrapper(locationPageParam, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.Name, CommonConstants.FieldName.PyStr,
                CommonConstants.FieldName.WbStr)),
            request);
        // 根据不同的位置类型查询不同的位置分页信息(前端必传默认值)
        if (formList != null && !formList.isEmpty()) {
            queryWrapper.lambda().in(Location::getFormEnum, formList);
        }
        // 根据父节点编码查询子项
        queryWrapper.lambda().likeRight(StringUtils.isNotNull(busNo), Location::getBusNo, busNo);
        if (locationPageParam.getFormEnum() != null) {
            queryWrapper.lambda().eq(Location::getFormEnum, locationPageParam.getFormEnum());
        }

        // 查询位置分页列表
        Page<LocationInfoDto> locationPage =
            HisPageUtils.selectPage(locationMapper, queryWrapper, pageNo, pageSize, LocationInfoDto.class);
        locationPage.getRecords().forEach(e -> {
            // 位置类型
            e.setFormEnum_enumText(EnumUtils.getInfoByValue(LocationForm.class, e.getFormEnum()));
            // 使用状态
            e.setOperationalEnum_enumText(EnumUtils.getInfoByValue(LocationOperational.class, e.getOperationalEnum()));
            // 启用停用
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(LocationStatus.class, e.getStatusEnum()));
        });
        return R.ok(locationPage);
    }

    /**
     * 新增位置信息
     *
     * @param locationAddOrEditDto 库房位置信息
     * @return 操作结果
     */
    @Override
    public R<?> addLocation(LocationAddOrEditDto locationAddOrEditDto) {

        Location location = new Location();
        BeanUtils.copyProperties(locationAddOrEditDto, location);
        location.setFormEnum(Integer.valueOf(locationAddOrEditDto.getFormEnum()));
        // 拼音码
        location.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(locationAddOrEditDto.getName()));
        // 五笔码
        location.setWbStr(ChineseConvertUtils.toWBFirstLetter(locationAddOrEditDto.getName()));
        // 采番bus_no三位
        String code = assignSeqUtil.getSeq(AssignSeqEnum.LOCATION_BUS_NO.getPrefix(), 3);
        // 如果传了上级 把当前的code拼到后边
        if (StringUtils.isNotEmpty(location.getBusNo())) {
            location.setBusNo(String.format(CommonConstants.Common.MONTAGE_FORMAT, location.getBusNo(),
                CommonConstants.Common.POINT, code));
        } else {
            location.setBusNo(code);
        }
        boolean result = locationService.addLocation(location);
        if (result) {
            return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"库房"}));
        }
        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }

    /**
     * 编辑位置信息
     *
     * @param locationAddOrEditDto 库房位置信息
     * @return 操作结果
     */
    @Override
    public R<?> editLocation(LocationAddOrEditDto locationAddOrEditDto) {
        Location location = new Location();
        BeanUtils.copyProperties(locationAddOrEditDto, location);
        // 拼音码
        location.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(locationAddOrEditDto.getName()));
        // 五笔码
        location.setWbStr(ChineseConvertUtils.toWBFirstLetter(locationAddOrEditDto.getName()));
        boolean result = locationService.updateLocation(location);
        if (result) {
            return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"库房"}));
        }
        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 位置分页列表-树型
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 位置分页列表
     */
    @Override
    public R<?> getLocationTree(Integer formKey, Integer pageNo, Integer pageSize) {

        LambdaQueryWrapper<Location> queryWrapper = new LambdaQueryWrapper<>();
        if (formKey != null) {
            queryWrapper.eq(Location::getFormEnum, formKey);
        }

        // 查询位置列表
        Page<Location> page = locationService.page(new Page<>(pageNo, pageSize), queryWrapper);
        List<Location> locationList = page.getRecords();
        // 将位置列表转为树结构
        List<LocationDto> locationTree = buildTree(locationList);
        Page<LocationDto> locationQueryDtoPage = new Page<>(pageNo, pageSize, page.getTotal());
        locationQueryDtoPage.setRecords(locationTree);

        locationQueryDtoPage.getRecords().forEach(e -> {
            // 物理形式枚举回显赋值
            e.setFormEnum_enumText(EnumUtils.getInfoByValue(LocationForm.class, e.getFormEnum()));
            // 状态编码回显赋值
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(LocationStatus.class, e.getStatusEnum()));
            // 操作状态回显赋值
            e.setOperationalEnum_enumText(EnumUtils.getInfoByValue(LocationBedStatus.class, e.getOperationalEnum()));
            // 模式编码回显赋值
            e.setModeEnum_enumText(EnumUtils.getInfoByValue(LocationMode.class, e.getModeEnum()));
        });

        return R.ok(locationQueryDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"位置信息查询"}));
    }

    /**
     * 将位置列表转换为树结构
     *
     * @param records 位置列表
     * @return tree
     */
    private List<LocationDto> buildTree(List<Location> records) {
        // 按b_no的层级排序，确保父节点先处理
        List<Location> sortedRecords = records.stream()
            .sorted(Comparator.comparingInt(r -> r.getBusNo().split("\\.").length)).collect(Collectors.toList());

        Map<String, LocationDto> nodeMap = new HashMap<>();
        List<LocationDto> tree = new ArrayList<>();

        for (Location record : sortedRecords) {
            String bNo = record.getBusNo();
            String[] parts = bNo.split("\\.");
            LocationDto node = new LocationDto();
            BeanUtils.copyProperties(record, node);
            // 将当前节点加入映射
            nodeMap.put(bNo, node);

            if (parts.length == 1) {
                // 根节点
                tree.add(node);
            } else {
                // 获取父节点的b_no（去掉最后一部分）
                String parentBNo = String.join(".", Arrays.copyOf(parts, parts.length - 1));
                LocationDto parent = nodeMap.get(parentBNo);

                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 处理父节点不存在的情况（例如数据缺失）
                    // 可根据需求调整为将节点加入根或抛出异常
                    tree.add(node);
                }
            }
        }
        return tree;
    }
}
