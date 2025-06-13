package com.openhis.web.outpatientmanage.appservice.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.outpatientmanage.appservice.IOutpatientDisposalService;
import com.openhis.web.outpatientmanage.dto.*;
import com.openhis.web.outpatientmanage.mapper.OutpatientDisposalMapper;
import com.openhis.web.pharmacymanage.dto.EncounterInfoSearchParam;
import com.openhis.workflow.domain.DeviceDispense;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IDeviceRequestService;
import com.openhis.workflow.service.IInventoryItemService;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 门诊处置
 *
 * @author yuxj
 * @date 2025/4/10
 */
@Service
public class OutpatientDisposalServiceImpl implements IOutpatientDisposalService {

    @Resource
    OutpatientDisposalMapper outpatientDisposalMapper;

    @Resource
    IOrganizationService iOrganizationService;

    @Resource
    private IServiceRequestService serviceRequestService;

    @Resource
    private IDeviceDispenseService deviceDispenseService;

    @Resource
    private IDeviceRequestService deviceRequestService;

    @Resource
    private IInventoryItemService inventoryItemService;
    @Resource
    IPractitionerService practitionerService;

    /**
     * 获取门诊处置初期数据列表
     *
     * @return 获取门诊处置初期数据列表
     */
    @Override
    public R<?> init() {
        OutpatientDisposalInitDto initDto = new OutpatientDisposalInitDto();

        // 获取科室下拉选列表
        List<Organization> organizationList =
            iOrganizationService.getList(OrganizationType.DEPARTMENT.getValue(), OrganizationClass.CLINIC.getValue());
        List<OutpatientDisposalInitDto.DepartmentOption> organizationOptions = organizationList.stream()
            .map(organization -> new OutpatientDisposalInitDto.DepartmentOption(organization.getId(),
                organization.getName()))
            .collect(Collectors.toList());

        initDto.setDepartmentOptions(organizationOptions);

        return R.ok(initDto);
    }

    /**
     * 分页查询就诊病人列表
     *
     * @param encounterInfoSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 就诊病人列表
     */
    @Override
    public R<?> getEncounterInfoListPage(EncounterInfoSearchParam encounterInfoSearchParam, Integer pageNo,
        Integer pageSize, String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.PatientName);
        searchFields.add(CommonConstants.FieldName.idCard);
        // 构建查询条件
        QueryWrapper<EncounterInfoSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(encounterInfoSearchParam, searchKey, searchFields, request);

        // 查询就诊病人列表
        Page<OutpatientDisposalEncounterInfoPageDto> encounterInfoPageDto =
            outpatientDisposalMapper.selectEncounterInfoListPage(new Page<>(pageNo, pageSize), queryWrapper);

        // 个别项目设定
        encounterInfoPageDto.getRecords().forEach(prescriptionPatientInfoDto -> {
            // 性别
            prescriptionPatientInfoDto.setGenderEnum_enumText(
                EnumUtils.getInfoByValue(AdministrativeGender.class, prescriptionPatientInfoDto.getGenderEnum()));
        });
        return R.ok(encounterInfoPageDto);
    }

    /**
     * 查询诊疗单列表
     *
     * @param encounterId 就诊号
     * @return 诊疗单列表
     */
    @Override
    public R<?> getDisposalInfoList(Long encounterId) {

        // 患者基本信息查询
        OutpatientDisposalPatientInfoDto outpatientDisposalPatientInfoDto =
            outpatientDisposalMapper.selectEncounterPatientInfo(encounterId);
        // 年龄
        outpatientDisposalPatientInfoDto
            .setAge(AgeCalculatorUtil.getAge(outpatientDisposalPatientInfoDto.getBirthDate()));
        // 性别
        outpatientDisposalPatientInfoDto.setGenderEnum_enumText(
            EnumUtils.getInfoByValue(AdministrativeGender.class, outpatientDisposalPatientInfoDto.getGenderEnum()));

        // 信息查询
        List<OutpatientDisposalTempInfoDto> outpatientDisposalTempInfoList = outpatientDisposalMapper
            .selectEncounterActivityInfoList(encounterId, CommonConstants.TableName.WOR_SERVICE_REQUEST,
                CommonConstants.TableName.WOR_DEVICE_REQUEST, ItemType.DEVICE.getValue(), ItemType.ACTIVITY.getValue());

        // 诊疗信息
        List<OutpatientDisposalActivityInfoDto> outpatientDisposalActivityInfoList = new ArrayList<>();
        OutpatientDisposalActivityInfoDto activityInfoDto;
        // 耗材信息
        List<OutpatientDisposalDeviceInfoDto> outpatientDisposalDeviceInfoList = new ArrayList<>();
        OutpatientDisposalDeviceInfoDto deviceInfoDto;
        Long tempID = null;
        // 诊疗信息,耗材信息列表做成
        if (!outpatientDisposalTempInfoList.isEmpty()) {
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (OutpatientDisposalTempInfoDto item : outpatientDisposalTempInfoList) {
                if (!Objects.equals(tempID, item.getId())) {
                    // 诊疗信息
                    activityInfoDto = new OutpatientDisposalActivityInfoDto();
                    activityInfoDto.setId(item.getId());
                    activityInfoDto.setType(item.getType());
                    activityInfoDto.setStatusEnum(item.getStatusEnum());
                    activityInfoDto.setStatusEnum_enumText(item.getStatusEnum_enumText());
                    activityInfoDto.setBusNo(item.getBusNo());
                    activityInfoDto.setActivityId(item.getActivityId());
                    activityInfoDto.setLotNumber(item.getLotNumber());
                    activityInfoDto.setUnitCode(item.getUnitCode());
                    activityInfoDto.setPartPercent(item.getPartPercent());
                    activityInfoDto.setLocationId(item.getLocationId());
                    activityInfoDto.setGroupNo(item.getGroupNo());
                    activityInfoDto.setItemName(item.getItemName());
                    activityInfoDto.setUnitPrice(item.getUnitPrice());
                    activityInfoDto.setTotalPrice(item.getTotalPrice());
                    activityInfoDto.setQuantity(item.getQuantity());
                    activityInfoDto.setFrequency(item.getFrequency());
                    activityInfoDto.setChargeStatusEnum(item.getChargeStatusEnum());
                    activityInfoDto.setChargeStatusEnum_enumText(item.getChargeStatusEnum_enumText());
                    activityInfoDto.setQuantityUnit(item.getQuantityUnit());
                    activityInfoDto.setQuantityUnit_dictText(item.getQuantityUnit_dictText());
                    for (OutpatientDisposalTempInfoDto itemAssist : outpatientDisposalTempInfoList) {
                        if ((Objects.equals(item.getId(), itemAssist.getId()))
                            && (itemAssist.getGroupNo() != null && !itemAssist.getGroupNo().isEmpty())) {
                            // 耗材信息
                            deviceInfoDto = new OutpatientDisposalDeviceInfoDto();
                            deviceInfoDto.setDeviceRequestId(itemAssist.getDeviceRequestId());
                            deviceInfoDto.setDeviceDispenseId(itemAssist.getDeviceDispenseId());
                            deviceInfoDto.setBusNo(itemAssist.getDeviceBusNo());
                            deviceInfoDto.setDeviceId(itemAssist.getDeviceId());
                            deviceInfoDto.setLotNumber(itemAssist.getDeviceLotNumber());
                            deviceInfoDto.setUnitCode(itemAssist.getDeviceUnitCode());
                            deviceInfoDto.setPartPercent(itemAssist.getDevicePartPercent());
                            deviceInfoDto.setLocationId(itemAssist.getDeviceLocationId());
                            deviceInfoDto.setGroupNo(itemAssist.getDeviceGroupNo());
                            deviceInfoDto.setItemName(itemAssist.getDeviceItemName());
                            deviceInfoDto.setUnitPrice(itemAssist.getDeviceUnitPrice());
                            deviceInfoDto.setTotalPrice(itemAssist.getDeviceTotalPrice());
                            deviceInfoDto.setQuantity(itemAssist.getDeviceQuantity());
                            deviceInfoDto.setFrequency(itemAssist.getDeviceFrequency());
                            deviceInfoDto.setStatusEnum(itemAssist.getDeviceStatusEnum());
                            deviceInfoDto.setStatusEnum_enumText(itemAssist.getDeviceStatusEnum_enumText());
                            deviceInfoDto.setQuantityUnit(itemAssist.getDeviceQuantityUnit());
                            deviceInfoDto.setQuantityUnit_dictText(itemAssist.getDeviceQuantityUnit_dictText());
                            outpatientDisposalDeviceInfoList.add(deviceInfoDto);
                        }
                    }
                    activityInfoDto.setOutpatientDisposalDeviceInfoList(outpatientDisposalDeviceInfoList);
                    outpatientDisposalActivityInfoList.add(activityInfoDto);
                    // 合计金额
                    if (item.getStatusEnum().equals(ChargeItemStatus.PLANNED.getValue())
                        || item.getStatusEnum().equals(ChargeItemStatus.BILLABLE.getValue())
                        || item.getStatusEnum().equals(ChargeItemStatus.BILLED.getValue())) {
                        totalPrice = totalPrice.add(item.getTotalPrice());
                    }
                }
                tempID = item.getId();
            }
            outpatientDisposalPatientInfoDto.setTotalPrice(totalPrice);
        }

        OutpatientDisposalInfoDto outpatientDisposalInfoDto = new OutpatientDisposalInfoDto();
        outpatientDisposalInfoDto.setOutpatientDisposalPatientInfoDto(outpatientDisposalPatientInfoDto);
        outpatientDisposalInfoDto.setOutpatientDisposalActivityInfoDtoList(outpatientDisposalActivityInfoList);
        return R.ok(outpatientDisposalInfoDto);
    }

    /**
     * 查询执行列表
     *
     * @param busNo 编码
     * @param activityId 诊疗Id
     * @param type 类型
     * @return 执行列表
     */
    @Override
    public R<?> getExecuteInfoList(String busNo, Long activityId, Integer type) {
        List<OutpatientDisposalExecuteInfoDto> outpatientDisposalExecuteInfoList = new ArrayList<>();
        if (Objects.equals(type, ItemType.ACTIVITY.getValue())) {
            outpatientDisposalExecuteInfoList = outpatientDisposalMapper.selectActivityExecuteInfo(type, busNo,
                activityId, RequestStatus.COMPLETED.getValue());
        } else if (Objects.equals(type, ItemType.DEVICE.getValue())) {
            outpatientDisposalMapper.selectDeviceExecuteInfo(type, busNo, activityId,
                DispenseStatus.COMPLETED.getValue());
        }
        return R.ok(outpatientDisposalExecuteInfoList);
    }

    /**
     * 执行
     *
     * @param itemId 诊疗id
     * @param type 诊疗类型
     * @return 执行结果
     */
    @Override
    public R<?> execute(Long itemId, Integer type) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 当前登录账号
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());

        if (type.equals(ItemType.ACTIVITY.getValue())) {

            // 获取服务申请数据
            ServiceRequest serviceRequest = serviceRequestService.getById(itemId);
            if (serviceRequest == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
            // 诊疗信息查询
            OutpatientDisposalActivityInfoDto infoDto = outpatientDisposalMapper.selectEncounterActivityInfo(itemId,
                CommonConstants.TableName.WOR_SERVICE_REQUEST, ItemType.ACTIVITY.getValue());
            // 耗材信息
            List<OutpatientDisposalDeviceInfoDto> outpatientDisposalDeviceInfoList;
            // 耗材信息查询
            if (infoDto.getGroupNo() != null && !infoDto.getGroupNo().isEmpty()) {
                outpatientDisposalDeviceInfoList =
                    outpatientDisposalMapper.selectEncounterDeviceInfoList(infoDto.getGroupNo(),
                        serviceRequest.getEncounterId(), CommonConstants.TableName.WOR_DEVICE_REQUEST);
                infoDto.setOutpatientDisposalDeviceInfoList(outpatientDisposalDeviceInfoList);
            }

            // 获取执行过的诊疗数据
            List<ServiceRequest> serviceRequestItem =
                serviceRequestService.selectServiceRequestByBasedOnId(infoDto.getId());
            // 获取执行诊疗最大数
            int step = serviceRequestItem.size();
            step += 1;

            // 执行诊疗
            ServiceRequest serviceRequestAddItem = serviceRequestService.createCompletedServiceRequest(serviceRequest,
                now, user, String.format("%03d", step));
            if (serviceRequestAddItem == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
            // 发放数量
            Integer quantity;
            // 获取器材发放数据，执行器材发放
            for (OutpatientDisposalDeviceInfoDto item : infoDto.getOutpatientDisposalDeviceInfoList()) {
                // 获取器材发放数据
                DeviceDispense deviceDispense = deviceDispenseService.getById(item.getDeviceDispenseId());
                if (deviceDispense == null) {
                    return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
                // 发放数量
                try {
                    quantity = deviceDispense.getQuantity() / serviceRequest.getQuantity();
                    // 检查余数
                    if (deviceDispense.getQuantity() % serviceRequest.getQuantity() != 0) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00012, null));
                    }
                } catch (ArithmeticException e) {
                    return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00012, null));
                }

                // 执行器材发放
                DeviceDispense deviceDispenseAddItem = deviceDispenseService
                    .createCompletedDeviceDispense(deviceDispense, now, user, String.format("%03d", step), quantity);
                if (deviceDispenseAddItem == null) {
                    return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
            }

            // 判断所有诊疗是否都执行完毕
            if (step == infoDto.getFrequency()) {
                // 修改服务申请状态
                boolean serviceRequestResult = serviceRequestService.completedStatusEnum(infoDto.getId(), now, user);
                if (!serviceRequestResult) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
                for (OutpatientDisposalDeviceInfoDto item : infoDto.getOutpatientDisposalDeviceInfoList()) {
                    // 修改器材发放状态
                    boolean deviceDispenseResult =
                        deviceDispenseService.completedStatusEnum(item.getDeviceDispenseId(), now, user);
                    if (!deviceDispenseResult) {
                        R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                    // 修改器材申请状态
                    boolean deviceRequestResult =
                        deviceRequestService.completedStatusEnum(item.getDeviceRequestId(), user);
                    if (!deviceRequestResult) {
                        R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }
            }

            // 更新库存
            for (OutpatientDisposalDeviceInfoDto item : infoDto.getOutpatientDisposalDeviceInfoList()) {

                Boolean updateBool = this.updateInventoryQuantity(item.getDeviceId(), item.getLotNumber(),
                    item.getLocationId(), item.getUnitCode(), item.getQuantity(), item.getPartPercent(), "subtract");
                if (!updateBool) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
            }

        } else {

            // 诊疗信息查询
            OutpatientDisposalActivityInfoDto infoDto = outpatientDisposalMapper.selectEncounterActivityDeviceInfo(
                itemId, CommonConstants.TableName.WOR_DEVICE_REQUEST, ItemType.DEVICE.getValue());

            // 获取器材发放数据
            DeviceDispense deviceDispense = deviceDispenseService.getById(infoDto.getId());
            if (deviceDispense == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }

            // 获取执行过的诊疗数据
            List<DeviceDispense> deviceDispenseItem =
                deviceDispenseService.selectDeviceDispenseByBasedOnId(infoDto.getId());
            // 获取执行诊疗最大数
            int step = deviceDispenseItem.size();
            step += 1;

            // 执行器材发放
            DeviceDispense deviceDispenseAddItem = deviceDispenseService.createCompletedDeviceDispense(deviceDispense,
                now, user, String.format("%03d", step), 1);
            if (deviceDispenseAddItem == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
            // 判断所有器材是否都执行完毕
            if (step == infoDto.getFrequency()) {
                // 修改器材发放状态
                boolean deviceDispenseResult = deviceDispenseService.completedStatusEnum(infoDto.getId(), now, user);
                if (!deviceDispenseResult) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
                // 修改器材申请状态
                boolean deviceRequestResult = deviceRequestService.completedStatusEnum(infoDto.getId(), user);
                if (!deviceRequestResult) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
            }
            // 更新库存
            Boolean updateBool =
                this.updateInventoryQuantity(infoDto.getActivityId(), infoDto.getLotNumber(), infoDto.getLocationId(),
                    infoDto.getUnitCode(), infoDto.getQuantity(), infoDto.getPartPercent(), "subtract");
            if (!updateBool) {
                R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 更新库存
     *
     * @param itemId 器材id
     * @param lotNumber 产品批号
     * @param locationId 仓库id
     * @param unitCode 单位
     * @param quantity 数量
     * @param partPercent 拆零比
     * @param type 类型
     * @return 执行结果
     */
    public Boolean updateInventoryQuantity(Long itemId, String lotNumber, Long locationId, String unitCode,
        Integer quantity, BigDecimal partPercent, String type) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 根据耗材的id，产品批号，仓库 查询库存表信息
        List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(itemId, lotNumber,
            locationId, SecurityUtils.getLoginUser().getTenantId());
        InventoryItem inventoryItem = new InventoryItem();
        if (!inventoryItemList.isEmpty()) {
            inventoryItem = inventoryItemList.get(0);
        }
        if (inventoryItem == null) {
            return false;
        }
        // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
        BigDecimal baseQuantity = inventoryItem.getQuantity();
        // 最小数量（最小单位库存数量）
        BigDecimal minQuantity = inventoryItem.getQuantity();
        if (Objects.equals(type, "subtract")) {
            // 计算盘点后库存数量，结果取小单位
            if (unitCode.equals(inventoryItem.getUnitCode())) {
                minQuantity = minQuantity.subtract(BigDecimal.valueOf(quantity));
                // 供应申请的物品计量单位与最小单位相同
            } else {
                minQuantity = minQuantity.subtract(partPercent.multiply(BigDecimal.valueOf(quantity)));
            }
        } else if (Objects.equals(type, "add")) {
            // 计算盘点后库存数量，结果取小单位
            if (unitCode.equals(inventoryItem.getUnitCode())) {
                minQuantity = minQuantity.add(BigDecimal.valueOf(quantity));
                // 供应申请的物品计量单位与最小单位相同
            } else {
                minQuantity = minQuantity.add(partPercent.multiply(BigDecimal.valueOf(quantity)));
            }
        }

        // 更新库存数量
        return inventoryItemService.updateInventoryQuantity(inventoryItem.getId(), baseQuantity, minQuantity, now);
    }

    /**
     * 取消
     *
     * @param busNo 编码
     * @param activityId 诊疗Id
     * @param type 类型
     * @return 执行结果
     */
    @Override
    public R<?> cancel(String busNo, Long activityId, Integer type) {

        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 当前登录账号
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());

        if (Objects.equals(type, ItemType.ACTIVITY.getValue())) {
            // 获取执行列表
            List<OutpatientDisposalExecuteInfoDto> infoList = outpatientDisposalMapper.selectActivityExecuteInfo(type,
                busNo, activityId, RequestStatus.COMPLETED.getValue());

            // 修改执行过的服务申请状态
            boolean serviceRequestResult = serviceRequestService.updateCancelledStatus(
                infoList.get(0).getServiceRequestId(), now, loginUser.getPractitionerId(), loginUser.getDeptId());
            if (!serviceRequestResult) {
                R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }

            for (OutpatientDisposalExecuteInfoDto item : infoList) {
                // 修改执行过的器材发放状态
                boolean deviceDispenseResult =
                    deviceDispenseService.cancelledStatusEnum(item.getDeviceDispenseId(), now, user);
                if (!deviceDispenseResult) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
            }

            // 获取执行过的服务申请数据
            ServiceRequest serviceRequest = serviceRequestService.getById(infoList.get(0).getServiceRequestId());
            if (serviceRequest == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
            // 获取总服务申请数据
            ServiceRequest serviceRequestFather = serviceRequestService.getById(serviceRequest.getBasedOnId());
            if (serviceRequestFather == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }

            // 总服务申请数据已完成时
            if (Objects.equals(serviceRequestFather.getStatusEnum(), RequestStatus.COMPLETED.getValue())) {
                // 修改总服务申请状态
                boolean serviceRequestResultFather =
                    serviceRequestService.activeStatusEnum(serviceRequestFather.getId(), now, user);
                if (!serviceRequestResultFather) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
                for (OutpatientDisposalExecuteInfoDto item : infoList) {
                    // 获取器材发放数据
                    DeviceDispense deviceDispense = deviceDispenseService.getById(item.getDeviceDispenseId());
                    if (deviceDispense == null) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                    // 获取总器材发放数据
                    DeviceDispense deviceDispenseFather = deviceDispenseService.getById(deviceDispense.getBasedOnId());
                    if (deviceDispenseFather == null) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                    // 修改执行过的器材发放状态
                    boolean deviceDispenseResultFather =
                        deviceDispenseService.inProgressStatusEnum(item.getDeviceDispenseId(), now, user);
                    if (!deviceDispenseResultFather) {
                        R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                    // 修改执行过的器材申请状态
                    boolean deviceRequestResultFather =
                        deviceRequestService.activeStatusEnum(item.getDeviceRequestId(), user);
                    if (!deviceRequestResultFather) {
                        R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }
            }
            // 更新库存
            for (OutpatientDisposalExecuteInfoDto item : infoList) {
                Boolean updateBool = this.updateInventoryQuantity(item.getDeviceId(), item.getLotNumber(),
                    item.getLocationId(), item.getUnitCode(), item.getQuantity(), item.getPartPercent(), "add");
                if (!updateBool) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
            }
        } else {
            // 获取执行列表
            List<OutpatientDisposalExecuteInfoDto> infoList = outpatientDisposalMapper.selectDeviceExecuteInfo(type,
                busNo, activityId, DispenseStatus.COMPLETED.getValue());

            // 修改执行过的器材发放状态
            boolean deviceDispenseResult =
                deviceDispenseService.cancelledStatusEnum(infoList.get(0).getDeviceDispenseId(), now, user);
            if (!deviceDispenseResult) {
                R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
            // 获取器材发放数据
            DeviceDispense deviceDispense = deviceDispenseService.getById(infoList.get(0).getDeviceDispenseId());
            if (deviceDispense == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
            // 获总取器材发放数据
            DeviceDispense deviceDispenseFather = deviceDispenseService.getById(deviceDispense.getBasedOnId());
            if (deviceDispenseFather == null) {
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
            // 总服务申请数据已完成时
            if (Objects.equals(deviceDispenseFather.getStatusEnum(), DispenseStatus.COMPLETED.getValue())) {
                // 修改执行过的器材发放状态
                boolean deviceDispenseResultFather =
                    deviceDispenseService.inProgressStatusEnum(infoList.get(0).getDeviceDispenseId(), now, user);
                if (!deviceDispenseResultFather) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
                // 修改执行过的器材申请状态
                boolean deviceRequestResultFather =
                    deviceRequestService.activeStatusEnum(infoList.get(0).getDeviceRequestId(), user);
                if (!deviceRequestResultFather) {
                    R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                }
            }
            // 更新库存
            Boolean updateBool = this.updateInventoryQuantity(infoList.get(0).getDeviceId(),
                infoList.get(0).getLotNumber(), infoList.get(0).getLocationId(), infoList.get(0).getUnitCode(),
                infoList.get(0).getQuantity(), infoList.get(0).getPartPercent(), "add");
            if (!updateBool) {
                R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
            }
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }
}
