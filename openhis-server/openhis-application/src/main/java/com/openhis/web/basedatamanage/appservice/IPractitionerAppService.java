package com.openhis.web.basedatamanage.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.dto.SelectableOrgDto;
import com.openhis.web.basedatamanage.dto.UserAndPractitionerDto;

import java.util.List;

/**
 * 参与者 应该服务类
 */
public interface IPractitionerAppService {

    /**
     * 新增用户及参与者
     * 
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    R<?> saveUserPractitioner(UserAndPractitionerDto userAndPractitionerDto);

    /**
     * 查询用户及参与者
     * 
     * @param userAndPractitionerDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 用户及参与者
     */
    IPage<UserAndPractitionerDto> getUserPractitionerPage(UserAndPractitionerDto userAndPractitionerDto,
        String searchKey, Integer pageNo, Integer pageSize);

    /**
     * 修改用户及参与者
     *
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    R<?> editUserPractitioner(UserAndPractitionerDto userAndPractitionerDto);

    /**
     * 删除用户及参与者 ; admin不允许删除
     *
     * @param userId 系统用户id
     * @return 结果
     */
    R<?> delUserPractitioner(Long userId);

    /**
     * 查询可选择切换科室集合
     * 
     * @return 可选择切换科室集合
     */
    List<SelectableOrgDto> getSelectableOrgList();

    /**
     * 切换科室
     *
     * @param orgId 科室id
     * @return 结果
     */
    R<?> switchOrg(Long orgId);

}
