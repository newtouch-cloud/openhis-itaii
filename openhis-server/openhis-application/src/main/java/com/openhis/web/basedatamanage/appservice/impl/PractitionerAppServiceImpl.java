package com.openhis.web.basedatamanage.appservice.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.basedatamanage.mapper.PractitionerAppAppMapper;

@Service
public class PractitionerAppServiceImpl implements IPractitionerAppService {

    @Resource
    PractitionerAppAppMapper practitionerAppAppMapper;

}
